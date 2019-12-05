package ru.ddg.stalt.ocular.lib.services;

import com.rabbitmq.client.Connection;
import ru.ddg.stalt.ocular.lib.exceptions.IncorrectServerNameException;
import ru.ddg.stalt.ocular.lib.exceptions.WrongConnectionException;
import ru.ddg.stalt.ocular.lib.model.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

public interface OcularService {

    /**
     * Подключение к очереди
     * @param address адрес сервера
     * @param port порт
     * @return объект соединения
     * @throws IOException ошибка ввода-вывода
     * @throws TimeoutException истекло время ожидания
     */
    Connection connect(String address, int port, String username, String password) throws IOException, TimeoutException;

    /**
     * Отключение от очереди
     * @param connection объект соединения
     * @throws IOException ошибка ввода-вывода
     */
    void disconnect(Connection connection) throws IOException;

    /**
     * Возвращает информацию о сервере
     * @param serverName имя сервера
     * @param connection объект соединения
     * @return информация о сервере
     * @see ServerState;
     */
    ServerState getServerState(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException, IOException, TimeoutException;

    /**
     * Отправляет команду 'reset' на сервер ocular
     * @param serverName имя сервера
     * @param connection объект соединения
     * @throws Exception
     */
    void resetServer(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

    /**
     * Добавляет новую камеру
     * @param camera камера
     * @param connection объект соединения
     * @throws Exception ошибка добавления камеры
     */
    void addCamera(Connection connection, Camera camera, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

    /**
     * Удаляет существующую камеру
     * @param cameraId идентификатор камеры
     * @param connection объект соединения
     * @throws Exception ошибка удаления камеры
     */
    void removeCamera(Connection connection, String serveName, String cameraId) throws Exception;

    /**
     * Обновляет существующую камеру
     * @param camera камера
     * @param connection объект соединения
     * @param serverName имя сервера
     * @throws Exception ошибка обновления камеры
     */
    void updateCamera(Connection connection, String serverName, Camera camera) throws Exception;

    /**
     * Возвращает список камер
     * @return список камер List<Camera>
     * @param connection объект соединения
     * @param serverName имя сервера
     * @see Camera
     */
    List<Camera> getCameraList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

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
    void ptzControl(Connection connection, String serverName, String cameraId, int vertical, int horizontal, int zoom) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

    /**
     * Возвращает список хранилищ заданного сервера
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список хранилищ List<Storage>
     */
    List<Storage> getStorageList(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

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
     * Обновляет хранилище
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param storageId идентификатор хранилища
     * @param storageName имя хранилища
     * @param storagePath адрес пути
     * @throws Exception ошибка обновления хранилища
     */
    void updateStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws Exception;

    /**
     * Удаляет хранилище
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param storageId идентификатор хранилища
     * @param storageName имя хранилища
     * @param storagePath адрес пути
     * @throws Exception ошибка удаления хранилища
     */
    void deleteStorage(Connection connection, String serverName, String storageId, String storageName, String storagePath) throws Exception;

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
     * Обновляет расписание включения камеры, основываясь на днях недели
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param weekDays массив дней недели (1-7), в которые камера включена
     * @throws Exception ошибка обновления расписания
     */
    void updateSchedule(Connection connection, String serverName, List<Integer> weekDays) throws Exception;

    /**
     * Оббновляет расписание, основываясь на времени Unix (Unix timestamp)
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param startTimestamp начало планирования
     * @param stopTimestamp завершение
     * @throws Exception ошибка обновления расписания
     */
    void updateSchedule(Connection connection, String serverName, int startTimestamp, int stopTimestamp) throws Exception;

    /**
     * Обновляет расписание включения камеры, в заданный интервал времени.
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param startTime начало планирования (час, минута и секунда HH-MM-SS)
     * @param stopTime завершение (час, минута и секунда HH-MM-SS)
     * @throws Exception ошибка обновления расписания
     */
    void updateSchedule(Connection connection, String serverName, String startTime, String stopTime) throws Exception;

    /**
     * Удаляет расписание.
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param scheduleId идентификатор расписания
     * @throws Exception ошибка удаления расписания
     */
    void deleteSchedule(Connection connection, String serverName, int scheduleId) throws Exception;

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
            Integer limit) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

    /**
     * Возвращает список расписаний
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список расписаний List<Schedule>
     */
    List<Schedule> getScheduleList(Connection connection, String serverName) throws IncorrectServerNameException, WrongConnectionException, IOException, TimeoutException;

    /**
     * Возвращает данные конфигурации
     * @param connection объект соединения
     * @param serverName имя сервера
     * @return список организаций
     */
    List<Organization> exportConfig(Connection connection, String serverName) throws WrongConnectionException, IncorrectServerNameException, IOException, TimeoutException;

    /**
     * Импортирует данные конфигурации
     * @param connection объект соединения
     * @param serverName имя сервера
     * @param organizations список организаций
     * @throws Exception ошибка импорта
     */
    void importConfig(Connection connection, String serverName, List<Organization> organizations) throws Exception;

}
