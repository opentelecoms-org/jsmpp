package org.jsmpp;

/**
 * This exception is thrown when there is a failure corresponds to PDU processing.
 * 
 * @author uudashr
 * @version 1.0
 *
 */
public class PDUException extends Exception {

	private static final long serialVersionUID = -4168434058788171394L;

	/**
	 * Default constructor.
	 */
	public PDUException() {
		super("PDUException found");
	}

	/**
     * Construct with specified message and cause.
     * 
	 * @param message is the detail message.
	 * @param cause is the parent cause.
	 */
	public PDUException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
     * Construct with specified message.
     * 
	 * @param message is the detail message.
	 */
	public PDUException(String message) {
		super(message);
	}

	/**
     * Construct with specified cause.
     * 
	 * @param cause is the parent cause.
	 */
	public PDUException(Throwable cause) {
		super(cause);
	}
}
