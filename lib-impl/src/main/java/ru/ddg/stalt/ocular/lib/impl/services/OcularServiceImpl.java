package ru.ddg.stalt.ocular.lib.impl.services;

import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.impl.contracts.Response;
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

        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(),server);

        Response response = queueService.send(connection, baseRequest);
        //TODO
        return null;
    }

    @Override
    public void resetServer(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException {
        checkConnection(connection);
        checkServerName(serverName);
        String server = serverName + "/reset/request/";
        BaseRequest baseRequest = new BaseRequest(UUID.randomUUID(), server);

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

        AddCameraRequest addCameraRequest = new AddCameraRequest(UUID.randomUUID(), server);
        addCameraRequest.setCamera(cameraDto);

        queueService.send(connection, addCameraRequest);

    }

    @Override
    public void removeCamera(Connection connection, long cameraId) throws Exception {

    }

    @Override
    public List<Camera> getCameraList(Connection connection, String serverName) {
        return null;
    }

    @Override
    public void setRecording(Connection connection, String serverName, Camera camera, boolean isRecording) throws Exception {

    }

    @Override
    public void ptzControl(Connection connection, String serverName, int vertical, int horizontal, int zoom) {

    }

    @Override
    public List<Storage> getStorageList(Connection connection, String serverName) {
        return null;
    }

    @Override
    public void addStorage(Connection connection, String serverName, String storageName, String storagePath) throws Exception {

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
