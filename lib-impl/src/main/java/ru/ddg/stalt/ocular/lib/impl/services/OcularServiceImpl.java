package ru.ddg.stalt.ocular.lib.impl.services;

import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.impl.contracts.PtzControlDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.Response;
import ru.ddg.stalt.ocular.lib.impl.contracts.StorageDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.AddStorageRequest;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.PtzControlRequest;
import ru.ddg.stalt.ocular.lib.model.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.AddCameraRequest;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.BaseRequest;
import ru.ddg.stalt.ocular.lib.services.OcularService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class OcularServiceImpl implements OcularService {
    @Autowired
    private QueueService queueService;

    @Override
    public Connection connect(String address, int port, String username, String password) throws IOException, TimeoutException {
        // TODO
        return queueService.createConnection(address, port, username, password);
    }

    @Override
    public void disconnect(Connection connection) throws IOException {
        connection.close();
    }

    @Override
    public ServerState getServerState(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/status/request";

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(),server,"status_request");

        Response response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void resetServer(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/reset/request/";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "reset_message");

        queueService.send(connection,baseRequest);
    }

    public void addCamera(Connection connection, Camera camera, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/cameras/add/request";

        CameraDto cameraDto = new CameraDto();
        cameraDto.setCameraId(camera.getCameraId());
        cameraDto.setName(camera.getName());
        cameraDto.setStorageDays(camera.getStorageDays());
        cameraDto.setAnalysisType(camera.getAnalysisType());
        cameraDto.setPrimaryAddress(camera.getPrimaryAddress());
        cameraDto.setSecondaryAddress(camera.getSecondaryAddress());
        cameraDto.setScheduleId(camera.getScheduleId());
        cameraDto.setStatus(camera.getStatus());
        cameraDto.setStorageId(camera.getStorageId());

        AddCameraRequest addCameraRequest = new AddCameraRequest(UUID.randomUUID(), server, "camera_add_request_message");
        addCameraRequest.setCamera(cameraDto);

        queueService.send(connection, addCameraRequest);

    }

    @Override
    public void removeCamera(Connection connection, String serverName, String cameraId) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        StringBuffer server = new StringBuffer(serverName)
                .append("/cameras/")
                .append(cameraId)
                .append("delete/request");

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server.toString(), "camera_delete_request_message");
        queueService.send(connection, baseRequest);
    }

    @Override
    public List<Camera> getCameraList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/cameras/list/request";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, "camera_list_request_message");
        Response response = queueService.send(connection, baseRequest);
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
        StringBuffer server = new StringBuffer(serverName)
                .append("/cameras/")
                .append(cameraId)
                .append("/ptz_control");

        if (vertical != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, vertical);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server.toString(),"camera_ptz_move_vertical");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
        if (horizontal != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, horizontal);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server.toString(),"camera_ptz_move_horizontal");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
        if (zoom != 0) {
            PtzControlDto ptzControlDto = new PtzControlDto(cameraId, zoom);
            PtzControlRequest request = new PtzControlRequest(UUID.randomUUID(), server.toString()," camera_ptz_zoom");
            request.setPtzControl(ptzControlDto);
            queueService.send(connection, request);
        }
    }

    @Override
    public List<Storage> getStorageList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/list/request";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server, " storage_list_request_message");
        Response response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void addStorage(Connection connection, String serverName, String storageName, String storagePath) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);

        String server = serverName + "/storages/add/request";
        StorageDto storageDto = new StorageDto();
        storageDto.setName(storageName);
        storageDto.setDefaultArchivePath(storagePath);
        AddStorageRequest addStorageRequest = new AddStorageRequest(UUID.randomUUID(), server, " storage_add_request_message");
        addStorageRequest.setStorageDto(storageDto);
        queueService.send(connection, addStorageRequest);
    }

    @Override
    public void addSchedule(Connection connection, String serverName, List<Integer> weekDays) throws Exception {

    }

    @Override
    public void addSchedule(Connection connection, String serverName, int startTimestamp, int stopTimestamp) throws Exception {

    }

    @Override
    public void addSchedule(Connection connection, String serverName, String startTime, String stopTime) throws Exception {

    }

    @Override
    public List<Record> getVideoArchive(Connection connection, String serverName, Integer startTimestamp, Integer stopTimestamp, List<String> cameras, Integer skip, Integer limit) {
        return null;
    }

    @Override
    public List<Schedule> getScheduleList(Connection connection, String serverName) {
        return null;
    }

    @Override
    public List<Organization> exportConfig(Connection connection, String serverName) {
        return null;
    }

    @Override
    public void importConfig(Connection connection, String serverName, List<Organization> organizations) throws Exception {

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
