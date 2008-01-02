package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
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
class SMPPServerSessionBoundTX extends SMPPServerSessionBound implements
        SMPPServerSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionBoundTX.class);
    
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }
    
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
    
    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            SubmitSm submitSm = pduDecomposer.submitSm(pdu);
            MessageId messageId = responseHandler.processSubmitSm(submitSm);
            logger.debug("Sending response with message_id " + messageId + " for request with sequence_number " + pduHeader.getSequenceNumber());
            responseHandler.sendSubmitSmResponse(messageId, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
    
    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
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
