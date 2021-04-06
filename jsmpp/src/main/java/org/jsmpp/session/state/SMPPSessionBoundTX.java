/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BroadcastSmResp;
import org.jsmpp.bean.CancelSmResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.ReplaceSmResp;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is Bound_TX state implementation of {@link SMPPSessionState}. This
 * class give specific response to a transmit related transaction, otherwise
 * it always give negative response.
 *
 * @author uudashr
 * @version 2.0
 * @since 2.0
 *
 */
class SMPPSessionBoundTX extends SMPPSessionBound implements SMPPSessionState {
    private static final String NO_REQUEST_FIND_FOR_SEQUENCE_NUMBER = "No request found for sequence_number ";
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundTX.class);

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }

    @Override
    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {

        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                SubmitSmResp resp = pduDecomposer.submitSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing submit_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processSubmitMultiResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                SubmitMultiResp resp = pduDecomposer.submitMultiResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing submit_multi_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {

        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QuerySmResp resp = pduDecomposer.querySmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing query_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.error(NO_REQUEST_FIND_FOR_SEQUENCE_NUMBER
                    + pduHeader.getSequenceNumber());
            responseHandler.sendGenerickNack(
                    SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                            .getSequenceNumber());
        }
    }

    @Override
    public void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            CancelSmResp resp = pduDecomposer.cancelSmResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error(NO_REQUEST_FIND_FOR_SEQUENCE_NUMBER
                    + pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processReplaceSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            ReplaceSmResp resp = pduDecomposer.replaceSmResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error(NO_REQUEST_FIND_FOR_SEQUENCE_NUMBER
                    + pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
    
    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        logger.error("Receiving alert_notification while on invalid bound state (transmitter)");
    }

    @Override
    public void processBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BroadcastSmResp resp = pduDecomposer.broadcastSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing broadcast_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processCancelBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BroadcastSmResp resp = pduDecomposer.broadcastSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing cancel_broadcast_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQueryBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BroadcastSmResp resp = pduDecomposer.broadcastSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing query_broadcast_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

}
