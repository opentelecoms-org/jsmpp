package org.jsmpp.session;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
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

    ServerMessageReceiverListener messageReceiverListener;
    BindRequestReceiver bindRequestReceiver = new BindRequestReceiver(this);

    public SMPPServerSessionResponseHandler(ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    public MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            MessageId messageId = messageReceiverListener.onAcceptSubmitSm(submitSm);
            try {
                pduSender().sendSubmitSmResp(submitSm.getSequenceNumber(), messageId.getValue());
            } catch (PDUStringException e) {
                throw new ProcessRequestException("send submit_sm_resp failed", SMPPConstant.STAT_ESME_RX_R_APPN, e);
            } catch (IOException e) {
                throw new ProcessRequestException("send submit_sm_resp failed", SMPPConstant.STAT_ESME_RX_R_APPN, e);
            }
            return messageId;
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }

    public QuerySmResult processQuerySm(QuerySm querySm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            QuerySmResult res= messageReceiverListener.onAcceptQuerySm(querySm);
            try {
                pduSender().sendQuerySmResp(querySm, res);
            } catch (PDUStringException e) {
                throw new ProcessRequestException("send submit_sm_resp failed", SMPPConstant.STAT_ESME_RX_R_APPN, e);
            } catch (IOException e) {
                throw new ProcessRequestException("send submit_sm_resp failed", SMPPConstant.STAT_ESME_RX_R_APPN, e);
            }
            return res;
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RINVDFTMSGID);
    }

    @SuppressWarnings("unchecked")
    public void processDeliverSmResp(DeliverSmResp resp) {
        PendingResponse<DeliverSmResp> pendingResp = pendingResponses().remove(resp);
        if (pendingResp != null) {
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence number " + resp.getSequenceNumber() + " found");
        }
    }

    public void processBind(Bind bind) {
        bindRequestReceiver.notifyAcceptBind(bind);
    }

    public BindRequest waitForRequest(long timeout) throws TimeoutException {
        return bindRequestReceiver.waitForRequest(timeout);
    }
}