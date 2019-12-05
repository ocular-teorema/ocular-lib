package ru.ddg.stalt.ocular.lib.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Данный объект содержит информацию о камере:
 * Идентификатор, имя, основной адрес (видеопоток с максимальным разрешением),
 * дополнительный адрес (опционально, видеопоток с меньшим разрешением), идентификатор хранилища,
 * идентификатор расписания, тип анализа (1 - без анализа, 2 - только движение, 3 - полный анализ), статус
 * время хранения видео (дней).
 */
@Getter
@Setter
public class Camera {

    private String cameraId;

    private String name;

    private String primaryAddress;

    private String secondaryAddress;

    private Integer storageId;

    private Integer scheduleId;

    private Integer analysisType;

    private CameraStatusEnum status;

    private Integer storageDays;
}
