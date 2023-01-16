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
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.OutboundServerResponseHandler;
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.session.SessionContext;
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
    private static final Logger log = LoggerFactory.getLogger(SMPPOutboundServerSessionOutbound.class);
    private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();

    @Override
    public SessionState getSessionState() {
        return SessionState.OUTBOUND;
    }

    @Override
    public void processOutbind(Command pduHeader, byte[] pdu,
                            OutboundServerResponseHandler responseHandler) throws IOException {
        log.info("Received outbind in OUTBOUND state, send negative response");
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

    @Override
    public void processBindResp(SessionContext sessionContext, Command pduHeader, byte[] pdu,
                                OutboundServerResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                BindResp resp = pduDecomposer.bindResp(pdu);
                OptionalParameter.Sc_interface_version scVersion = resp.getOptionalParameter(OptionalParameter.Sc_interface_version.class);
                log.debug("Other side reports SMPP interface version {}", scVersion);
                InterfaceVersion interfaceVersion = scVersion != null ?
                    InterfaceVersion.IF_50.min(InterfaceVersion.valueOf(scVersion.getValue())) :
                    InterfaceVersion.IF_34;
                if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_RECEIVER_RESP)
                {
                    sessionContext.bound(BindType.BIND_RX, interfaceVersion);
                }
                else if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_TRANSMITTER_RESP)
                {
                    sessionContext.bound(BindType.BIND_TX, interfaceVersion);
                }
                else if (pduHeader.getCommandId() == SMPPConstant.CID_BIND_TRANSCEIVER_RESP)
                {
                    sessionContext.bound(BindType.BIND_TRX, interfaceVersion);
                }
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing bind_resp";
                log.error(message, e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                    .getSequenceNumber());
                pendingResp
                    .doneWithInvalidResponse(new InvalidResponseException(
                        message, e));
            }
        } else {
            log.error("No request with sequence_number {} found", pduHeader.getSequenceNumber() );
            responseHandler.sendGenericNack(
                SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
                                 OutboundServerResponseHandler responseHandler) throws IOException {
        log.info("Received deliver_sm in OUTBOUND state, send negative response");
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
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
            log.error("No request found for {}", pduHeader);
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
    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        log.info("Received unbind in OUTBOUND state, send negative response");
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
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive unknown command_id"));
        }
    }

    @Override
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        log.info("Received data_sm in OUTBOUND state, send negative response");
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
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

    // TODO
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
