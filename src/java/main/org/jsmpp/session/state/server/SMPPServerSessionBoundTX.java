package org.jsmpp.session.state.server;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionBoundTX extends SMPPServerSessionBoundTRX {
    public SMPPServerSessionBoundTX(ServerResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }

    @Override
    public void processDeliverSmResp(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

}
