package ru.ddg.stalt.ocular.lib.impl.contracts.events;

/**
 * Событие камер
 */
public interface OcularEvent {

    /**
     * @return Идентификатор события
     */
    Integer getEventId();

    /**
     * @return Тип события
     */
    Integer getEventType();

    /**
     * @return Время начала события
     */
    Integer getEventStartTimestamp();

    /**
     * @return Время окончания события
     */
    Integer getEventEndTimestamp();

    /**
     * @return Уверенность в событии (в процентах)
     */
    Integer getConfidence();

    /**
     * @return Реакця события.
     */
    Integer getReaction();
}
