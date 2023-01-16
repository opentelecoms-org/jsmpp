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
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.InterfaceVersion;
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
    private static final Logger log = LoggerFactory.getLogger(AbstractGenericSMPPSessionBound.class);

    protected InterfaceVersion interfaceVersion;

    public InterfaceVersion getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(final InterfaceVersion interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        EnquireLink enquireLink = pduDecomposer.enquireLink(pdu);
        responseHandler.processEnquireLink(enquireLink);
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
    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        log.info("Receiving unbind request");
        try {
            responseHandler.sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            responseHandler.notifyUnbonded();
        }
    }

    @Override
    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            log.error("No request found for {}", pduHeader);
        }
    }

    @Override
    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendGenericNack(SMPPConstant.STAT_ESME_RINVCMDID,
                pduHeader.getSequenceNumber());
    }

    @Override
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new GenericNackResponseException(
                    "Received generic_nack with command_status "
                            + pduHeader.getCommandStatusAsHex(), pduHeader.getCommandStatus()));
            log.error("Received generic_nack with command_status={} and sequence_number={}"
                ,pduHeader.getCommandStatusAsHex(), IntUtil.toHexString(pduHeader.getSequenceNumber()));
        }
    }

    @Override
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        try {
            DataSm dataSm = pduDecomposer.dataSm(pdu);
            DataSmResult dataSmResult = responseHandler.processDataSm(dataSm);
            log.debug("Sending response with message_id {} for request with sequence_number {}", dataSmResult.getMessageId(), pduHeader.getSequenceNumber());
            responseHandler.sendDataSmResp(dataSmResult, pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());

        if (pendingResp != null) {
            try {
                DataSmResp resp = pduDecomposer.dataSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                log.error("Failed decomposing data_sm_resp", e);
                responseHandler.sendGenericNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            log.warn("No request with sequence_number {} found", pduHeader.getSequenceNumber());
        }
    }
}
