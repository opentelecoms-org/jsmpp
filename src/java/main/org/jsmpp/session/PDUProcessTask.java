package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.session.state.SMPPSessionState;

/**
 * @author uudashr
 *
 */
public class PDUProcessTask implements Runnable {
    private final Command pduHeader;
    private final byte[] pdu;
    private final SMPPSessionState stateProcessor;
    private final ResponseHandler responseHandler;
    private final ActivityNotifier activityNotifier;
    private final Runnable onIOExceptionTask;
    
    public PDUProcessTask(Command pduHeader, byte[] pdu,
            SMPPSessionState stateProcessor, ResponseHandler responseHandler,
            ActivityNotifier activityNotifier, Runnable onIOExceptionTask) {
        this.pduHeader = pduHeader;
        this.pdu = pdu;
        this.stateProcessor = stateProcessor;
        this.responseHandler = responseHandler;
        this.activityNotifier = activityNotifier;
        this.onIOExceptionTask = onIOExceptionTask;
    }

    public void run() {
        try {
            switch (pduHeader.getCommandId()) {
            case SMPPConstant.CID_BIND_RECEIVER_RESP:
            case SMPPConstant.CID_BIND_TRANSMITTER_RESP:
            case SMPPConstant.CID_BIND_TRANSCEIVER_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processBindResp(pduHeader, pdu, responseHandler);
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
            case SMPPConstant.CID_SUBMIT_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processSubmitSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_SUBMIT_MULTI_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processSubmitMultiResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_QUERY_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processQuerySmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DELIVER_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processDeliverSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM:
                activityNotifier.notifyActivity();
                stateProcessor.processDataSm(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_DATA_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processDataSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_CANCEL_SM_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processCancelSmResp(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND:
                activityNotifier.notifyActivity();
                stateProcessor.processUnbind(pduHeader, pdu, responseHandler);
                break;
            case SMPPConstant.CID_UNBIND_RESP:
                activityNotifier.notifyActivity();
                stateProcessor.processUnbindResp(pduHeader, pdu, responseHandler);
                break;
            default:
                stateProcessor.processUnknownCid(pduHeader, pdu, responseHandler);
            }
        } catch (IOException e) {
            onIOExceptionTask.run();
        }
    }
}
