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
package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.session.state.SMPPServerSessionState;
import org.jsmpp.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class PDUProcessServerTask implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PDUProcessServerTask.class);

    private final Command pduHeader;
    private final byte[] pdu;
    private final SMPPServerSessionState stateProcessor;
    private final ActivityNotifier activityNotifier;
    private final ServerResponseHandler responseHandler;
    private final Runnable onIOExceptionTask;

    public PDUProcessServerTask(Command pduHeader, byte[] pdu,
            SMPPServerSessionState stateProcessor,
            ActivityNotifier activityNotifier,
            ServerResponseHandler responseHandler, Runnable onIOExceptionTask) {
        this.pduHeader = pduHeader;
        this.pdu = pdu;
        this.stateProcessor = stateProcessor;
        this.activityNotifier = activityNotifier;
        this.responseHandler = responseHandler;
        this.onIOExceptionTask = onIOExceptionTask;
    }

    @Override
    public void run() {
        try {
            int commandId = pduHeader.getCommandId();
            if (commandId == SMPPConstant.CID_ENQUIRE_LINK
                || commandId == SMPPConstant.CID_ENQUIRE_LINK_RESP) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Received PDU {}", HexUtil.convertBytesToHexString(pdu, 0, pdu.length));
                }
            } else if (logger.isDebugEnabled()) {
                logger.debug("Received PDU {}", HexUtil.convertBytesToHexString(pdu, 0, pdu.length));
            }

            switch (commandId) {
            case SMPPConstant.CID_BIND_RECEIVER:
            case SMPPConstant.CID_BIND_TRANSMITTER:
            case SMPPConstant.CID_BIND_TRANSCEIVER:
                activityNotifier.notifyActivity();
                stateProcessor.processBind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_GENERIC_NACK:
                activityNotifier.notifyActivity();
                stateProcessor.processGenericNack(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK:
                activityNotifier.notifyActivity();
                stateProcessor.processEnquireLink(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processEnquireLinkResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_SUBMIT_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processSubmitSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_SUBMIT_MULTI:
                activityNotifier.notifyActivity();
                stateProcessor.processSubmitMulti(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_QUERY_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processQuerySm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DELIVER_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processDeliverSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processDataSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processDataSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_CANCEL_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processCancelSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_REPLACE_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processReplaceSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND:
                activityNotifier.notifyActivity();
                stateProcessor.processUnbind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processUnbindResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_BROADCAST_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processBroadcastSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_CANCEL_BROADCAST_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processCancelBroadcastSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_QUERY_BROADCAST_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processQueryBroadcastSm(pduHeader, pdu, responseHandler);
                break;

            default:
                stateProcessor.processUnknownCid(pduHeader, pdu, responseHandler);
            }
        } catch (IOException e) {
            logger.info("I/O exception: {}", e.getMessage());
            onIOExceptionTask.run();
        }
    }

    protected Command getPduHeader() {
        return pduHeader;
    }
}
