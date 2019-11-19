package ru.ddg.ocular.lib.interfaces.services;

import ru.ddg.ocular.lib.interfaces.facades.model.Camera;
import ru.ddg.ocular.lib.interfaces.facades.contracts.ServerState;
import ru.ddg.ocular.lib.interfaces.facades.model.Connection;

import java.util.List;

public interface LibraryService {

    /**
     * Возвращает информацию о сервере
     * @param address адрес сервера
     * @param port порт
     * @return информация о сервере
     * @see ServerState;
     */
    ServerState getServerState(String address, int port);

    /**
     * Отправляет команду 'reset' на сервер ocular
     * @throws Exception
     */
    void resetServer() throws Exception;

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
     * @throws Exception ошибка отключения
     */
    void disconnect() throws Exception;

    /**
     * Добавляет новую камеру
     * @param camera камера
     * @throws Exception ошибка добавления камеры
     */
    void addCamera(Camera camera) throws Exception;

    /**
     * Удаляет существующую камеру
     * @param cameraId идентификатор камеры
     * @param address адрес сервера
     * @param port порт
     * @throws Exception ошибка удаления камеры
     */
    void removeCamera(long cameraId, String address, int port) throws Exception;

    /**
     * Возвращает список камер
     * @return список камер List<Camera>
     * @see Camera
     */
    List<Camera> getCameraList();

    /**
     * Переключает режим записи камеры.
     * @param camera Камера
     * @param isRecording включена true или выключена false
     * @throws Exception ошибка переключения режима записи
     */
    void setRecording(Camera camera, boolean isRecording) throws Exception;
}
