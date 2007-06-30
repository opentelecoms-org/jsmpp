package org.jsmpp.extra;

/**
 * This exception is thrown if the message queue has reach it maximum size.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class QueueMaxException extends QueueException {
    private static final long serialVersionUID = 5811663728783767496L;

    /**
     * Default constructor.
     */
    public QueueMaxException() {
        super();
    }

    /**
     * Construct with specified detail messsage and cause.
     * 
     * @param message is the detail message.
     * @param cause is the parent cause.
     */
    public QueueMaxException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public QueueMaxException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the parent cause.
     */
    public QueueMaxException(Throwable cause) {
        super(cause);
    }

}
