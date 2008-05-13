package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.session.ResponseHandler;

/**
 * This class is provide interface to response to every incoming SMPP Commands.
 * How the the response behavior is depends to it's states, or the
 * implementation of this class.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 */
public interface SMPPSessionState extends GenericSMPPSessionState {
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
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processBindResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;


    /**
     * Process the submit short message response command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an I/O error found.
     */
    void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;
    
    /**
     * Process a submit multiple message response.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an I/O error found.
     */
    void processSubmitMultiResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the query short message response command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

    /**
     * Process the deliver short message request command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;
    
    void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

}
