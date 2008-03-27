package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.IntUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is open state implementation of {@link SMPPSessionState}. When
 * the session state is open, we only give positive response to bind related
 * intention.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionOpen extends SMPPSessionClosed {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionOpen.class);

    public SMPPSessionOpen(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.OPEN;
    }

    @Override
    public void processBindResp(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                logger.debug("Bind Response header (" + pduHeader.getCommandLength() + ", " + pduHeader.getCommandIdAsHex() + ", " + IntUtil.toHexString(pduHeader.getCommandStatus()) + ", " + pduHeader.getSequenceNumber() + ")");
                BindResp resp = pduDecomposer.bindResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing submit_sm_resp";
                logger.error(message, e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
                pendingResp.doneWithInvalidResponse(new InvalidResponseException(message, e));
            }
        } else {
            logger.error("No request with sequence number " + pduHeader.getSequenceNumber() + " found");
            responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }
}
