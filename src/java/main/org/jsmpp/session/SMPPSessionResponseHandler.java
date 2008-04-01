package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMPPSessionResponseHandler extends BaseResponseHandler implements ClientResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionResponseHandler.class);
    final MessageReceiverListener messageReceiverListener;

    public SMPPSessionResponseHandler(MessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        messageReceiverListener.onAcceptDeliverSm(deliverSm);
        try {
            sendDeliverSmResp(deliverSm.getSequenceNumber());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendDeliverSmResp(int sequenceNumber) throws IOException {
        try {
            pduSender().sendDeliverSmResp(sequenceNumber);
            logger.debug("deliver_sm_resp with seq_number " + sequenceNumber + " has been sent");
        } catch (PDUStringException e) {
            logger.error("Failed sending deliver_sm_resp", e);
        }
    }

    public void processSubmitSmResp(SubmitSmResp resp) {
        PendingResponse<Command> pendingResp = removeSentItem(resp.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence number " + resp.getSequenceNumber() + " found");
        }
    }

    public void processQuerySmResp(QuerySmResp resp) throws IOException {
        PendingResponse<Command> pendingResp = removeSentItem(resp.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.done(resp);
        } else {
            logger.error("No request find for sequence number " + resp.getSequenceNumber());
            sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, resp.getSequenceNumber());
        }
    }
}