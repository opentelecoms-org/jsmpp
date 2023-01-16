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
import org.jsmpp.bean.CancelBroadcastSmResp;
import org.jsmpp.bean.CancelSmResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.QueryBroadcastSmResp;
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
 * class gives specific response to a transmit related transaction, otherwise
 * it always gives a negative response.
 *
 * @author uudashr
 * @version 2.0
 * @since 2.0
 *
 */
class SMPPSessionBoundTX extends SMPPSessionBound implements SMPPSessionState {
    private static final Logger log = LoggerFactory.getLogger(SMPPSessionBoundTX.class);
    private static final String NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND = "No request for sequence_number {} found";

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
                log.error("Failed decomposing submit_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
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
                log.error("Failed decomposing submit_multi_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {

        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QuerySmResp resp = pduDecomposer.querySmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                log.error("Failed decomposing query_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
            responseHandler.sendGenericNack(
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
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
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
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        /*
         * For SMPP 3.3, deliver_sm can be sent in BOUND_RX as well as in BOUND_TX mode,
         */
        if (interfaceVersion == InterfaceVersion.IF_33){
            processDeliverSm0(pduHeader, pdu, responseHandler);
            return;
        }
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        log.error("Receiving alert_notification while on invalid bound state (transmitter)");
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
                log.error("Failed decomposing broadcast_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processCancelBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                CancelBroadcastSmResp resp = pduDecomposer.cancelBroadcastSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                log.error("Failed decomposing cancel_broadcast_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQueryBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QueryBroadcastSmResp resp = pduDecomposer.queryBroadcastSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                log.error("Failed decomposing query_broadcast_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
            }
        } else {
            log.warn(NO_REQUEST_FOR_SEQUENCE_NUMBER_FOUND, pduHeader.getSequenceNumber());
        }
    }

}
