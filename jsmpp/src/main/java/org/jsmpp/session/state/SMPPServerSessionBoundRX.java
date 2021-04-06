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

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
class SMPPServerSessionBoundRX extends SMPPServerSessionBound implements
        SMPPServerSessionState {

    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionBoundRX.class);

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_RX;
    }

    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        processDeliverSmResp0(pduHeader, pdu, responseHandler);
    }

    @Override
    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processSubmitMulti(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processReplaceSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
    
    static final void processDeliverSmResp0(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            DeliverSmResp resp = pduDecomposer.deliverSmResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

    @Override
    public void processCancelBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

    @Override
    public void processQueryBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
            SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                .getSequenceNumber());
    }

}
