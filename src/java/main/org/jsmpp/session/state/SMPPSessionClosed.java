package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.SMPPSessionHandler;

/**
 * This class is closed state implementation of {@link SMPPSessionState}. This
 * sesion state on SMPP spesification context, implemented on since version 5.0,
 * but we can also use this.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionClosed implements SMPPSessionState {
    
    @Override
    public SessionState getSessionState() {
        return SessionState.CLOSED;
    }
    
    public void processBindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for closed session state");
    }
}
