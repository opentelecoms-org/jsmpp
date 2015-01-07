package org.jsmpp.extra;

import org.jsmpp.util.IntUtil;

/**
 * This exception is thrown if we receive an negative response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class NegativeResponseException extends Exception {
    private static final long serialVersionUID = 7198456791204091251L;
    private int commandStatus;

    /**
     * Construct with specified command_status.
     * 
     * @param commandStatus is the command_status.
     */
    public NegativeResponseException(int commandStatus) {
        super("Negative response " + IntUtil.toHexString(commandStatus)
                + " found");
        this.commandStatus = commandStatus;
    }

    /**
     * Get the command_status.
     * 
     * @return is the command_status.
     */
    public int getCommandStatus() {
        return commandStatus;
    }
}
