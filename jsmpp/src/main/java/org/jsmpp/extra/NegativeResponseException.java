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
        // 9 is reserved
        mapping.put(10, "Invalid Source Address");
        mapping.put(11, "Invalid Destination Address");
        mapping.put(12, "Message ID is invalid");
        mapping.put(13, "Bind Failed");
        mapping.put(14, "Invalid Password");
        mapping.put(15, "Invalid System ID");
        // 16 is reserved
        mapping.put(17, "Cancel SM Failed");
        // 18 is reserved
        mapping.put(19, "Replace SM Failed");
        mapping.put(20, "Message Queue Full");
        mapping.put(21, "Invalid Service Type");
        // 22 - 50 are reserved
        mapping.put(51, "Invalid number of destinations");
        mapping.put(52, "Invalid Distribution List name");
        // 53 - 63 are reserved
        mapping.put(64, "Destination flag is invalid (submit_multi)");
        // 65 is reserved
        mapping.put(66, "Invalid 'submit with replace' request");
        mapping.put(67, "Invalid esm_class field data");
        mapping.put(69, "submit_sm or submit_multi failed");
        // 70 - 71 are reserved
        mapping.put(72, "Invalid Source address TON");
        mapping.put(73, "Invalid Source address NPI");
        mapping.put(80, "Invalid Destination address TON");
        mapping.put(81, "Invalid Destination address NPI");
        // 82 is reserved
        mapping.put(83, "Invalid system_type field");
        mapping.put(84, "Invalid replace_if_present flag");
        mapping.put(85, "Invalid number of messages");
        // 86 - 87 are reserved
        mapping.put(88, "Throttling error (ESME has exceeded allowed message limits)");
        // 89 - 96 are reserved
        mapping.put(97, "Invalid Scheduled Delivery Time");
        mapping.put(98, "Invalid message validity period (Expiry time)");
        mapping.put(99, "Predefined Message Invalid or Not Found");
        mapping.put(100, "ESME Receiver Temporary App Error Code");
        mapping.put(101, "ESME Receiver Permanent App Error Code");
        mapping.put(102, "ESME Receiver Reject Message Error Code");
        mapping.put(103, "query_sm request failed");
        // 104 - 191 are reserved
        mapping.put(192, "Error in the optional part of the PDU Body");
        mapping.put(193, "Optional Parameter not allowed");
        mapping.put(194, "Invalid Parameter Length");
        mapping.put(195, "Expected Optional Parameter missing");
        mapping.put(196, "Invalid Optional Parameter Value");
        // 197 - 253 are reserved
        mapping.put(254, "Delivery Failure (used for data_sm_resp)");
        mapping.put(255, "Unknown Error");
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
