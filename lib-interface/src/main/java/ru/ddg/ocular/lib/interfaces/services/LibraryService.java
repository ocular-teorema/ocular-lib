package ru.ddg.ocular.lib.interfaces.services;

import ru.ddg.ocular.lib.interfaces.facades.contracts.Camera;
import ru.ddg.ocular.lib.interfaces.facades.contracts.ServerState;

import java.util.List;

public interface LibraryService {

    /**
     * Возвращает информацию о сервере
     * @return информация о сервере
     * @see ServerState;
     */
    ServerState getServerState();

    /**
     * Отправляет команду 'reset' на сервер ocular
     * @return статус успех (true) или неудача (false)
     */
    boolean resetServer();

    /**
     * Подключение к очереди
     * @param address адрес
     * @param port порт
     * @return статус успех (true) или неудача (false)
     */
    boolean connect(String address, int port);

    /**
     * Отключение от очереди
     * @return статус успех (true) или неудача (false)
     */
    boolean disconnect();

    /**
     * Добавляет новую камеру
     * @param camera камера
     * @return статус успех (true) или неудача (false)
     */
    boolean addCamera(Camera camera);

    /**
     * Удаляет существующую камеру
     * @param camera камера
     * @return статус успех (true) или неудача (false)
     */
    boolean removeCamera(Camera camera);

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
     * @return режим успешно изменен (true) или неудача (false)
     */
    boolean setRecording(Camera camera, boolean isRecording);
}
