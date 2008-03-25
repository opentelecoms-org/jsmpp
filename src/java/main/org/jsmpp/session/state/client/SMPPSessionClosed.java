package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;

/**
 * This class is closed state implementation of {@link SMPPSessionState}. This
 * session state on SMPP specification context, implemented on since version
 * 5.0, but we can also use this.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionClosed extends ClientSessionState {

    public SMPPSessionClosed(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    public SessionState getSessionState() {
        return SessionState.CLOSED;
    }

    @Override
    public void processBindResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processDeliverSm(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processEnquireLink(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processEnquireLinkResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processGenericNack(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processQuerySmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processSubmitSmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processUnbind(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processUnbindResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processUnknownCid(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }
}
