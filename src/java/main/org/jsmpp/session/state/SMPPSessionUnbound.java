package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.session.SMPPSessionHandler;

/**
 * This class is unbound state implementation of {@link SMPPSessionState}. All
 * this method is throw {@link IOException} since when the state is unbound we
 * should not give any positive response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionUnbound implements SMPPSessionState {

    public void processBindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        throw new IOException("Invalid process for unbound session state");
    }
}
