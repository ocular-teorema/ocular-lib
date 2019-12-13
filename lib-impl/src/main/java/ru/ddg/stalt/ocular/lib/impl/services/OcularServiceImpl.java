package ru.ddg.stalt.ocular.lib.impl.services;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ddg.stalt.ocular.lib.exceptions.DuplicateDriverIdException;
import ru.ddg.stalt.ocular.lib.impl.contracts.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.responses.*;
import ru.ddg.stalt.ocular.lib.impl.model.OcularConnection;
import ru.ddg.stalt.ocular.lib.model.*;
import ru.ddg.stalt.ocular.lib.services.OcularService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
public class OcularServiceImpl implements OcularService {
    @Autowired
    private QueueService queueService;

    @Autowired
    private ResponseService responseService;

    @Override
    public Connection connect(String address, int port, String username, String password, long responseTimeout, String driverId) throws IOException, TimeoutException, DuplicateDriverIdException {
        com.rabbitmq.client.Connection connection = queueService.createConnection(address, port, username, password);
        responseService.subscribe(driverId, connection);
        return new OcularConnection(driverId, connection, responseTimeout, this);
    }

    @Override
    public void disconnect(Connection connection) throws IOException, TimeoutException {
        OcularConnection ocularConnection = (OcularConnection) connection;
        responseService.unsubscribe(ocularConnection.getDriverId());
        ocularConnection.getConnection().close();
    }

    @Override
    public ServerState getServerState(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        ServerStatusRequest stateRequest = new ServerStatusRequest(UUID.randomUUID(), serverName);

        ServerStateResponse serverStateResponse = queueService.send((OcularConnection) connection, stateRequest, ServerStateResponse.class);

        ServerHardwareInfo hardwareInfo = new ServerHardwareInfo();

        hardwareInfo.setCpuUtilization(serverStateResponse.getData().getHardware().getCpuUtilization());
        hardwareInfo.setDefaultVideoPath(serverStateResponse.getData().getHardware().getDefaultVideoPath());
        hardwareInfo.setDiscUsage(serverStateResponse.getData().getHardware().getDiscUsage());
        hardwareInfo.setIpAddress(serverStateResponse.getData().getHardware().getIpAddress());
        hardwareInfo.setOcularVersion(serverStateResponse.getData().getHardware().getOcularVersion());
        hardwareInfo.setUptime(serverStateResponse.getData().getHardware().getUptime());
        hardwareInfo.setLoadAverage(serverStateResponse.getData().getHardware().getLoadAverage());

        List<ServiceState> services = new ArrayList<>();
        for (ServiceStateDto dto : serverStateResponse.getData().getServices()) {
            ServiceState serviceState = new ServiceState();
            serviceState.setName(dto.getName());
            serviceState.setStatus(dto.getStatus());
            serviceState.setErrorCode(dto.getErrorCode());
            serviceState.setErrorMessage(dto.getErrorMessage());

            services.add(serviceState);
        }

        List<CameraState> cameraStates = new ArrayList<>();
        for (CameraStateDto stateDto : serverStateResponse.getData().getCameras()) {
            CameraState cameraState = new CameraState();
            cameraState.setCameraId(stateDto.getCameraId());
            cameraState.setStatus(stateDto.getStatus());
            cameraState.setErrorCode(stateDto.getErrorCode());
            cameraState.setErrorMessage(stateDto.getErrorMessage());

            cameraStates.add(cameraState);
        }

        return new ServerState(hardwareInfo, services, cameraStates);
    }

    @Override
    public void resetServer(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        ResetServerRequest resetRequest = new ResetServerRequest(UUID.randomUUID(), serverName);

        queueService.send((OcularConnection) connection, resetRequest, BaseResponse.class);
    }

    public void addCamera(@NonNull Connection connection, Camera camera, @NonNull String serverName) throws Exception {

        CameraDto cameraDto = new CameraDto();
        cameraDto.setName(camera.getName());
        cameraDto.setStorageDays(camera.getStorageDays());
        cameraDto.setAnalysisType(camera.getAnalysisType());
        cameraDto.setPrimaryAddress(camera.getPrimaryAddress());
        cameraDto.setSecondaryAddress(camera.getSecondaryAddress());
        cameraDto.setScheduleId(camera.getScheduleId());
        cameraDto.setStorageId(camera.getStorageId());

        AddCameraRequest addCameraRequest = new AddCameraRequest(UUID.randomUUID(), serverName);
        addCameraRequest.setData(cameraDto);

        queueService.send((OcularConnection) connection, addCameraRequest, BaseResponse.class);
    }

    @Override
    public void removeCamera(@NonNull Connection connection, @NonNull String serverName, String cameraId) throws Exception {

        DeleteCameraRrequest deleteCameraRrequest = new DeleteCameraRrequest(UUID.randomUUID(), serverName);
        deleteCameraRrequest.setCameraId(cameraId);
        queueService.send((OcularConnection) connection, deleteCameraRrequest, DeleteCameraResponse.class);
    }

