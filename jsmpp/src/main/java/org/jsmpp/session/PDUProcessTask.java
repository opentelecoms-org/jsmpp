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
import org.jsmpp.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class PDUProcessTask implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(PDUProcessTask.class);
    
    private final Command pduHeader;
    private final byte[] pdu;
    private final SMPPSessionContext sessionContext;
    private final ResponseHandler responseHandler;
    private final ActivityNotifier activityNotifier;
    private final Runnable onIOExceptionTask;

    public PDUProcessTask(Command pduHeader, byte[] pdu,
    		SMPPSessionContext sessionContext, ResponseHandler responseHandler,
            ActivityNotifier activityNotifier, Runnable onIOExceptionTask) {
        this.pduHeader = pduHeader;
        this.pdu = pdu;
        this.sessionContext = sessionContext;
        this.responseHandler = responseHandler;
        this.activityNotifier = activityNotifier;
        this.onIOExceptionTask = onIOExceptionTask;
    }

    @Override
    public void run() {
        try {
            if (pduHeader.getCommandId() == SMPPConstant.CID_ENQUIRE_LINK
                || pduHeader.getCommandId() == SMPPConstant.CID_ENQUIRE_LINK_RESP) {
                if (log.isTraceEnabled()) {
                    log.trace("Received PDU {}", HexUtil.convertBytesToHexString(pdu, 0, pdu.length));
                }
            } else if (log.isDebugEnabled()) {
                log.debug("Received PDU {}", HexUtil.convertBytesToHexString(pdu, 0, pdu.length));
            }

            switch (pduHeader.getCommandId()) {
            case SMPPConstant.CID_BIND_RECEIVER_RESP:
            case SMPPConstant.CID_BIND_TRANSMITTER_RESP:
            case SMPPConstant.CID_BIND_TRANSCEIVER_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processBindResp(sessionContext, pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_GENERIC_NACK:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processGenericNack(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processEnquireLink(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ENQUIRE_LINK_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processEnquireLinkResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_SUBMIT_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processSubmitSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_SUBMIT_MULTI_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processSubmitMultiResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_QUERY_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processQuerySmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DELIVER_SM:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processDeliverSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processDataSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processDataSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_CANCEL_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processCancelSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_REPLACE_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processReplaceSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_ALERT_NOTIFICATION:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processAlertNotification(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processUnbind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processUnbindResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_BROADCAST_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processBroadcastSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_CANCEL_BROADCAST_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processCancelBroadcastSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_QUERY_BROADCAST_SM_RESP:
                activityNotifier.notifyActivity();
                sessionContext.getStateProcessor().processQueryBroadcastSmResp(pduHeader, pdu, responseHandler);
                break;

            default:
            	sessionContext.getStateProcessor().processUnknownCid(pduHeader, pdu, responseHandler);
            }
        } catch (IOException e) {
            onIOExceptionTask.run();
        }
    }

    public Command getPduHeader() {
        return pduHeader;
    }
}
