package org.jsmpp.examples;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SMPPServerSimulator implements Runnable, ServerMessageReceiverListener {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSimulator.class);
    private final ExecutorService execService = Executors.newFixedThreadPool(5);
    private final ExecutorService execServiceDelReciept = Executors.newFixedThreadPool(100);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private int port;
    
    public SMPPServerSimulator(int port) {
        this.port = port;
    }
    
    public void run() {
        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);
            
            logger.info("Listening on port {}", port);
            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                logger.info("Accepting connection for session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                execService.execute(new WaitBindTask(serverSession));
            }
        } catch (IOException e) {
            logger.error("IO error occured", e);
        }
    }
    
    public QuerySmResult onAcceptQuerySm(QuerySm querySm,
            SMPPServerSession source) throws ProcessRequestException {
        logger.info("Accepting query sm, but not implemented");
        return null;
    }
    
    public MessageId onAcceptSubmitSm(SubmitSm submitSm,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        logger.debug("Receiving submit_sm {}, and return message id {}", new String(submitSm.getShortMessage()), messageId.getValue());
        if (SMSCDeliveryReceipt.SUCCESS.containedIn(submitSm.getRegisteredDelivery()) || SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitSm.getRegisteredDelivery())) {
            execServiceDelReciept.execute(new DeliveryReceiptTask(source, submitSm, messageId));
        }
        return messageId;
    }
    
    private class WaitBindTask implements Runnable {
        private final SMPPServerSession serverSession;
        
        public WaitBindTask(SMPPServerSession serverSession) {
            this.serverSession = serverSession;
        }

        public void run() {
            try {
                BindRequest bindRequest = serverSession.waitForBind(1000);
                logger.info("Accepting bind for session {}", serverSession.getSessionId());
                try {
                    bindRequest.accept("sys");
                } catch (IOException e) {
                    logger.error("Failed accepting bind request for session {}", serverSession.getSessionId());
                }
            } catch (IllegalStateException e) {
                logger.error("System error", e);
            } catch (TimeoutException e) {
                logger.warn("Wait for bind has reach timeout", e);
            }
        }
    }
    
    private class DeliveryReceiptTask implements Runnable {
        private final SMPPServerSession session;
        private final SubmitSm submitSm;
        private MessageId messageId;
        public DeliveryReceiptTask(SMPPServerSession session,
                SubmitSm submitSm, MessageId messageId) {
            this.session = session;
            this.submitSm = submitSm;
            this.messageId = messageId;
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            String stringValue = Integer.valueOf(messageId.getValue(), 16).toString();
            try {
                
                DeliveryReceipt delRec = new DeliveryReceipt(stringValue, 1, 1, new Date(), new Date(), DeliveryReceiptState.DELIVRD,  null, new String(submitSm.getShortMessage()));
                session.deliverShortMessage("mc", TypeOfNumber.valueOf(submitSm.getDestAddrTon()), NumberingPlanIndicator.valueOf(submitSm.getDestAddrNpi()), submitSm.getDestAddress(), TypeOfNumber.valueOf(submitSm.getSourceAddrTon()), NumberingPlanIndicator.valueOf(submitSm.getSourceAddrNpi()), submitSm.getSourceAddr(), new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT), (byte)0, (byte)0, null, null, new RegisteredDelivery(0), (byte)0, DataCoding.newInstance(0), (byte)0, delRec.toString().getBytes());
                logger.debug("Sending delivery reciept for message id " + messageId + ":" + stringValue);
            } catch (Exception e) {
                logger.error("Failed sending delivery_receipt for message id " + messageId + ":" + stringValue, e);
            }
            
        }
    }
    
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SMPPServerSimulator smppServerSim = new SMPPServerSimulator(8056);
        smppServerSim.run();
    }
}
