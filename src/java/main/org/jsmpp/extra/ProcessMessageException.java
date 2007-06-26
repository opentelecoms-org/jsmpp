package org.jsmpp.extra;

/**
 * Thrown if there is an error occur when processing message.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class ProcessMessageException extends Exception {
    private static final long serialVersionUID = -3633100382131187197L;

    private int errorCode;

    /**
     * Construct with specified message and error code.
     * 
     * @param message is the detail message.
     * @param errorCode is the error code (or known as command_status).
     */
    public ProcessMessageException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Construct with specified message, error code, and cause.
     * 
     * @param message is the detail message.
     * @param errorCode is the error code (or known as command_status).
     * @param cause is the parent cause.
     */
    public ProcessMessageException(String message, int errorCode,
            Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * Get the error code (or known as command_status).
     * 
     * @return the error code.
     */
    public int getErrorCode() {
        return errorCode;
    }
}
