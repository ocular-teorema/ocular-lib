package ru.ddg.stalt.ocular.lib.facades.exceptions;

public class BaseException extends Exception {
    /**
     * Создает новое исключение с указанным сообщением. Причина не инициализирована, и может быть инициализирована с помощью вызова
     * {@link java.lang.Exception#initCause initCause}.
     *
     * @param message подробное сообщение. Сообщение сохранено для дальнейшего получения с помощью метода getMessage()
     * {@link java.lang.Exception#getMessage() getMessage()}
     *
     * @see {@link java.lang.Exception#Exception(String message) Exception(String message)}
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Создает новое исключение с указанным сообщением и причиной.
     * Обратите внимание, что подробное сообщение, связанное с причиной {@code cause} автоматически не включается в подробное сообщение этого исключения.
     *
     * @param message подробное сообщение. Сообщение сохраняется для дальнейшего получения с помощью метода getMessage()
     * {@link Exception#getMessage() getMessage()}
     *
     * @param cause причина (которая сохранена для дальнейшего получения с помощью метода {@link Exception#getCause() getCause()}.
     * (Значение null допустимо, и сигнализирует о том, что причина отсутствует или неизвестна.)
     *
     * @see {@link Exception#Exception(String message, Throwable cause) Exception(String message, Throwable cause)}
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Создает новое исключение с указанной причиной и детальным сообщением вида <tt>(cause==null ? null : cause.toString())</tt>
     * (Которое обычно содержит класс и детальное сообщение о причине <tt>cause</tt>)
     * Данный конструктор полезен для исключений, которые являются нечто большим, чем обертки для других исключений
     * (например, {@link java.security.PrivilegedActionException}).
     *
     * @param cause причина (которая сохранена для дальнейшего получения с помощью метода {@link Exception#getCause() getCause()}.
     * (Значение null допустимо, и сигнализирует о том, что причина отсутствует или неизвестна.)
     */
    public BaseException(Throwable cause) {
        super(cause);
    }
}
