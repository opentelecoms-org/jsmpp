package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 * @version 2.0
 * 
 */
public class SMPPSession extends ClientSession {

    public SMPPSession() {
        super();
    }

    public SMPPSession(String host, int port, BindParameter bindParam, ConnectionFactory connFactory) throws IOException {
        super(host, port, bindParam, connFactory);
    }

    public MessageId submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocolId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        PendingResponse<SubmitSmResp> pendingResp = pendingResponses.add(SubmitSmResp.class);
        try {
            SubmitSm submitSm = new SubmitSm();
            submitSm.setSequenceNumber(pendingResp.getSequenceNumber());
            submitSm.setServiceType(serviceType);
            submitSm.setSourceAddrTon(sourceAddrTon);
            submitSm.setSourceAddrNpi(sourceAddrNpi);
            submitSm.setSourceAddr(sourceAddr);
            submitSm.setDestAddress(destinationAddr);
            submitSm.setDestAddrNpi(destAddrNpi);
            submitSm.setDestAddrTon(destAddrTon);
            submitSm.setEsmClass(esmClass);
            submitSm.setProtocolId(protocolId);
            submitSm.setPriorityFlag(priorityFlag);
            submitSm.setScheduleDeliveryTime(scheduleDeliveryTime);
            submitSm.setValidityPeriod(validityPeriod);
            submitSm.setRegisteredDelivery(registeredDelivery);
            submitSm.setReplaceIfPresent(replaceIfPresentFlag);
            submitSm.setDataCoding(dataCoding);
            submitSm.setShortMessage(shortMessage);
            submitSm.setOptionalParameters(params);
            pduSender.sendSubmitSm(submitSm);
        } catch (RuntimeException e) {
            logger.error("Failed submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }
        pendingResponses.wait(pendingResp);
        logger.debug("Submit sm response received");
        return new MessageId(pendingResp.getResponse().getMessageId());
    }

    public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        PendingResponse<QuerySmResp> pendingResp = pendingResponses.add(QuerySmResp.class);
        try {
            QuerySm querySm = new QuerySm();
            querySm.setSequenceNumber(pendingResp.getSequenceNumber());
            querySm.setMessageId(messageId);
            querySm.setSourceAddrTon(sourceAddrTon);
            querySm.setSourceAddr(sourceAddr);
            querySm.setSourceAddrNpi(sourceAddrNpi);
            pduSender.sendQuerySm(querySm);
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