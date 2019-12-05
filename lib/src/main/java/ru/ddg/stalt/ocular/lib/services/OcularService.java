package ru.ddg.stalt.ocular.lib.services;

import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.model.*;
import ru.ddg.stalt.ocular.lib.model.Connection;

import java.util.List;

public interface OcularService {

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
    ServerState getServerState(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException;

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

    /**
     * Возвращает список хранилищ заданного сервера
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список хранилищ List<Storage>
     */
    List<Storage> getStorageList(Connection connection, String serverName);

    /**
     * Добавляет новое хранилище с заданным именем по заданному пути
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param storageName имя хранилища
     * @param storagePath адрес пути
     * @throws Exception ошибка добавления хранилища
     */
    void addStorage(Connection connection, String serverName, String storageName, String storagePath) throws Exception;

    /**
     * Добавляет новое расписание включения камеры, основываясь на днях недели
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param weekDays массив дней недели (1-7), в которые камера включена
     * @throws Exception ошибка добавленния расписания
     */
    void addSchedule(Connection connection, String serverName, List<Integer> weekDays) throws Exception;

    /**
     * Добавляет новое расписание, основываясь на времени Unix (Unix timestamp)
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param startTimestamp начало планирования
     * @param stopTimestamp завершение
     * @throws Exception ошибка добавленния расписания
     */
    void addSchedule(Connection connection, String serverName, int startTimestamp, int stopTimestamp) throws Exception;

    /**
     * Добавляет новое расписание включения камеры, в заданный интервал времени.
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param startTime начало планирования (час, минута и секунда HH-MM-SS)
     * @param stopTime завершение (час, минута и секунда HH-MM-SS)
     * @throws Exception ошибка добавленния расписания
     */
    void addSchedule(Connection connection, String serverName, String startTime, String stopTime) throws Exception;

    /**
     * Возвращает записи из архива
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param startTimestamp время начала поиска
     * @param stopTimestamp время окончания поиска
     * @param cameras массив идентификаторов камер
     * @param skip пропуск первых N элементов запроса (N >= 0)
     * @param limit предел результатов запроса
     * @return массив записей
     */
    List<Record> getVideoArchive(
            Connection connection,
            String serverName,
            Integer startTimestamp,
            Integer stopTimestamp,
            List<String> cameras,
            Integer skip,
            Integer limit);

    /**
     * Возвращает список расписаний
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список расписаний List<Schedule>
     */
    List<Schedule> getScheduleList(Connection connection, String serverName);

    /**
     * Возвращает данные конфигурации
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список организаций
     */
    List<Organization> exportConfig(Connection connection, String serverName);

    /**
     * Импортирует данные конфигурации
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param organizations список организаций
     * @throws Exception ошибка импорта
     */
    void importConfig(Connection connection, String serverName, List<Organization> organizations) throws Exception;

}
