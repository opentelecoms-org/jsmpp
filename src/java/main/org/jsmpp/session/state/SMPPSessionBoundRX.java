package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is bound_tx state implementation of {@link SMPPSessionState}.
 * Response to receiver related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundRX extends SMPPSessionBound implements SMPPSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundRX.class);
    private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();
    
    public SessionState getSessionState() {
        return SessionState.BOUND_RX;
    }
    
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        processDeliverSm0(pduHeader, pdu, responseHandler);
    }

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
    
    public void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
    
    static void processDeliverSm0(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        try {
            DeliverSm deliverSm = pduDecomposer.deliverSm(pdu);
            responseHandler.processDeliverSm(deliverSm);
            responseHandler.sendDeliverSmResp(pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            logger.error("Failed decomposing deliver_sm", e);
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e
                    .getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            logger.error("Failed processing deliver_sm", e);
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e
                    .getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
    
    
}
