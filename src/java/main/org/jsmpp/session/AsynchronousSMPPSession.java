package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.connection.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AsynchronousSMPPSession extends BaseClientSession {

    private static final Logger logger = LoggerFactory.getLogger(AsynchronousSMPPSession.class);

    public AsynchronousSMPPSession(ConnectionFactory connFactory) {
        super(connFactory);
    }

    public void submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, IOException {
        PendingResponse<SubmitSmResp> pendingResp = pendingResponses.add(SubmitSmResp.class);
        try {
            pduSender.sendSubmitSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, scheduleDeliveryTime, validityPeriod, registeredDelivery, replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage, params);
        } catch (IOException e) {
            logger.error("Failed submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }
    }

    public void queryShortMessage(String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException, IOException {
        PendingResponse<QuerySmResp> pendingResp = pendingResponses.add(QuerySmResp.class);
        try {
            pduSender.sendQuerySm(pendingResp.getSequenceNumber(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        } catch (IOException e) {
            logger.error("Failed to submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }
    }

}