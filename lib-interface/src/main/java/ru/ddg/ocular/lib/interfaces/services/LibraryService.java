package ru.ddg.ocular.lib.interfaces.services;

import ru.ddg.ocular.lib.interfaces.facades.model.Camera;
import ru.ddg.ocular.lib.interfaces.facades.contracts.ServerState;
import ru.ddg.ocular.lib.interfaces.facades.model.Connection;

import java.util.List;

public interface LibraryService {

    /**
     * Подключение к очереди
     * @param address адрес сервера
     * @param port порт
     * @return объект соединения
     * @throws Exception ошибка подключения
     */
    Connection connect(String address, int port) throws Exception;

    /**
     * Отключение от очереди
     * @param connection объект соединения
     * @throws Exception ошибка отключения
     */
    void disconnect(Connection connection) throws Exception;

    /**
     * Возвращает информацию о сервере
     * @param serverName имя сервера
     * @param connection объект соединения
     * @return информация о сервере
     * @see ServerState;
     */
    ServerState getServerState(Connection connection, String serverName);

    /**
     * Отправляет команду 'reset' на сервер ocular
     * @param serverName имя сервера
     * @param connection объект соединения
     * @throws Exception
     */
    void resetServer(Connection connection, String serverName) throws Exception;

    /**
     * Добавляет новую камеру
     * @param camera камера
     * @param connection объект соединения
     * @throws Exception ошибка добавления камеры
     */
    void addCamera(Connection connection, Camera camera) throws Exception;

    /**
     * Удаляет существующую камеру
     * @param cameraId идентификатор камеры
     * @param connection объект соединения
     * @throws Exception ошибка удаления камеры
     */
    void removeCamera(Connection connection, long cameraId) throws Exception;

    /**
     * Возвращает список камер
     * @return список камер List<Camera>
     * @param connection объект соединения
     * @param serverName имя сервера
     * @see Camera
     */
    List<Camera> getCameraList(Connection connection, String serverName);

    /**
     * Переключает режим записи камеры.
     * @param camera Камера
     * @param connection объект соединения
     * @param isRecording включена true или выключена false
     * @param serverName имя сервера
     * @throws Exception ошибка переключения режима записи
     */
    void setRecording(Connection connection, String serverName, Camera camera, boolean isRecording) throws Exception;

    /**
     * Управление ptz камерой
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param vertical движение в вертикальном направлении
     * @param horizontal движение в горизонтальном направлении
     * @param zoom приближение
     */
    void ptzControl(Connection connection, String serverName, int vertical, int horizontal, int zoom);
}
