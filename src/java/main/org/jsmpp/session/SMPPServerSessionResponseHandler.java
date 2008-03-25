package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.BindType;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class SMPPServerSessionResponseHandler extends BaseResponseHandler implements ServerResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionResponseHandler.class);

    private final SMPPServerSession serverSession;

    SMPPServerSessionResponseHandler(SMPPServerSession serverSession) {
        super(serverSession);
        this.serverSession = serverSession;
    }

    public void sendBindResp(String systemId, BindType bindType, int sequenceNumber) throws IOException {
        if (bindType.equals(BindType.BIND_RX)) {
            this.serverSession.changeState(SessionState.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TX)) {
            this.serverSession.changeState(SessionState.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            this.serverSession.changeState(SessionState.BOUND_TRX);
        }
        try {
            serverSession.pduSender.sendBindResp(bindType.commandId() | SMPPConstant.MASK_CID_RESP, sequenceNumber, systemId);
        } catch (PDUStringException e) {
            logger.error("Failed sending bind response", e);
            // FIXME uud: validate the systemId when the setting up the
            // value, so it never throws PDUStringException on above block
        }
    }

    public void sendSubmitSmResponse(MessageId messageId, int sequenceNumber) throws IOException {
        try {
            serverSession.pduSender.sendSubmitSmResp(sequenceNumber, messageId.getValue());
        } catch (PDUStringException e) {
            /*
             * There should be no PDUStringException thrown since creation of
             * MessageId should be safe.
             */
            logger.error("SYSTEM ERROR. Failed sending submitSmResp", e);
        }
    }

    public void processBind(Bind bind) {
        this.serverSession.bindRequestReceiver.notifyAcceptBind(bind);
    }

    public MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        return fireAcceptSubmitSm(submitSm);
    }

    public QuerySmResult processQuerySm(QuerySm querySm) throws ProcessRequestException {
        return fireAcceptQuerySm(querySm);
    }

    MessageId fireAcceptSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        if (serverSession.messageReceiverListener != null) {
            return serverSession.messageReceiverListener.onAcceptSubmitSm(submitSm, serverSession);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }

    QuerySmResult fireAcceptQuerySm(QuerySm querySm) throws ProcessRequestException {
        if (serverSession.messageReceiverListener != null) {
            return serverSession.messageReceiverListener.onAcceptQuerySm(querySm, serverSession);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RINVDFTMSGID);
    }
}