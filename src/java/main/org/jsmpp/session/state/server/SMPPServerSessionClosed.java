package org.jsmpp.session.state.server;

import java.io.IOException;

import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionClosed extends ServerSessionState {

    public SMPPServerSessionClosed(ServerResponseHandler responseHandler) {
        super(responseHandler);
    }

    public SessionState getSessionState() {
        return SessionState.CLOSED;
    }

    @Override
    public void processBind(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processDeliverSmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processQuerySm(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    @Override
    public void processSubmitSm(PDU pdu) throws IOException {
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
