package org.jsmpp.extra;

/**
 * Throw if the the response never come for specified timeout.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class ResponseTimeoutException extends Exception {
    private static final long serialVersionUID = 2091678783085990727L;

    /**
     * Default constructor.
     */
    public ResponseTimeoutException() {
        super();
    }

    /**
     * Construct with specified message and cause.
     * 
     * @param message is the message.
     * @param cause is the cause.
     */
    public ResponseTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public ResponseTimeoutException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the cause.
     */
    public ResponseTimeoutException(Throwable cause) {
        super(cause);
    }

}
