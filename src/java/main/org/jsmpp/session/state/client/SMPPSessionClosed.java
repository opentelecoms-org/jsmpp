package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is closed state implementation of {@link SMPPSessionState}. This
 * session state on SMPP specification context, implemented on since version
 * 5.0, but we can also use this.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionClosed implements SMPPSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionClosed.class);

    protected PDUDecomposer pduDecomposer = PDUDecomposer.getInstance();
    final protected ClientResponseHandler responseHandler;

    public SMPPSessionClosed(ClientResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public SessionState getSessionState() {
        return SessionState.CLOSED;
    }

    public void process(PDU pdu) throws IOException {
        switch (pdu.getCommandId()) {
        case SMPPConstant.CID_BIND_RECEIVER_RESP:
        case SMPPConstant.CID_BIND_TRANSMITTER_RESP:
        case SMPPConstant.CID_BIND_TRANSCEIVER_RESP:
            processBindResp(pdu);
            break;
        case SMPPConstant.CID_GENERIC_NACK:
            processGenericNack(pdu);
            break;
        case SMPPConstant.CID_ENQUIRE_LINK:
            processEnquireLink(pdu);
            break;
        case SMPPConstant.CID_ENQUIRE_LINK_RESP:
            processEnquireLinkResp(pdu);
            break;
        case SMPPConstant.CID_SUBMIT_SM_RESP:
            processSubmitSmResp(pdu);
            break;
        case SMPPConstant.CID_QUERY_SM_RESP:
            processQuerySmResp(pdu);
            break;
        case SMPPConstant.CID_DELIVER_SM:
            processDeliverSm(pdu);
            break;
        case SMPPConstant.CID_UNBIND:
            processUnbind(pdu);
            break;
        case SMPPConstant.CID_UNBIND_RESP:
            processUnbindResp(pdu);
            break;
        default:
            processUnknownCid(pdu);
        }
    }

    public void processEnquireLink(PDU pdu) throws IOException {
        responseHandler.sendEnquireLinkResp(pdu.getCommand().getSequenceNumber());
    }

    public void processEnquireLinkResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu.getCommand());
        }
    }

    public void processUnbind(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        logger.info("Receving unbind request");
        try {
            responseHandler.sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            responseHandler.notifyUnbonded();
        }
    }

    public void processUnbindResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu);
        }
    }

    public void processUnknownCid(PDU pdu) throws IOException {
        logger.warn("Received unknown command " + pdu.getCommand());
        responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID, pdu.getCommand().getSequenceNumber());
    }

    public void processGenericNack(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive generic_nack with command_status " + pdu.getCommand().getCommandStatusAsHex()));
            logger.error("Receive generick_nack for (" + pendingResp.getResponse().getCommandIdAsHex() + "). " + "command_status=" + pdu.getCommand().getCommandStatusAsHex() + ", sequence_number=" + IntUtil.toHexString(pdu.getCommand().getSequenceNumber()));
        }
    }

    public void processBindResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    public void processDeliverSm(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    public void processQuerySmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

    public void processSubmitSmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid operation for " + getSessionState() + " session state");
    }

}
