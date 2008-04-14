package org.jsmpp;

/**
 * This exception is thrown when receive unexpected request.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public class InvalidRequestException extends Exception {
	private static final long serialVersionUID = -2275241874120654607L;

	/**
	 * Default constructor.
	 */
	public InvalidRequestException() {
		super();
	}

	/**
     * Construct with specified message and cause.
     * 
	 * @param message is the detail message.
	 * @param cause is the parent cause.
	 */
	public InvalidRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * Construct with specified message.
     * 
	 * @param message is the detail message.
	 */
	public InvalidRequestException(String message) {
		super(message);
	}

	/**
     * Construct with specified cause.
     * 
	 * @param cause is the parent cause.
	 */
	public InvalidRequestException(Throwable cause) {
		super(cause);
	}
	
}
