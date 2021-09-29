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

import org.jsmpp.InvalidNumberOfDestinationsException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.session.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BroadcastSmResult;
import org.jsmpp.session.QueryBroadcastSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.ServerResponseHandler;
import org.jsmpp.session.SubmitSmResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
class SMPPServerSessionBoundTX extends SMPPServerSessionBound implements
        SMPPServerSessionState {

    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSessionBoundTX.class);

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }

    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    @Override
    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            SubmitSm submitSm = pduDecomposer.submitSm(pdu);
            SubmitSmResult submitSmResult = responseHandler.processSubmitSm(submitSm);
            logger.debug("Sending response with message_id {} for request with sequence_number {}",
                submitSmResult.getMessageId(), pduHeader.getSequenceNumber());
            responseHandler.sendSubmitSmResponse(submitSmResult, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processSubmitMulti(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            SubmitMulti submitMulti = pduDecomposer.submitMulti(pdu);
            SubmitMultiResult result = responseHandler.processSubmitMulti(submitMulti);
            logger.debug("Sending response with message_id {} for request with sequence_number {}",
                result.getMessageId(), pduHeader.getSequenceNumber());
            responseHandler.sendSubmitMultiResponse(result, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (InvalidNumberOfDestinationsException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVNUMDESTS, pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            QuerySm querySm = pduDecomposer.querySm(pdu);
            QuerySmResult result = responseHandler.processQuerySm(querySm);
            responseHandler.sendQuerySmResp(querySm.getMessageId(),
                    result.getFinalDate(), result.getMessageState(),
                    result.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            CancelSm cancelSm = pduDecomposer.cancelSm(pdu);
            responseHandler.processCancelSm(cancelSm);
            responseHandler.sendCancelSmResp(pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processReplaceSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        try {
            ReplaceSm replaceSm = pduDecomposer.replaceSm(pdu);
            responseHandler.processReplaceSm(replaceSm);
            responseHandler.sendReplaceSmResp(pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        try {
            BroadcastSm broadcastSm = pduDecomposer.broadcastSm(pdu);
            BroadcastSmResult broadcastSmResult = responseHandler.processBroadcastSm(broadcastSm);
            responseHandler.sendBroadcastSmResp(broadcastSmResult, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processCancelBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        try {
            CancelBroadcastSm cancelBroadcastSm = pduDecomposer.cancelBroadcastSm(pdu);
            responseHandler.processCancelBroadcastSm(cancelBroadcastSm);
            responseHandler.sendCancelBroadcastSmResp(pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processQueryBroadcastSm(final Command pduHeader, final byte[] pdu, final ServerResponseHandler responseHandler)
        throws IOException {
        try {
            QueryBroadcastSm queryBroadcastSm = pduDecomposer.queryBroadcastSm(pdu);
            QueryBroadcastSmResult queryBroadcastSmResult = responseHandler.processQueryBroadcastSm(queryBroadcastSm);
            responseHandler.sendQueryBroadcastSmResp(queryBroadcastSmResult, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

}
