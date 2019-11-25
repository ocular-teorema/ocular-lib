package ru.ddg.stalt.ocular.lib.facades.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Данный объект содержит информацию о камере:
 * Идентификатор, имя, основной адрес (видеопоток с максимальным разрешением),
 * дополнительный адрес (опционально, видеопоток с меньшим разрешением), идентификатор хранилища,
 * идентификатор расписания, тип анализа (1 - без анализа, 2 - только движение, 3 - полный анализ), статус,
 * статус записи (true - включена, false - выключена)
 * время хранения видео (дней), адрес архива.
 */
@Getter
@AllArgsConstructor
public class Camera {

    String cameraId;

    String name;

    String primaryAddress;

    String secondaryAddress;

    Integer storageId;

    Integer scheduleId;

    Integer analysisType;

    String status;

    Boolean isRecording;

    Integer storageDays;

    String archivePath;
}
