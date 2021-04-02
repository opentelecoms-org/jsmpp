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

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.session.SMPPSessionContext;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is open state implementation of {@link SMPPSessionState}. When
 * the session state is open, we only give positive response to bind related intention.
 *
 * @author uudashr
 * @version 2.0
 * @since 2.0
 *
 */
class SMPPSessionOpen implements SMPPSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionOpen.class);
    private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();

    @Override
    public SessionState getSessionState() {
        return SessionState.OPEN;
    }

    @Override
    public void processBindResp(SMPPSessionContext sessionContext, Command pduHeader, byte[] pdu,
                                ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BindResp resp = pduDecomposer.bindResp(pdu);
                if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_RECEIVER_RESP)
                {
                    sessionContext.bound(BindType.BIND_RX);
                }
                else if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_TRANSMITTER_RESP)
                {
                    sessionContext.bound(BindType.BIND_TX);
                }
                else if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_TRANSCEIVER_RESP)
                {
                    sessionContext.bound(BindType.BIND_TRX);
                }
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing bind_resp";
                logger.error(message, e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
                pendingResp
                        .doneWithInvalidResponse(new InvalidResponseException(
                                message, e));
            }
        } else {
            logger.error("No request with sequence_number {} found", pduHeader.getSequenceNumber() );
            responseHandler.sendGenerickNack(
                SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                    .getSequenceNumber());
        }
    }

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        logger.info("Received deliver_sm in OPEN state, send negative response");
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
//        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
//            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
//                .getSequenceNumber());
        responseHandler.sendEnquireLinkResp(pduHeader.getSequenceNumber());
    }

    @Override
    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for {}", pduHeader);
        }
    }

    @Override
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected generic_nack"));
        }
    }

    @Override
    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected submit_sm_resp"));
        }
    }

    @Override
    public void processSubmitMultiResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected submit_multi_resp"));
        }
    }

    @Override
    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

    @Override
    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected unbind_resp"));
        }
    }

    @Override
    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        if ((pduHeader.getCommandId() & SMPPConstant.MASK_CID_RESP) == SMPPConstant.MASK_CID_RESP){
            // response
            PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
            if (pendingResp != null) {

                pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unknown command_id"));
            }
        } else {
            // command
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                    .getSequenceNumber());
        }
    }

    @Override
    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected query_sm"));
        }
    }

    @Override
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

    @Override
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected data_sm_resp"));
        }
    }

    @Override
    public void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected cancel_sm_resp"));
        }
    }

    @Override
    public void processReplaceSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);

        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected replace_sm_resp"));
        }
    }

    @Override
    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected alert_notification"));
        }
    }

//    public void processBroadcastSm(Command pduHeader, byte[] pdu,
//                              BaseResponseHandler responseHandler) throws IOException {
//        PendingResponse<Command> pendingResp = responseHandler
//            .removeSentItem(1);
//        if (pendingResp != null) {
//            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
//                "Receive unexpected broadcast_sm"));
//        }
//    }

    public void processBroadcastSmResp(Command pduHeader, byte[] pdu,
                                  ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected broadcast_sm_resp"));
        }
    }

//    public void processCancelBroadcastSm(Command pduHeader, byte[] pdu,
//                                   ResponseHandler responseHandler) throws IOException {
//        PendingResponse<Command> pendingResp = responseHandler
//            .removeSentItem(1);
//        if (pendingResp != null) {
//            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
//                "Receive unexpected cancel_broadcast_sm"));
//        }
//    }

    public void processCancelBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected cancel_broadcast_sm_resp"));
        }
    }

//    public void processQueryBroadcastSm(Command pduHeader, byte[] pdu,
//                                         ResponseHandler responseHandler) throws IOException {
//        PendingResponse<Command> pendingResp = responseHandler
//            .removeSentItem(1);
//        if (pendingResp != null) {
//            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
//                "Receive unexpected query_broadcast_sm"));
//        }
//    }

    public void processQueryBroadcastSmResp(Command pduHeader, byte[] pdu,
                                             ResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected query_broadcast_sm_resp"));
        }
    }

}
