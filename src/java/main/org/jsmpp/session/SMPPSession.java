package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.ConnectionFactory;

/**
 * @author uudashr
 * @version 2.0
 * 
 */
public class SMPPSession extends ClientSession {

    public SMPPSession(MessageReceiverListener messageReceiverListener) {
        super(messageReceiverListener);
    }

    public SMPPSession(ClientResponseHandler responseHandler, MessageReceiverListener messageReceiverListener) {
        super(responseHandler, messageReceiverListener);
    }

    public SMPPSession(ConnectionFactory connFactory, MessageReceiverListener messageReceiverListener, ClientResponseHandler responseHandler) {
        super(connFactory, messageReceiverListener, responseHandler);
    }

    public SMPPSession(String host, int port, BindParameter bindParam, ConnectionFactory connFactory, MessageReceiverListener messageReceiverListener, ClientResponseHandler responseHandler) throws IOException {
        super(host, port, bindParam, connFactory, messageReceiverListener, responseHandler);
    }

    public String submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        PendingResponse<SubmitSmResp> pendingResp = pendingResponses.add(SubmitSmResp.class);
        try {
            pduSender.sendSubmitSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, scheduleDeliveryTime, validityPeriod, registeredDelivery, replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage, params);
        } catch (RuntimeException e) {
            logger.error("Failed submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }
        pendingResponses.wait(pendingResp);
        logger.debug("Submit sm response received");
        return pendingResp.getResponse().getMessageId();
    }

    public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        PendingResponse<QuerySmResp> pendingResp = pendingResponses.add(QuerySmResp.class);
        try {
            pduSender.sendQuerySm(pendingResp.getSequenceNumber(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        } catch (RuntimeException e) {
            logger.error("Failed to submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }

        pendingResponses.wait(pendingResp);
        logger.info("Query sm response received");

        QuerySmResp resp = pendingResp.getResponse();
        if (resp.getMessageId().equals(messageId)) {
            return new QuerySmResult(resp.getFinalDate(), resp.getMessageState(), resp.getErrorCode());
        }
        // message id requested not same as the returned
        throw new InvalidResponseException("Requested message_id doesn't match with the result");
    }
}