package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;

/**
 * @author uudashr
 *
 */
public interface BaseResponseHandler {
    
    /**
     * Remove the previously {@link PendingResponse} that set when the request
     * was sent.
     * 
     * @param sequenceNumber the sequence number of the request.
     * @return the {@link PendingResponse} correspond to specified
     *         sequenceNumber. Return <tt>null</tt> if the the mapped
     *         sequenceNumber not found
     */
    public PendingResponse<Command> removeSentItem(int sequenceNumber);
    
    /**
     * Response by sending <b>GENERICK_NACK</b>.
     * 
     * @param commandStatus is the command status.
     * @param sequenceNumber is the sequence number original PDU if can be decoded.
     * @throws IOException if an IO error occur.
     */
    public void sendGenerickNack(int commandStatus, int sequenceNumber)
            throws IOException;
    /**
     * Response by sending negative response.
     * 
     * @param originalCommandId is the original command id.
     * @param commandStatus is the command status.
     * @param sequenceNumber is the sequence number of original PDU request.
     * @throws IOException if an IO error occur.
     */
    public void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException;
    
    /**
     * Response by sending <b>ENQUIRE_LINK_RESP</b>.
     * 
     * @param sequenceNumber is the sequence number of original <b>ENQUIRE_LINK</b>
     *          request.
     * @throws IOException if an IO error occur.
     */
    public void sendEnquireLinkResp(int sequenceNumber) throws IOException;
    
    /**
     * Response by send <b>UNBIND_RESP</b>.
     * 
     * @param sequenceNumber is the sequence number of original <b>UNBIND</b> request.
     * @throws IOException if an IO error occur.
     */
    public void sendUnbindResp(int sequenceNumber) throws IOException;
    
    /**
     * Notify for unbind.
     */
    public void notifyUnbonded();

}
