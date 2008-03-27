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
import org.jsmpp.session.connection.socket.SocketConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * @version 2.0
 * 
 */
public class SMPPSession extends BaseClientSession {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSession.class);

    public SMPPSession() {
        this(SocketConnectionFactory.getInstance());
    }

    public SMPPSession(ConnectionFactory connFactory) {
        super(connFactory);
    }

    public SMPPSession(String host, int port, BindParameter bindParam, ConnectionFactory connFactory) throws IOException {
        this(connFactory);
        connectAndBind(host, port, bindParam);
    }

    public SMPPSession(String host, int port, BindParameter bindParam) throws IOException {
        this();
        connectAndBind(host, port, bindParam);
    }

    public String submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<SubmitSmResp> pendingResp = pendingResponses.add(SubmitSmResp.class);
        try {
            pduSender.sendSubmitSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, scheduleDeliveryTime, validityPeriod, registeredDelivery, replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage, params);
        } catch (IOException e) {
            logger.error("Failed submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }

        pendingResponses.wait(pendingResp);
        logger.debug("Submit sm response received");
        return pendingResp.getResponse().getMessageId();
    }

    public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<QuerySmResp> pendingResp = pendingResponses.add(QuerySmResp.class);
        try {
            pduSender.sendQuerySm(pendingResp.getSequenceNumber(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        } catch (IOException e) {
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