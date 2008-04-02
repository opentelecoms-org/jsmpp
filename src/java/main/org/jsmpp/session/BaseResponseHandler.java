package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.Assert;
import org.jsmpp.BindType;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseResponseHandler implements ResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(BaseResponseHandler.class);

    private PDUSender pduSender;
    private PendingResponses pendingResponses;
    private BaseSession session;

    public void init(PDUSender pduSender, PendingResponses pendingResponses, BaseSession session) {
        Assert.isNull(this.pduSender);
        Assert.isNull(this.pendingResponses);
        Assert.isNull(this.session);

        Assert.notNull(pduSender);
        Assert.notNull(pendingResponses);
        Assert.notNull(session);
        this.pduSender = pduSender;
        this.pendingResponses = pendingResponses;
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
        return (PendingResponse<Command>) pendingResponses().remove(sequenceNumber);
    }

    public void notifyUnbonded() {
        changeState(SessionState.UNBOUND);
    }

    private void changeState(SessionState state) {
        session.changeState(state);
    }

    public void sendBindResp(String systemId, BindType bindType, int sequenceNumber) throws IOException {
        if (bindType.equals(BindType.BIND_RX)) {
            changeState(SessionState.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TX)) {
            changeState(SessionState.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            changeState(SessionState.BOUND_TRX);
        }
        try {
            pduSender().sendBindResp(bindType.commandId() | SMPPConstant.MASK_CID_RESP, sequenceNumber, systemId);
        } catch (PDUStringException e) {
            logger.error("Failed sending bind response", e);
            // FIXME uud: validate the systemId when the setting up the
            // value, so it never throws PDUStringException on above block
        }
    }

    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
        logger.debug("Sending enquire_link_resp");
        pduSender().sendEnquireLinkResp(sequenceNumber);
    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber) throws IOException {
        pduSender().sendGenericNack(commandStatus, sequenceNumber);
    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
        pduSender().sendHeader(originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
    }

    public void sendUnbindResp(int sequenceNumber) throws IOException {
        pduSender().sendUnbindResp(SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    public PendingResponses pendingResponses() {
        return pendingResponses;
    }

    public PDUSender pduSender() {
        return pduSender;
    }
}
