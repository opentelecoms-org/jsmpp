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
package org.jsmpp.examples;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
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
public class StressServer implements Runnable, ServerMessageReceiverListener {
    private static final int DEFAULT_MAX_WAIT_BIND = 10;
    private static final String DEFAULT_LOG4J_PATH = "stress/server-log4j.properties";
    private static final Integer DEFAULT_PORT = 8056;
    private static final Integer DEFAULT_PROCESSOR_DEGREE = 3;
    private static final Logger logger = LoggerFactory.getLogger(StressServer.class);
    private final ExecutorService waitBindExecService = Executors.newFixedThreadPool(DEFAULT_MAX_WAIT_BIND);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private final AtomicInteger requestCounter = new AtomicInteger();
    private int processorDegree;
    private int port;
    
    public StressServer(int port, int processorDegree) {
        this.port = port;
        this.processorDegree = processorDegree;
    }
    
    public void run() {
        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);
            sessionListener.setSessionStateListener(new SessionStateListenerImpl());
            sessionListener.setPduProcessorDegree(processorDegree);
            new TrafficWatcherThread().start();
            logger.info("Listening on port {}", port);
            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                logger.info("Accepting connection for session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                waitBindExecService.execute(new WaitBindTask(serverSession));
            }
        } catch (IOException e) {
            logger.error("IO error occured", e);
        }
    }
    
    public QuerySmResult onAcceptQuerySm(QuerySm querySm,
            SMPPServerSession source) throws ProcessRequestException {
        return null;
    }
    
    public MessageId onAcceptSubmitSm(SubmitSm submitSm,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        logger.debug("Receiving submit_sm {}, and return message id {}", new String(submitSm.getShortMessage()), messageId.getValue());
        requestCounter.incrementAndGet();
        return messageId;
    }
    
    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti,
            SMPPServerSession source) throws ProcessRequestException {
        return null;
    }
    
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
        return null;
    }
    
    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
            throws ProcessRequestException {
    }
    
    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
            throws ProcessRequestException {
    }
    
    private class SessionStateListenerImpl implements SessionStateListener {
        public void onStateChange(SessionState newState, SessionState oldState,
        		Session source) {
            SMPPServerSession session = (SMPPServerSession)source;
            logger.info("New state of " + session.getSessionId() + " is " + newState);
        }
    }
    
    private class WaitBindTask implements Runnable {
        private final SMPPServerSession serverSession;
        
        public WaitBindTask(SMPPServerSession serverSession) {
            this.serverSession = serverSession;
        }

        public void run() {
            try {
                BindRequest bindRequest = serverSession.waitForBind(1000);
                logger.debug("Accepting bind for session {}", serverSession.getSessionId());
                try {
                    bindRequest.accept("sys", InterfaceVersion.IF_34);
                } catch (PDUStringException e) {
                    logger.error("Invalid system id", e);
                    bindRequest.reject(SMPPConstant.STAT_ESME_RSYSERR);
                }
            } catch (IllegalStateException e) {
                logger.error("System error", e);
            } catch (TimeoutException e) {
                logger.warn("Wait for bind has reach timeout", e);
            } catch (IOException e) {
                logger.error("Failed accepting bind request for session {}", serverSession.getSessionId());
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
                session.deliverShortMessage(
                        "mc", 
                        TypeOfNumber.valueOf(submitSm.getDestAddrTon()), 
                        NumberingPlanIndicator.valueOf(submitSm.getDestAddrNpi()), 
                        submitSm.getDestAddress(), 
                        TypeOfNumber.valueOf(submitSm.getSourceAddrTon()), 
                        NumberingPlanIndicator.valueOf(submitSm.getSourceAddrNpi()), 
                        submitSm.getSourceAddr(), 
                        new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT), 
                        (byte)0, 
                        (byte)0, 
                        new RegisteredDelivery(0), 
                        DataCodings.ZERO, 
                        delRec.toString().getBytes());
                logger.debug("Sending delivery reciept for message id " + messageId + ":" + stringValue);
            } catch (Exception e) {
                logger.error("Failed sending delivery_receipt for message id " + messageId + ":" + stringValue, e);
            }
        }
    }
    
    
    private class TrafficWatcherThread extends Thread {
        @Override
        public void run() {
            logger.info("Starting traffic watcher...");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                int trafficPerSecond = requestCounter.getAndSet(0);
                logger.info("Traffic per second : " + trafficPerSecond);
            }
        }
    }
    
    public static void main(String[] args) {
        int port;
        try {
            port = Integer.parseInt(System.getProperty("jsmpp.server.port", DEFAULT_PORT.toString()));
        } catch (NumberFormatException e) {
            port = DEFAULT_PORT;
        }
        
        int processorDegree;
        try {
            processorDegree = Integer.parseInt(System.getProperty("jsmpp.server.procDegree", DEFAULT_PROCESSOR_DEGREE.toString()));
        } catch (NumberFormatException e) {
            processorDegree = DEFAULT_PROCESSOR_DEGREE;
        }
        
        String log4jPath = System.getProperty("jsmpp.server.log4jPath", DEFAULT_LOG4J_PATH);
        PropertyConfigurator.configure(log4jPath);
        
        logger.info("Processor degree: " + processorDegree);
        StressServer stressServer = new StressServer(port, processorDegree);
        stressServer.run();
    }
}