    @Override
    public void updateCamera(@NonNull Connection connection, @NonNull String serverName, Camera camera) throws Exception {

        UpdateCameraRequest request = new UpdateCameraRequest(UUID.randomUUID(), serverName);
        request.setCameraId(camera.getCameraId());

        CameraDto dto = new CameraDto();
        dto.setCameraId(camera.getCameraId());
        dto.setName(camera.getName());
        dto.setPrimaryAddress(camera.getPrimaryAddress());
        dto.setSecondaryAddress(camera.getSecondaryAddress());
        dto.setStatus(camera.getStatus());
        dto.setStreamAddress(camera.getStreamAddress());
        dto.setIsRecording(camera.isEnabled());
        dto.setStorageDays(camera.getStorageDays());
        dto.setAnalysisType(camera.getAnalysisType());
        dto.setStorageId(camera.getStorageId());

        dto.setScheduleId(camera.getScheduleId());

        request.setCamera(dto);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public List<Camera> getCameraList(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        CameraListRequest baseRequest = new CameraListRequest(UUID.randomUUID(), serverName);
        CameraListResponse cameraListResponse = queueService.send((OcularConnection) connection, baseRequest, CameraListResponse.class);
        List<Camera> cameras = new ArrayList<>();
        for (CameraDto dto : cameraListResponse.getData()) {
            Camera camera = convertCamera(dto);
            cameras.add(camera);
        }

        return cameras;
    }

    @Override
    public void setRecording(@NonNull Connection connection, @NonNull String serverName, Camera camera, boolean isRecording) throws Exception {

        RecordingRequest recordingRequest = new RecordingRequest(UUID.randomUUID(), serverName);
        recordingRequest.setCameraId(camera.getCameraId());
        recordingRequest.setIsRecording(isRecording);
        queueService.send((OcularConnection) connection, recordingRequest, BaseResponse.class);

    }

    @Override
    public void ptzControl(@NonNull Connection connection, @NonNull String serverName, String cameraId, int vertical, int horizontal, int zoom) throws Exception {

        if (vertical != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, vertical);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), serverName);
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);

        }
        if (horizontal != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, horizontal);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), serverName);
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);

        }
        if (zoom != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, zoom);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), serverName);
            request.setPtzControl(ptzControlDto);
            BaseResponse response = queueService.send((OcularConnection) connection, request, BaseResponse.class);

        }
    }

    @Override
    public List<Storage> getStorageList(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        StorageListRequest storageRequest = new StorageListRequest(UUID.randomUUID(), serverName);
        StorageListResponse storageListResponse = queueService.send((OcularConnection) connection, storageRequest, StorageListResponse.class);

        List<Storage> storages = new ArrayList<>();
        for (StorageDto dto : storageListResponse.getData()) {
            Storage storage = convertStorage(dto);

            storages.add(storage);
        }

        return storages;
    }

    @Override
    public void addStorage(@NonNull Connection connection, @NonNull String serverName, String storageName, String storagePath) throws Exception {

        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest addStorageRequest = new StorageRequest(UUID.randomUUID(), serverName);
        addStorageRequest.setStorageDto(storageDto);

        queueService.send((OcularConnection) connection, addStorageRequest, BaseResponse.class);
    }

    @Override
    public void updateStorage(@NonNull Connection connection, @NonNull String serverName, String storageId, String storageName, String storagePath) throws Exception {

        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), serverName);
        storageRequest.setStorageId(storageId);
        storageRequest.setStorageDto(storageDto);

        queueService.send((OcularConnection) connection, storageRequest, BaseResponse.class);
    }

    @Override
    public void deleteStorage(@NonNull Connection connection, @NonNull String serverName, String storageId, String storageName, String storagePath) throws Exception {

        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), serverName);
        storageRequest.setStorageId(storageId);

        queueService.send((OcularConnection) connection, storageRequest, BaseResponse.class);
    }

    @Override
    public void addSchedule(@NonNull Connection connection, @NonNull String serverName, List<Integer> weekDays) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("weekdays");
        dto.setWeekDays(weekDays);

        AddScheduleRequest request = new AddScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void addSchedule(@NonNull Connection connection, @NonNull String serverName, int startTimestamp, int stopTimestamp) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("timestamp");
        dto.setStartTimestamp(startTimestamp);
        dto.setStopTimestamp(stopTimestamp);

        AddScheduleRequest request = new AddScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void addSchedule(@NonNull Connection connection, @NonNull String serverName, String startTime, String stopTime) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("time_period");
        dto.setStartTime(startTime);
        dto.setStartTime(stopTime);

        AddScheduleRequest request = new AddScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void updateSchedule(@NonNull Connection connection, @NonNull String serverName, int scheduleId, List<Integer> weekDays) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("weekdays");
        dto.setWeekDays(weekDays);

        UpdateScheduleRequest request = new UpdateScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);
        request.setScheduleId(scheduleId);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void updateSchedule(@NonNull Connection connection, @NonNull String serverName, int scheduleId, int startTimestamp, int stopTimestamp) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("timestamp");
        dto.setStartTimestamp(startTimestamp);
        dto.setStartTimestamp(stopTimestamp);

        UpdateScheduleRequest request = new UpdateScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);
        request.setScheduleId(scheduleId);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void updateSchedule(@NonNull Connection connection, @NonNull String serverName, int scheduleId, String startTime, String stopTime) throws Exception {

        ScheduleDto dto = new ScheduleDto();
        dto.setType("time_period");
        dto.setStartTime(startTime);
        dto.setStartTime(stopTime);

        UpdateScheduleRequest request = new UpdateScheduleRequest(UUID.randomUUID(), serverName);
        request.setScheduleDto(dto);
        request.setScheduleId(scheduleId);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public void deleteSchedule(@NonNull Connection connection, @NonNull String serverName, int scheduleId) throws Exception {

        AddScheduleRequest request = new AddScheduleRequest(UUID.randomUUID(), serverName);
//        request.setScheduleId(scheduleId);

        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public List<Record> getVideoArchive(@NonNull Connection connection, @NonNull String serverName, Integer startTimestamp, Integer stopTimestamp, List<String> cameras, Integer skip, Integer limit) throws Exception {

        ArchiveRecordRequest request = new ArchiveRecordRequest(UUID.randomUUID(), serverName);
        request.setCameraIds(cameras);
        request.setStartTimestamp(startTimestamp);
        request.setEndTimestamp(stopTimestamp);
        request.setLimit(limit);
        request.setSkip(skip);

        RecordListResponse recordListResponse = queueService.send((OcularConnection) connection, request, RecordListResponse.class);

        List<Record> records = new ArrayList<>();
        for (RecordDto dto : recordListResponse.getData()) {
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
    public Map<ScheduleTypeEnum,List<Schedule>> getScheduleList(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        ScheduleListRequest baseRequest = new ScheduleListRequest(UUID.randomUUID(), serverName);
        ScheduleListResponse scheduleListResponse = queueService.send((OcularConnection) connection, baseRequest, ScheduleListResponse.class);
        Map<ScheduleTypeEnum,List<Schedule>> scheduleMap = new HashMap<>();

       List<Schedule> weekdays = scheduleListResponse.getData().get("weekdays").stream().map(scheduleDto -> {
           Schedule schedule = new Schedule();
           schedule.setId(scheduleDto.getId());
           schedule.setType(ScheduleTypeEnum.WEEKDAYS);
           schedule.setWeekDays(scheduleDto.getWeekDays());
           return schedule;
       }).collect(Collectors.toList());
       scheduleMap.put(ScheduleTypeEnum.WEEKDAYS, weekdays);

        List<Schedule> timestamp = scheduleListResponse.getData().get("timestamp").stream().map(scheduleDto -> {
            Schedule schedule = new Schedule();
            schedule.setId(scheduleDto.getId());
            schedule.setType(ScheduleTypeEnum.TIMESTAMP);
            schedule.setStartTimestamp(scheduleDto.getStartTimestamp());
            schedule.setStopTimestamp(scheduleDto.getStopTimestamp());
            return schedule;
        }).collect(Collectors.toList());
        scheduleMap.put(ScheduleTypeEnum.TIMESTAMP, timestamp);

        List<Schedule> timePeriod = scheduleListResponse.getData().get("time_period").stream().map(scheduleDto -> {
            Schedule schedule = new Schedule();
            schedule.setId(scheduleDto.getId());
            schedule.setType(ScheduleTypeEnum.TIMEPERIOD);
            schedule.setStartTime(scheduleDto.getStartTime());
            schedule.setStopTime(scheduleDto.getStopTime());
            return schedule;
        }).collect(Collectors.toList());
        scheduleMap.put(ScheduleTypeEnum.TIMEPERIOD, timePeriod);

        return scheduleMap;
    }

    @Override
    public List<Organization> exportConfig(@NonNull Connection connection, @NonNull String serverName) throws Exception {

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), serverName);
        ExportConfigResponse organizationListDto = queueService.send((OcularConnection) connection, baseRequest, ExportConfigResponse.class);
        List<Organization> organizations = new ArrayList<>();
        for (OrganizationDto dto : organizationListDto.getData()) {
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
    public void importConfig(@NonNull Connection connection, @NonNull String serverName, List<Organization> organizations) throws Exception {
        ConfigImportRequest request = new ConfigImportRequest(UUID.randomUUID(), serverName);
        request.setOrganizations(organizations);
        queueService.send((OcularConnection) connection, request, BaseResponse.class);
    }

    @Override
    public EventHandler setEventHandler(Connection connection, EventHandler eventHandler) {
        return ((OcularConnection)connection).getEventHandler().getAndSet(eventHandler);
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
//        schedule.setType(scheduleDto.getType());
        schedule.setWeekDays(scheduleDto.getWeekDays());
        schedule.setStartTime(scheduleDto.getStartTime());
        schedule.setStopTime(scheduleDto.getStopTime());
        schedule.setStartTimestamp(scheduleDto.getStartTimestamp());
        schedule.setStopTimestamp(scheduleDto.getStopTimestamp());

        return schedule;
    }
}
