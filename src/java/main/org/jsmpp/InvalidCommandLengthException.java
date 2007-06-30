package org.jsmpp;

/**
 * This exception is thrown if command_length of PDU is not acceptable.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class InvalidCommandLengthException extends PDUException {
    private static final long serialVersionUID = -3097470067910064841L;

    /**
     * Default constructor.
     */
    public InvalidCommandLengthException() {
        super();
    }

    /**
     * Construct with specified message and cause.
     * 
     * @param message is the detail message.
     * @param cause is the parent cause.
     */
    public InvalidCommandLengthException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public InvalidCommandLengthException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the parent cause.
     */
    public InvalidCommandLengthException(Throwable cause) {
        super(cause);
    }

}
