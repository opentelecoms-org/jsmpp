package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
public class SMPPServerSessionUnbound implements SMPPServerSessionState {
    
    public SessionState getSessionState() {
        return SessionState.UNBOUND;
    }

    public void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }
    
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }
    
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException("Invalid process for open session state");
    }
    
}
