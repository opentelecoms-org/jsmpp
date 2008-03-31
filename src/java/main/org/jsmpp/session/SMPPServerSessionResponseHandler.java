package org.jsmpp.session;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMPPServerSessionResponseHandler extends BaseResponseHandler implements ServerResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionResponseHandler.class);

    final BaseServerSession serverSession;

    public SMPPServerSessionResponseHandler(BaseServerSession serverSession) {
        super(serverSession);
        this.serverSession = serverSession;
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

    public void processDeliverSmResp(DeliverSmResp resp) {
        PendingResponse<Command> pendingResp = serverSession.responseHandler.removeSentItem(resp.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence number " + resp.getSequenceNumber() + " found");
        }
    }
}