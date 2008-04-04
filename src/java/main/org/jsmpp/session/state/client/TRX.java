package org.jsmpp.session.state.client;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is bound_trx state implementation of {@link Mode}. Response both
 * to transmit and receive related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class TRX extends ClientSessionState {
    private static final Logger logger = LoggerFactory.getLogger(TRX.class);

    public TRX(ClientSession session) {
        super(session);
    }

    public Mode getMode() {
        return Mode.BOUND_TRX;
    }

    @Override
    public void processBindResp(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }

    @Override
    public void processDeliverSm(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        logger.debug("processing delivered sm " + pduHeader);
        try {
            DeliverSm deliverSm = pduDecomposer.deliverSm(pdu);
            getSession().getResponseHandler().processDeliverSm(getSession(), deliverSm);
        } catch (PDUStringException e) {
            logger.error("Failed decomposing deliver_sm", e);
            sendNegativeResponse(pdu.getCommandId(), e.getErrorCode(), pdu.getCommand().getSequenceNumber());
        } catch (ProcessRequestException e) {
            logger.error("Failed processing deliver_sm", e);
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processSubmitSmResp(PDU pdu) {
        try {
            SubmitSmResp resp = pduDecomposer.submitSmResp(pdu);
            PendingResponse<SubmitSmResp> pendingResp = pendingResponses().remove(resp);
            if (pendingResp != null) {
                pendingResp.done(resp);
            } else {
                logger.warn("No request with sequence number " + resp.getSequenceNumber() + " found");
            }
        } catch (ProcessRequestException e) {
            logger.error("Failed decomposing submit_sm_resp", e);
            sendGenerickNack(e.getErrorCode(), pdu.getCommand().getSequenceNumber());
        }
    }

    @Override
    public void processQuerySmResp(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        try {
            QuerySmResp resp = pduDecomposer.querySmResp(pdu);
            PendingResponse<QuerySmResp> pendingResp = pendingResponses().remove(resp);
            if (pendingResp != null) {
                pendingResp.done(resp);
            } else {
                logger.error("No request find for sequence number " + resp.getSequenceNumber());
                sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, resp.getSequenceNumber());
            }
        } catch (PDUStringException e) {
            logger.error("Failed decomposing submit_sm_resp", e);
            sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
}
