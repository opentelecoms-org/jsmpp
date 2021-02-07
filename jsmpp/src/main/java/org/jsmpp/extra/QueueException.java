package org.jsmpp.extra;

/**
 * General queue exception.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class QueueException extends RuntimeException {
    private static final long serialVersionUID = -8946319349013591134L;

    /**
     * Default constructor.
     */
    public QueueException() {
        super();
    }

    /**
     * Construct with specified message and cause.
     * 
     * @param message is the detail message.
     * @param cause is the cause.
     */
    public QueueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public QueueException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the cause of an error.
     */
    public QueueException(Throwable cause) {
        super(cause);
    }

}
