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

import org.jsmpp.GenericNackResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DataSmResp;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
abstract class AbstractGenericSMPPSessionBound implements GenericSMPPSessionState {
    protected static final PDUDecomposer pduDecomposer = new DefaultDecomposer();
    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericSMPPSessionBound.class);
    
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendEnquireLinkResp(pduHeader.getSequenceNumber());
    }

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

    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        logger.info("Receiving unbind request");
        try {
            responseHandler.sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            responseHandler.notifyUnbonded();
        }
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for {}", pduHeader);
        }
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID,
                pduHeader.getSequenceNumber());
    }
    
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new GenericNackResponseException(
                    "Receive generic_nack with command_status "
                            + pduHeader.getCommandStatusAsHex(), pduHeader.getCommandStatus()));
            logger.error("Receive generick_nack. "
                    + "command_status=" + pduHeader.getCommandStatusAsHex()
                    + ", sequence_number="
                    + IntUtil.toHexString(pduHeader.getSequenceNumber()));
        }
    }
    
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        try {
            DataSm dataSm = pduDecomposer.dataSm(pdu);
            DataSmResult dataSmResult = responseHandler.processDataSm(dataSm);
            logger.debug("Sending response with message_id {} for request with sequence_number {}", dataSmResult.getMessageId(), pduHeader.getSequenceNumber());
            responseHandler.sendDataSmResp(dataSmResult, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
    
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        
        if (pendingResp != null) {
            try {
                DataSmResp resp = pduDecomposer.dataSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing data_sm_resp", e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }
}
