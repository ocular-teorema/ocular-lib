package ru.ddg.stalt.ocular.lib.impl.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.impl.contracts.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.BaseResponse;
import ru.ddg.stalt.ocular.lib.impl.model.OcularConnection;
import ru.ddg.stalt.ocular.lib.model.*;
import ru.ddg.stalt.ocular.lib.services.OcularService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OcularServiceImpl implements OcularService {
    @Autowired
    private QueueService queueService;

    @Override
    public Connection connect(String address, int port, String username, String password, long responseTimeout) throws IOException, TimeoutException {
         com.rabbitmq.client.Connection connection = queueService.createConnection(address, port, username, password);
         return new OcularConnection(connection, responseTimeout);
    }

    @Override
    public void disconnect(Connection connection) throws IOException {
        OcularConnection ocularConnection = (OcularConnection)connection;
        ocularConnection.getConnection().close();
    }

    @Override
    public ServerState getServerState(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/status";

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(),server,"status");

        ServerStateDto serverStateDto = queueService.send((OcularConnection) connection, baseRequest, ServerStateDto.class);
        if (!serverStateDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(serverStateDto.getErrorDescription());
        }

        ServerHardwareInfo hardwareInfo = new ServerHardwareInfo();

        hardwareInfo.setCpuUtilization(serverStateDto.getHardware().getCpuUtilization());
        hardwareInfo.setDefaultVideoPath(serverStateDto.getHardware().getDefaultVideoPath());
        hardwareInfo.setDiscUsage(serverStateDto.getHardware().getDiscUsage());
        hardwareInfo.setIpAddress(serverStateDto.getHardware().getIpAddress());
        hardwareInfo.setOcularVersion(serverStateDto.getHardware().getOcularVersion());
        hardwareInfo.setUptime(serverStateDto.getHardware().getUptime());
        hardwareInfo.setLoadAverage(serverStateDto.getHardware().getLoadAverage());

        List<ServiceState> services = new ArrayList<>();
        for (ServiceStateDto dto: serverStateDto.getServices()) {
            ServiceState serviceState = new ServiceState();
            serviceState.setName(dto.getName());
            serviceState.setStatus(dto.getStatus());
            serviceState.setErrorCode(dto.getErrorCode());
            serviceState.setErrorMessage(dto.getErrorMessage());

            services.add(serviceState);
        }

        List<CameraState> cameraStates = new ArrayList<>();
        for (CameraStateDto stateDto : serverStateDto.getCameras()) {
            CameraState cameraState = new CameraState();
            cameraState.setCameraId(stateDto.getCameraId());
            cameraState.setStatus(stateDto.getStatus());
            cameraState.setErrorCode(stateDto.getErrorCode());
            cameraState.setErrorMessage(stateDto.getErrorMessage());

            cameraStates.add(cameraState);
        }

        return new ServerState(hardwareInfo,services,cameraStates);
    }

    @Override
    public void resetServer(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/reset";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "reset_request");

        BaseResponse response = queueService.send((OcularConnection)connection,baseRequest, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    public void addCamera(Connection connection, Camera camera, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/cameras/add";

        CameraDto cameraDto = new CameraDto();
        cameraDto.setName(camera.getName());
        cameraDto.setStorageDays(camera.getStorageDays());
        cameraDto.setAnalysisType(camera.getAnalysisType());
        cameraDto.setPrimaryAddress(camera.getPrimaryAddress());
        cameraDto.setSecondaryAddress(camera.getSecondaryAddress());
        cameraDto.setScheduleId(camera.getScheduleId());
        cameraDto.setStorageId(camera.getStorageId());

        CameraRequest addCameraRequest = new CameraRequest(UUID.randomUUID(), server, "cameras_add");
        addCameraRequest.setCameraDto(cameraDto);

        BaseResponse response = queueService.send((OcularConnection)connection, addCameraRequest, BaseResponse.class);
        if(response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void removeCamera(Connection connection, String serverName, String cameraId) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/delete";

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "cameras_delete");
        BaseResponse response = queueService.send((OcularConnection)connection, baseRequest, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void updateCamera(Connection connection, String serverName, Camera camera) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/update";
        CameraRequest request = new CameraRequest(UUID.randomUUID(), server, "cameras_update");
        request.setCameraId(camera.getCameraId());

        CameraDto dto = new CameraDto();
        dto.setName(camera.getName());
        dto.setPrimaryAddress(camera.getPrimaryAddress());
        dto.setSecondaryAddress(camera.getSecondaryAddress());
        dto.setAnalysisType(camera.getAnalysisType());
        dto.setStorageDays(camera.getStorageDays());
        dto.setStorageId(camera.getStorageId());
        dto.setScheduleId(camera.getScheduleId());

        request.setCameraDto(dto);

        BaseResponse response = queueService.send((OcularConnection)connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public List<Camera> getCameraList(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/list";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "cameras_list");
        CameraListDto cameraListDto = queueService.send((OcularConnection)connection, baseRequest, CameraListDto.class);
        if (!cameraListDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(cameraListDto.getErrorDescription());
        }
        List<Camera> cameras = new ArrayList<>();
        for (CameraDto dto : cameraListDto.getCameras()) {
            Camera camera = convertCamera(dto);
            cameras.add(camera);
        }

        return cameras;
    }

    @Override
    public void setRecording(Connection connection, String serverName, Camera camera, boolean isRecording) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/cameras/set_recording";
        RecordingRequest recordingRequest = new RecordingRequest(UUID.randomUUID(), server, "cameras_set_recording");
        recordingRequest.setCameraId(camera.getCameraId());
        recordingRequest.setIsRecording(isRecording);
        BaseResponse response = queueService.send((OcularConnection)connection, recordingRequest, BaseResponse.class);

        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());

    }

    @Override
    public void ptzControl(Connection connection, String serverName, String cameraId, int vertical, int horizontal, int zoom) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/cameras/ptz_control";

        if (vertical != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, vertical);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_move_vertical");
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection)connection, request, BaseResponse.class);
            if (response.isSuccess()) {
                return;
            }
            //TODO exceptions
            throw new Exception(response.getErrorDescription());
        }
        if (horizontal != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, horizontal);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_move_horizontal");
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection)connection, request, BaseResponse.class);
            if (response.isSuccess()) {
                return;
            }
            //TODO exceptions
            throw new Exception(response.getErrorDescription());
        }
        if (zoom != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, zoom);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_zoom");
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection)connection, request, BaseResponse.class);
            if (response.isSuccess()) {
                return;
            }
            //TODO exceptions
            throw new Exception(response.getErrorDescription());
        }
    }

    @Override
    public List<Storage> getStorageList(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/list";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "storage_list");
        StorageListDto storageListDto = queueService.send((OcularConnection)connection, baseRequest, StorageListDto.class);
        if (!storageListDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(storageListDto.getErrorDescription());
        }

        List<Storage> storages = new ArrayList<>();
        for (StorageDto dto : storageListDto.getStorages()) {
            Storage storage = convertStorage(dto);

            storages.add(storage);
        }

        return storages;
    }

    @Override
    public void addStorage(Connection connection, String serverName, String storageName, String storagePath) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/add";
        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest addStorageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_add");
        addStorageRequest.setStorageDto(storageDto);

        BaseResponse response = queueService.send((OcularConnection)connection, addStorageRequest, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void updateStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/update";
        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_update");
        storageRequest.setStorageId(storageId);
        storageRequest.setStorageDto(storageDto);

        BaseResponse response = queueService.send((OcularConnection) connection, storageRequest, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void deleteStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/delete";
        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_delete_request");
        storageRequest.setStorageId(storageId);

        BaseResponse response = queueService.send((OcularConnection) connection, storageRequest, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void addSchedule(Connection connection, String serverName, List<Integer> weekDays) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/add";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("weekdays");
        dto.setWeekDays(weekDays);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_add");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void addSchedule(Connection connection, String serverName, int startTimestamp, int stopTimestamp) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/add";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("timestamp");
        dto.setStartTimestamp(startTimestamp);
        dto.setStartTimestamp(stopTimestamp);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_add");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void addSchedule(Connection connection, String serverName, String startTime, String stopTime) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/add";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("time_period");
        dto.setStartTime(startTime);
        dto.setStartTime(stopTime);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_add");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void updateSchedule(Connection connection, String serverName, List<Integer> weekDays) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/update";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("weekdays");
        dto.setWeekDays(weekDays);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_update");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection)connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void updateSchedule(Connection connection, String serverName, int startTimestamp, int stopTimestamp) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/update";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("timestamp");
        dto.setStartTimestamp(startTimestamp);
        dto.setStartTimestamp(stopTimestamp);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_update");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void updateSchedule(Connection connection, String serverName, String startTime, String stopTime) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/update";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("time_period");
        dto.setStartTime(startTime);
        dto.setStartTime(stopTime);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_update");
        request.setScheduleDto(dto);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public void deleteSchedule(Connection connection, String serverName, int scheduleId) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/delete";
        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedules_delete");
        request.setScheduleId(scheduleId);

        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    @Override
    public List<Record> getVideoArchive(Connection connection, String serverName, Integer startTimestamp, Integer stopTimestamp, List<String> cameras, Integer skip, Integer limit) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/archive/archive/video/request";
        ArchiveRecordRequest request = new ArchiveRecordRequest(UUID.randomUUID(), server, "archive_video");
        request.setCameraIds(cameras);
        request.setStartTimestamp(startTimestamp);
        request.setEndTimestamp(stopTimestamp);
        request.setLimit(limit);
        request.setSkip(skip);

        RecordListDto recordListDto = queueService.send((OcularConnection) connection, request, RecordListDto.class);
        if (!recordListDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(recordListDto.getErrorDescription());
        }

        List<Record> records = new ArrayList<>();
        for (RecordDto dto : recordListDto.getRecords()) {
            Record record = new Record();
            record.setId(dto.getId());
            record.setCameraId(dto.getCameraId());
            record.setStartTimestamp(dto.getStartTimestamp());
            record.setEndTimestamp(dto.getEndTimestamp());
            record.setFileSize(dto.getFileSize());

            records.add(record);
        }

        return records;
    }

    @Override
    public List<Schedule> getScheduleList(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/list/request";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "schedule_list");
        ScheduleListDto scheduleListDto = queueService.send((OcularConnection)connection, baseRequest, ScheduleListDto.class);
        if (!scheduleListDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(scheduleListDto.getErrorDescription());
        }
        List<Schedule> scheduleList = new ArrayList<>();
        for (ScheduleDto dto : scheduleListDto.getSchedules()) {
            Schedule schedule = convertSchedule(dto);

            scheduleList.add(schedule);
        }

        return scheduleList;
    }

    @Override
    public List<Organization> exportConfig(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/config/export";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "config_export");
        OrganizationListDto organizationListDto = queueService.send((OcularConnection) connection, baseRequest, OrganizationListDto.class);
        if (!organizationListDto.isSuccess()) {
            //TODO exceptions
            throw new Exception(organizationListDto.getErrorDescription());
        }
        List<Organization> organizations = new ArrayList<>();
        for (OrganizationDto dto : organizationListDto.getOrganizations()) {
            Organization organization = new Organization();
            organization.setName(dto.getName());

            List<Server> serverList = new ArrayList<>();
            for (ServerDto serverDto : dto.getServers()) {
                Server serverObj = new Server();
                serverObj.setId(serverDto.getServerId());
                serverObj.setName(serverDto.getName());

                List<Camera> cameraList = new ArrayList<>();
                for (CameraDto cameraDto : serverDto.getCameras()) {
                    Camera camera = convertCamera(cameraDto);
                    cameraList.add(camera);
                }

                List<Schedule> scheduleList = new ArrayList<>();
                for (ScheduleDto scheduleDto : serverDto.getSchedules()) {
                    Schedule schedule = convertSchedule(scheduleDto);

                    scheduleList.add(schedule);
                }

                List<Storage> storageList = new ArrayList<>();
                for (StorageDto storageDto : serverDto.getStorages()) {
                    Storage storage = convertStorage(storageDto);

                    storageList.add(storage);
                }
                serverObj.setCameras(cameraList);
                serverObj.setSchedules(scheduleList);
                serverObj.setStorages(storageList);

                serverList.add(serverObj);
            }

            organization.setServers(serverList);
            organizations.add(organization);
        }
        return organizations;
    }

    @Override
    public void importConfig(Connection connection, String serverName, List<Organization> organizations) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/config/import";
        ConfigImportRequest request = new ConfigImportRequest(UUID.randomUUID(), server, "config_import");
        request.setOrganizations(organizations);
        BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);
        if (response.isSuccess()) {
            return;
        }
        //TODO exceptions
        throw new Exception(response.getErrorDescription());
    }

    private void checkConnection(Connection connection) throws WrongConnectionException {
        if (connection == null) {
            throw new WrongConnectionException("Wrong connection");
        }
        if (!(connection instanceof OcularConnection)) {
            throw new WrongConnectionException("Wrong connection");
        }
    }

    private void checkServerName(String serverName) throws IncorrectServerNameException {
        if (serverName == null || serverName.isEmpty()) {
            throw new IncorrectServerNameException("Wrong server name");
        }
    }

    private Camera convertCamera(CameraDto cameraDto) {
        Camera camera = new Camera();
        camera.setCameraId(cameraDto.getCameraId());
        camera.setName(cameraDto.getName());
        camera.setAnalysisType(cameraDto.getAnalysisType());
        camera.setPrimaryAddress(cameraDto.getPrimaryAddress());
        camera.setSecondaryAddress(cameraDto.getSecondaryAddress());
        camera.setStatus(cameraDto.getStatus());
        camera.setStorageDays(cameraDto.getStorageDays());
        camera.setStorageId(cameraDto.getStorageId());
        camera.setScheduleId(cameraDto.getScheduleId());
        camera.setStreamAddress(cameraDto.getStreamAddress());

        return camera;
    }

    private Storage convertStorage(StorageDto storageDto) {
        Storage storage = new Storage();
        storage.setId(storageDto.getId());
        storage.setName(storageDto.getName());
        storage.setDefaultArchivePath(storageDto.getDefaultArchivePath());

        return storage;
    }

    private Schedule convertSchedule(ScheduleDto scheduleDto) {
        Schedule schedule = new Schedule();
        schedule.setId(scheduleDto.getId());
        schedule.setType(scheduleDto.getType());
        schedule.setWeekDays(scheduleDto.getWeekDays());
        schedule.setStartTime(scheduleDto.getStartTime());
        schedule.setStopTime(scheduleDto.getStopTime());
        schedule.setStartTimestamp(scheduleDto.getStartTimestamp());
        schedule.setStopTimestamp(scheduleDto.getStopTimestamp());

        return schedule;
    }
}
