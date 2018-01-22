package org.jsmpp.extra;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
    static {
        HashMap<Integer, String> mapping = new HashMap<Integer, String>();
        mapping.put(1, "Message Length is invalid");
        mapping.put(2, "Command Length is invalid");
        mapping.put(3, "Invalid Command ID");
        mapping.put(4, "Incorrect BIND Status for given command");
        mapping.put(5, "ESME Already in Bound State");
        mapping.put(6, "Invalid Priority Flag");
        mapping.put(7, "Invalid Registered Delivery Flag");
        mapping.put(8, "System Error");
        mapping.put(10, "Invalid Source Address");
        mapping.put(11, "Invalid Destination Address");
        mapping.put(12, "Message ID is invalid");
        mapping.put(13, "Bind Failed");
        mapping.put(14, "Invalid Password");
        mapping.put(15, "Invalid System ID");
        commandStatusToDescription = Collections.unmodifiableMap(mapping);
    }
    private static final Map<Integer, String> commandStatusToDescription;
    private static final long serialVersionUID = 7198456791204091251L;
    private final int commandStatus;

    /**
     * Construct with specified command_status.
     * 
     * @param commandStatus is the command_status.
     */
    public NegativeResponseException(int commandStatus) {
        super(createMessageForCommandStatus(commandStatus));
        this.commandStatus = commandStatus;
    }

    private static String createMessageForCommandStatus(int commandStatus) {
        String commandStatusHex = IntUtil.toHexString(commandStatus);
        String description = commandStatusToDescription.get(commandStatus);
        if (description != null) {
            return String.format("Negative response %s (%s) found", commandStatusHex, description);
        } else {
            return String.format("Negative response %s found", commandStatusHex);
        }
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
