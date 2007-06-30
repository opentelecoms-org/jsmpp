package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessMessageException;

/**
 * <tt>SMPPSessionHandler</tt> provide interface to handle the session
 * routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface SMPPSessionHandler {

    /**
     * Remove the previously {@link PendingResponse} that set when the request
     * was sent.
     * 
     * @param sequenceNumber the senquence number of the request.
     * @return the {@link PendingResponse} correspond to specified
     *         sequenceNumber. Return <tt>null</tt> if the the mapped
     *         sequenceNumber not found
     */
    public PendingResponse<Command> removeSentItem(int sequenceNumber);

    /**
     * Process the deliver
     * 
     * @param deliverSm
     * @throws ProcessMessageException
     */
    public void processDeliverSm(DeliverSm deliverSm)
            throws ProcessMessageException;

    /**
     * Send <b>DELIVER_SM_RESP</b> to smsc.
     * 
     * @param sequenceNumber
     * @throws IOException
     */
    public void sendDeliverSmResp(int sequenceNumber) throws IOException;

    /**
     * Send <b>GENERICK_NACK</b> to smsc.
     * 
     * @param commandStatus
     * @param sequenceNumber
     * @throws IOException
     */
    public void sendGenerickNack(int commandStatus, int sequenceNumber)
            throws IOException;

    /**
     * Send negative response to SMSC.
     * 
     * @param originalCommandId
     * @param commandStatus
     * @param sequenceNumber
     * @throws IOException
     */
    public void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send <b>ENQUIRE_LINK_RESP</b> to SMSC.
     * 
     * @param sequenceNumber
     * @throws IOException
     */
    public void sendEnquireLinkResp(int sequenceNumber) throws IOException;

    /**
     * Send <b>UNBIND_RESP</b> to SMSC.
     * 
     * @param sequenceNumber
     * @throws IOException
     */
    public void sendUnbindResp(int sequenceNumber) throws IOException;
}
