package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.session.SMPPSessionHandler;

/**
 * This class is provide interface to response to every incomming SMPP Commands.
 * How the the response behaviour is depends to it's states, or the
 * implementation of this class.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 */
public interface SMPPSessionState {
    public static final SMPPSessionState OPEN = new SMPPSessionOpen();
    public static final SMPPSessionState BOUND_RX = new SMPPSessionBoundRX();
    public static final SMPPSessionState BOUND_TX = new SMPPSessionBoundTX();
    public static final SMPPSessionState BOUND_TRX = new SMPPSessionBoundTRX();
    public static final SMPPSessionState UNBOUND = new SMPPSessionUnbound();
    public static final SMPPSessionState CLOSED = new SMPPSessionClosed();

    /**
     * Process the bind response command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processBindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the generick_nack command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processGenericNack(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the enquire_link command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;;

    /**
     * Process the enquire_link_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the submit_sm_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the query_sm_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the deliver_sm command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the unbind command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processUnbind(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the unbind_resp command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processUnbindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;

    /**
     * Process the unknown command id.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param sessionHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    public void processUnknownCid(Command pduHeader, byte[] pdu,
            SMPPSessionHandler sessionHandler) throws IOException;
}
