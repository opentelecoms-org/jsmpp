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
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.OutboundServerResponseHandler;
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is outbound state implementation of {@link SMPPOutboundServerSessionState}. When
 * the session state is outbound, we only give positive response to bind related intention.
 * 
 * @author pmoerenhout
 * @version 1.0
 * @since 2.3
 * 
 */
class SMPPOutboundServerSessionOutbound implements SMPPOutboundServerSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPOutboundServerSessionOutbound.class);
    private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();
    
    public SessionState getSessionState() {
        return SessionState.OUTBOUND;
    }

    public void processOutbind(Command pduHeader, byte[] pdu,
                            OutboundServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

    public void processBindResp(Command pduHeader, byte[] pdu,
                                OutboundServerResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BindResp resp = pduDecomposer.bindResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing submit_sm_resp";
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
                                 OutboundServerResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected deliver_sm"));
        }
    }

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected enquire_link"));
        }
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected enquire_link_resp"));
        }
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected generic_nack"));
        }
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected unbind"));
        }
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected unbind_resp"));
        }
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unknown command_id"));
        }
    }

    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected data_sm"));
        }
    }
    
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected data_sm_resp"));
        }
    }

    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unexpected alert_notification"));
        }
    }
}
