package org.jsmpp.util;

/**
 * This exception is throw if there is an invalid format on delivery receipt
 * content.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class InvalidDeliveryReceiptException extends Exception {
    private static final long serialVersionUID = -3946145794520014988L;

    /**
     * Default constructor.
     */
    public InvalidDeliveryReceiptException() {
        super();
    }

    /**
     * Construct with specified message and cause.
     * 
     * @param message is the detail message.
     * @param cause is the cause.
     */
    public InvalidDeliveryReceiptException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public InvalidDeliveryReceiptException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the cause.
     */
    public InvalidDeliveryReceiptException(Throwable cause) {
        super(cause);
    }

}
