package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseHandler.class);

    private final BaseSMPPSession session;

    public BaseResponseHandler(BaseSMPPSession session) {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
        return (PendingResponse<Command>) this.session.pendingResponses.remove(sequenceNumber);
    }

    public void notifyUnbonded() {
        this.session.changeState(SessionState.UNBOUND);
    }

    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
        logger.debug("Sending enquire_link_resp");
        session.pduSender.sendEnquireLinkResp(sequenceNumber);
    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber) throws IOException {
        session.pduSender.sendGenericNack(commandStatus, sequenceNumber);
    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
        session.pduSender.sendHeader(originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
    }

    public void sendUnbindResp(int sequenceNumber) throws IOException {
        session.pduSender.sendUnbindResp(SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }
}
