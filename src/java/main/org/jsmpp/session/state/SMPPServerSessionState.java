package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
public interface SMPPServerSessionState extends GenericSMPPSessionState {
    public static final SMPPServerSessionState OPEN = new SMPPServerSessionOpen();
    public static final SMPPServerSessionState BOUND_RX = new SMPPServerSessionBoundRX();
    public static final SMPPServerSessionState BOUND_TX = new SMPPServerSessionBoundTX();
    public static final SMPPServerSessionState BOUND_TRX = new SMPPServerSessionTRX();
    public static final SMPPServerSessionState UNBOUND = new SMPPServerSessionUnbound();
    public static final SMPPServerSessionState CLOSED = new SMPPServerSessionClosed();
    
    /**
     * Process the bind request command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an IO error occur.
     */
    void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the submit short message request command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException
     */
    void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the cancel short message request command.
     * 
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
}
