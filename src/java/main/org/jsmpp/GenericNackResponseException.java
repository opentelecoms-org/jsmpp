package org.jsmpp;

/**
 * @author uudashr
 *
 */
public class GenericNackResponseException extends InvalidResponseException {
    private static final long serialVersionUID = -5938563802952633189L;
    private final int commandStatus;
    
    public GenericNackResponseException(String message, int commandStatus) {
        super(message);
        this.commandStatus = commandStatus;
    }
    
    public int getCommandStatus() {
        return commandStatus;
    }
}
