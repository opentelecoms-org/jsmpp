package org.jsmpp.session.state.server;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.Mode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * 
 */
public class TRX extends ServerSessionState {
    private static final Logger logger = LoggerFactory.getLogger(TRX.class);

    public TRX(ServerSession serverSession) {
        super(serverSession);
    }

    public Mode getMode() {
        return Mode.BOUND_TRX;
    }

    @Override
    public void processBind(PDU pdu) {
        sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RALYBND, pdu.getCommand().getSequenceNumber());
    }

    @Override
    public void processDeliverSmResp(PDU pdu) {
        DeliverSmResp resp = pduDecomposer.deliverSmResp(pdu);
        PendingResponse<DeliverSmResp> pendingResp = pendingResponses().remove(resp);
        if (pendingResp != null) {
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence number " + resp.getSequenceNumber() + " found");
        }
    }

    @Override
    public void processSubmitSm(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        try {
            SubmitSm submitSm = pduDecomposer.submitSm(pdu);
            getSession().getResponseHandler().processSubmitSm(getSession(), submitSm);
        } catch (PDUStringException e) {
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQuerySm(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        try {
            QuerySm querySm = pduDecomposer.querySm(pdu);
            getSession().getResponseHandler().processQuerySm(getSession(), querySm);
        } catch (PDUStringException e) {
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
}
