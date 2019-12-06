package ru.ddg.stalt.ocular.lib.impl.services;

import org.springframework.beans.factory.annotation.Autowired;
import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.impl.contracts.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.*;
import ru.ddg.stalt.ocular.lib.impl.model.OcularConnection;
import ru.ddg.stalt.ocular.lib.model.*;
import ru.ddg.stalt.ocular.lib.services.OcularService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OcularServiceImpl implements OcularService {
    @Autowired
    private QueueService queueService;

    @Override
    public Connection connect(String address, int port, String username, String password, long responseTimeout) throws IOException, TimeoutException {
        // TODO
         com.rabbitmq.client.Connection connection = queueService.createConnection(address, port, username, password);
         return new OcularConnection(connection, responseTimeout);
    }

    @Override
    public void disconnect(Connection connection) throws IOException {
        connection.close();
    }

    @Override
    public ServerState getServerState(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/status";

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(),server,"status");

        BaseResponse response = queueService.send((OcularConnection) connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void resetServer(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/reset";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "reset_request");

        queueService.send(connection,baseRequest);
    }

    public void addCamera(Connection connection, Camera camera, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
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

        queueService.send(connection, addCameraRequest);

    }

    @Override
    public void removeCamera(Connection connection, String serverName, String cameraId) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/delete";

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "cameras_delete");
        queueService.send(connection, baseRequest);
    }

    @Override
    public void updateCamera(Connection connection, String serverName, Camera camera) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
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

        queueService.send(connection, request);
    }

    @Override
    public List<Camera> getCameraList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/list";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "cameras_list");
        BaseResponse response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void setRecording(Connection connection, String serverName, Camera camera, boolean isRecording) throws Exception {

    }

    @Override
    public void ptzControl(Connection connection, String serverName, String cameraId, int vertical, int horizontal, int zoom) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/cameras/ptz_control";

        if (vertical != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, vertical);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_move_vertical");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
        if (horizontal != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, horizontal);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_move_horizontal");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
        if (zoom != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, zoom);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server,"cameras_ptz_zoom");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
    }

    @Override
    public List<Storage> getStorageList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/list";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "storage_list");
        BaseResponse response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void addStorage(Connection connection, String serverName, String storageName, String storagePath) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/add";
        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest addStorageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_add");
        addStorageRequest.setStorageDto(storageDto);
        queueService.send(connection, addStorageRequest);
    }

    @Override
    public void updateStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/update";
        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_update");
        storageRequest.setStorageId(storageId);
        storageRequest.setStorageDto(storageDto);

        queueService.send(connection, storageRequest);
    }

    @Override
    public void deleteStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/delete";
        StorageRequest storageRequest = new StorageRequest(UUID.randomUUID(), server, "storage_delete_request");
        storageRequest.setStorageId(storageId);

        queueService.send(connection, storageRequest);
    }

    @Override
    public void addSchedule(Connection connection, String serverName, List<Integer> weekDays) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/add";
        ScheduleDto dto = new ScheduleDto();
        dto.setType("weekdays");
        dto.setWeekDays(weekDays);

        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedule_add");
        request.setScheduleDto(dto);

        queueService.send(connection, request);
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

        queueService.send(connection, request);
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

        queueService.send(connection, request);
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

        queueService.send(connection, request);
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

        queueService.send(connection, request);
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

        queueService.send(connection, request);
    }

    @Override
    public void deleteSchedule(Connection connection, String serverName, int scheduleId) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/delete";
        ScheduleRequest request = new ScheduleRequest(UUID.randomUUID(), server, "schedules_delete");
        request.setScheduleId(scheduleId);

        queueService.send(connection, request);
    }

    @Override
    public List<Record> getVideoArchive(Connection connection, String serverName, Integer startTimestamp, Integer stopTimestamp, List<String> cameras, Integer skip, Integer limit) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/archive/archive/video/request";
        ArchiveRecordRequest request = new ArchiveRecordRequest(UUID.randomUUID(), server, "archive_video_request");
        request.setCameraIds(cameras);
        request.setStartTimestamp(startTimestamp);
        request.setEndTimestamp(stopTimestamp);
        request.setLimit(limit);
        request.setSkip(skip);

        BaseResponse response = queueService.send(connection, request);
        //TODO
        return null;
    }

    @Override
    public List<Schedule> getScheduleList(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/schedules/list/request";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "schedule_list_request_message");
        BaseResponse response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public List<Organization> exportConfig(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/config/export";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "config_export");
        BaseResponse response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void importConfig(Connection connection, String serverName, List<Organization> organizations) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/config/import";
        ConfigImportRequest request = new ConfigImportRequest(UUID.randomUUID(), server, "config_import");
        request.setOrganizations(organizations);
        queueService.send(connection, request);
    }

    private void checkConnection(Connection connection) throws WrongConnectionException {
        if (connection == null) {
            throw new WrongConnectionException("Wrong connection");
        }
    }

    private void checkServerName(String serverName) throws IncorrectServerNameException {
        if (serverName == null || serverName.isEmpty()) {
            throw new IncorrectServerNameException("Wrong server name");
        }
    }
}
