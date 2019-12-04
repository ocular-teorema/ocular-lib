package ru.ddg.stalt.ocular.lib.impl.services.impl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ddg.stalt.ocular.lib.facades.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.facades.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.facades.model.*;
import ru.ddg.stalt.ocular.lib.impl.contracts.CameraDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.AddCameraRequestDto;
import ru.ddg.stalt.ocular.lib.impl.contracts.requests.RequestDto;
import ru.ddg.stalt.ocular.lib.impl.services.QueueService;
import ru.ddg.stalt.ocular.lib.services.OcularService;

import java.util.List;
import java.util.UUID;

public class OcularServiceImpl implements OcularService {
    private final static String QUEUE_NAME = "OCULAR";

    @Autowired
    QueueService queueService;

    @Override
    public Connection connect(String address, int port) throws Exception {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(address);
        connectionFactory.setPort(port);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        return new QueueConnection(connection,channel);

    }

    @Override
    public void disconnect(Connection connection) throws Exception {
        QueueConnection queueConnection = (QueueConnection)connection;
        queueConnection.getChannel().close();
        queueConnection.getConnection().close();

    }

    @Override
    public ServerState getServerState(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException {
        checkConnection(connection);
        checkServerName(serverName);

        if (serverName == null || serverName.isEmpty()) {
            throw new IncorrectServerNameException("Wrong server name");
        }
        StringBuilder stringBuilder = new StringBuilder("ocular/");
        stringBuilder.append(serverName);
        stringBuilder.append("/status/request");
        RequestDto requestDto = new RequestDto(UUID.randomUUID());

        queueService.sendCommand(stringBuilder.toString(),requestDto);

        return null;
    }

    @Override
    public void resetServer(Connection connection, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        StringBuilder stringBuilder = new StringBuilder("ocular/");
        stringBuilder.append(serverName);
        stringBuilder.append("/reset/request");
        RequestDto requestDto = new RequestDto(UUID.randomUUID());

        queueService.sendCommand(stringBuilder.toString(),requestDto);
    }

    public void addCamera(Connection connection, Camera camera, String serverName) throws Exception {
        checkConnection(connection);
        checkServerName(serverName);
        StringBuilder stringBuilder = new StringBuilder("ocular/");
        stringBuilder.append(serverName);
        stringBuilder.append("/cameras/add/request");

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

        AddCameraRequestDto addCameraRequest = new AddCameraRequestDto();
        addCameraRequest.setCamera(cameraDto);
        addCameraRequest.setRequestUid(UUID.randomUUID());

        queueService.sendCommand(stringBuilder.toString(),addCameraRequest);

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
