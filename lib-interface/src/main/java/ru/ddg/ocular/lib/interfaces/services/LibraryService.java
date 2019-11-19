package ru.ddg.ocular.lib.interfaces.services;

import ru.ddg.ocular.lib.interfaces.facades.model.Camera;
import ru.ddg.ocular.lib.interfaces.facades.contracts.ServerState;
import ru.ddg.ocular.lib.interfaces.facades.model.Connection;

import java.util.List;

public interface LibraryService {

    /**
     * Возвращает информацию о сервере
     * @param serverName имя сервера
     * @return информация о сервере
     * @see ServerState;
     */
    ServerState getServerState(String serverName);

    /**
     * Отправляет команду 'reset' на сервер ocular
     * @param serverName имя сервера
     * @throws Exception
     */
    void resetServer(String serverName) throws Exception;

    /**
     * Подключение к очереди
     * @param address адрес сервера
     * @param port порт
     * @param serverName имя сервера
     * @return объект соединения
     * @throws Exception ошибка подключения
     */
    Connection connect(String address, int port, String serverName) throws Exception;

    /**
     * Отключение от очереди
     * @throws Exception ошибка отключения
     */
    void disconnect(String serverName) throws Exception;

    /**
     * Добавляет новую камеру
     * @param camera камера
     * @throws Exception ошибка добавления камеры
     */
    void addCamera(Camera camera) throws Exception;

    /**
     * Удаляет существующую камеру
     * @param cameraId идентификатор камеры
     * @param serverName имя сервера
     * @throws Exception ошибка удаления камеры
     */
    void removeCamera(long cameraId, String serverName) throws Exception;

    /**
     * Возвращает список камер
     * @return список камер List<Camera>
     * @param serverName имя сервера
     * @see Camera
     */
    List<Camera> getCameraList(String serverName);

    /**
     * Переключает режим записи камеры.
     * @param camera Камера
     * @param serverName имя сервера
     * @param isRecording включена true или выключена false
     * @throws Exception ошибка переключения режима записи
     */
    void setRecording(Camera camera, String serverName, boolean isRecording) throws Exception;

    /**
     * Управление ptz камерой
     * @param vertical движение в вертикальном направлении
     * @param horizontal движение в горизонтальном направлении
     * @param zoom приближение
     */
    void ptzControl(int vertical, int horizontal, int zoom);
}
