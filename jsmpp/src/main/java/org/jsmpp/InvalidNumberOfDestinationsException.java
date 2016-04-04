package org.jsmpp;


/**
 * This exception should be thrown if there is an invalid number of destination
 * address used by submit_multi.
 * 
 * @author uudashr
 * 
 */
public class InvalidNumberOfDestinationsException extends PDUException {
    private static final long serialVersionUID = -1515128166927438310L;
    private final int actualLength;
    
    /**
     * Default constructor.
     * 
     * @param message is the message.
     * @param actualLength the actual length.
     */
    public InvalidNumberOfDestinationsException(String message, int actualLength) {
        super(message);
        this.actualLength = actualLength;
    }
    
    public int getActualLength() {
        return actualLength;
    }
    
}
