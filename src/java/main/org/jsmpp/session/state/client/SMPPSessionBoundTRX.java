package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is bound_trx state implementation of {@link SMPPSessionState}.
 * Response both to transmit and receive related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionBoundTRX extends SMPPSessionUnbound {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundTRX.class);

    public SMPPSessionBoundTRX(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TRX;
    }

    @Override
    public void processBindResp(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }

    @Override
    public void processDeliverSm(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        logger.debug("processing delivered sm " + pduHeader);
        try {
            DeliverSm deliverSm = pduDecomposer.deliverSm(pdu);
            responseHandler.processDeliverSm(deliverSm);
        } catch (PDUStringException e) {
            logger.error("Failed decomposing deliver_sm", e);
            responseHandler.sendNegativeResponse(pdu.getCommandId(), e.getErrorCode(), pdu.getCommand().getSequenceNumber());
        } catch (ProcessRequestException e) {
            logger.error("Failed processing deliver_sm", e);
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processSubmitSmResp(PDU pdu) throws IOException {
        try {
            SubmitSmResp resp = pduDecomposer.submitSmResp(pdu);
            responseHandler.processSubmitSmResp(resp);
        } catch (PDUStringException e) {
            logger.error("Failed decomposing submit_sm_resp", e);
            responseHandler.sendGenerickNack(e.getErrorCode(), pdu.getCommand().getSequenceNumber());
        }
    }

    @Override
    public void processQuerySmResp(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QuerySmResp resp = pduDecomposer.querySmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing submit_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
            }
        } else {
            logger.error("No request find for sequence number " + pduHeader.getSequenceNumber());
            responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }
}
