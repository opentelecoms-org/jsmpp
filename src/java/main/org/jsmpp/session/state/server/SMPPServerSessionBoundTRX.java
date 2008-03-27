package org.jsmpp.session.state.server;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionBoundTRX extends SMPPServerSessionUnbound {

    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionBoundTRX.class);

    public SMPPServerSessionBoundTRX(ServerResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TRX;
    }

    @Override
    public void processBind(PDU pdu) throws IOException {
        responseHandler.sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RALYBND, pdu.getCommand().getSequenceNumber());
    }

    @Override
    public void processDeliverSmResp(PDU pdu) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            DeliverSmResp resp = pduDecomposer.deliverSmResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence number " + pdu.getCommand().getSequenceNumber() + " found");
        }
    }

    @Override
    public void processSubmitSm(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        try {
            SubmitSm submitSm = pduDecomposer.submitSm(pdu);
            responseHandler.processSubmitSm(submitSm);
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQuerySm(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        try {
            QuerySm querySm = pduDecomposer.querySm(pdu);
            responseHandler.processQuerySm(querySm);
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
}
