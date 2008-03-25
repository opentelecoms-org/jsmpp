package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;

/**
 * This class is bound_tx state implementation of {@link SMPPSessionState}.
 * Response to receiver related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionBoundRX extends SMPPSessionBoundTRX implements SMPPSessionState {

    public SMPPSessionBoundRX(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_RX;
    }

    @Override
    public void processSubmitSmResp(PDU pdu) throws IOException {
        responseHandler.sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }

    @Override
    public void processQuerySmResp(PDU pdu) throws IOException {
        responseHandler.sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }
}
