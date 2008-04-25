package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUStringException;

/**
 * Task for sending SMPP command.
 * 
 * @author uudashr
 *
 */
public interface SendCommandTask {
    
    /**
     * Executing the task.
     * 
     * @param out is the output stream.
     * @param sequenceNumber is the sequence number.
     * @throws PDUStringException if there is an invalid PDU String found.
     * @throws IOException if there is an IO error found.
     */
    void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException;
    
    /**
     * It should be like submit_sm, deliver_sm, query_sm, data_sm, etc.
     * 
     * @return the command name.
     */
    String getCommandName();
}
