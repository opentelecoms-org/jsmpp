package org.jsmpp;

/**
 * This exception thrown if we receive unexpected response (such as invalid PDU
 * parameter).
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class InvalidResponseException extends Exception {
    private static final long serialVersionUID = -7149393390064407238L;

    /**
     * Construct with specified message and cause.
     * 
	 * @param message is the detail message.
	 * @param cause is the parent cause.
	 */
	public InvalidResponseException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * Construct with specified message.
     * 
	 * @param message is the detail message.
	 */
	public InvalidResponseException(String message) {
		super(message);
	}
}
