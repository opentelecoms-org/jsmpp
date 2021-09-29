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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.BroadcastSmResult;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.QueryBroadcastSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.jsmpp.session.SubmitMultiResult;
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.HexUtil;
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
    private static final Logger log = LoggerFactory.getLogger(StressServer.class);
    private static final int DEFAULT_MAX_WAIT_BIND = 5;
    private static final int DEFAULT_MAX_DELIVERIES = 5;
    private static final int DEFAULT_PORT = 8056;
    private static final int DEFAULT_PROCESSOR_DEGREE = 10;
    private static final String CANCELSM_NOT_IMPLEMENTED = "cancel_sm not implemented";
    private static final String REPLACESM_NOT_IMPLEMENTED = "replace_sm not implemented";
    private final ExecutorService waitBindExecService = Executors.newFixedThreadPool(DEFAULT_MAX_WAIT_BIND);
    private final ScheduledExecutorService deliveryExecService = Executors.newScheduledThreadPool(DEFAULT_MAX_DELIVERIES);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private final AbsoluteTimeFormatter timeFormatter = new AbsoluteTimeFormatter();
    private final AtomicInteger requestCounter = new AtomicInteger();
    private int processorDegree;
    private int port;

    public StressServer(int port, int processorDegree) {
        this.port = port;
        this.processorDegree = processorDegree;
    }

    @Override
    public void run() {
        try (SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port)) {
            sessionListener.setSessionStateListener(new SessionStateListenerImpl());
            sessionListener.setPduProcessorDegree(processorDegree);
            new TrafficWatcherThread().start();
            log.info("Listening on port {}", port);
            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                log.info("Accepting connection for session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                waitBindExecService.execute(new WaitBindTask(serverSession, 60000));
            }
        } catch (IOException e) {
            log.error("I/O error occurred", e);
        }
        waitBindExecService.shutdown();
        deliveryExecService.shutdown();
    }

    @Override
    public QuerySmResult onAcceptQuerySm(QuerySm querySm,
            SMPPServerSession source) throws ProcessRequestException {
        String finalDate = timeFormatter.format(new Date());
        log.info("Receiving query_sm, and return {}", finalDate);
        return new QuerySmResult(finalDate, MessageState.DELIVERED, (byte)0x00);
    }

    @Override
    public SubmitSmResult onAcceptSubmitSm(SubmitSm submitSm,
                                           SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        byte[] shortMessage = submitSm.getShortMessage();
        if (submitSm.isUdhi()) {
            int udhl = (shortMessage[0] & 0xff);
            log.info("Receiving submit_sm {} {}, return message id {}",
                HexUtil.convertBytesToHexString(shortMessage, 0, 1 + udhl),
                new String(shortMessage, 1+udhl,shortMessage.length - udhl - 1),
                messageId.getValue());
        } else {
            log.info("Receiving submit_sm {}, return message id {}", new String(submitSm.getShortMessage()), messageId.getValue());
        }
        requestCounter.incrementAndGet();

        if (SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitSm.getRegisteredDelivery())) {
            log.debug("Schedule delivery receipt in 1000ms");
            deliveryExecService.schedule(new DeliveryReceiptTask(source, submitSm, messageId), 1000, TimeUnit.MILLISECONDS);
        }

        return new SubmitSmResult(messageId, new OptionalParameter[0]);
    }

    @Override
    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        log.info("Receiving submit_multi {}, and return message id {}", new String(submitMulti.getShortMessage()), messageId.getValue());
        requestCounter.incrementAndGet();
        return new SubmitMultiResult(messageId.getValue(), new UnsuccessDelivery[0], new OptionalParameter[0]);
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        OptionalParameter.Message_payload messagePayload = (OptionalParameter.Message_payload)dataSm.getOptionalParameter(OptionalParameter.Tag.MESSAGE_PAYLOAD);
        log.info("Receiving data_sm {}, and return message id {}", messagePayload.getValueAsString(), messageId.getValue());
        requestCounter.incrementAndGet();
        return new DataSmResult(messageId, new OptionalParameter[]{});
    }

    @Override
    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
            throws ProcessRequestException {
        log.warn("cancel_sm not implemented");
        throw new ProcessRequestException(CANCELSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RCANCELFAIL);
    }

    @Override
    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
            throws ProcessRequestException {
        log.warn("replace_sm not implemented");
        throw new ProcessRequestException(REPLACESM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RREPLACEFAIL);
    }

    @Override
    public BroadcastSmResult onAcceptBroadcastSm(final BroadcastSm broadcastSm, final SMPPServerSession source)
        throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        OptionalParameter[] optionalParameters = new OptionalParameter[]{};
        log.info("Received broadcast_sm, and return {}", messageId);
        return new BroadcastSmResult(messageId, optionalParameters);
    }

    @Override
    public void onAcceptCancelBroadcastSm(final CancelBroadcastSm cancelBroadcastSm, final SMPPServerSession source)
        throws ProcessRequestException {
        log.info("Received cancel_broadcast_sm");
    }

    @Override
    public QueryBroadcastSmResult onAcceptQueryBroadcastSm(final QueryBroadcastSm queryBroadcastSm,
                                                           final SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        OptionalParameter[] optionalParameters = new OptionalParameter[]{};
        log.info("Receiving query_broadcast_sm, and return {}", messageId);
        return new QueryBroadcastSmResult(messageId, optionalParameters);
    }

    private static class SessionStateListenerImpl implements SessionStateListener {
        @Override
        public void onStateChange(SessionState newState, SessionState oldState, Session source) {
            SMPPServerSession session = (SMPPServerSession)source;
            log.info("New state of session {} is {}" , session.getSessionId(), newState);
        }
    }

    private static class WaitBindTask implements Runnable {
        private final SMPPServerSession serverSession;
        private final long timeout;

        public WaitBindTask(SMPPServerSession serverSession, long timeout) {
            this.serverSession = serverSession;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            try {
                log.info("Waiting {} ms for bind for session {} ", timeout, serverSession.getSessionId());
                BindRequest bindRequest = serverSession.waitForBind(timeout);
                log.info("Accepting bind for session {}", serverSession.getSessionId());
                try {
                    serverSession.setInterfaceVersion(InterfaceVersion.IF_50.min(bindRequest.getInterfaceVersion()));
                    bindRequest.accept("sys", InterfaceVersion.IF_50);

                } catch (PDUStringException e) {
                    log.error("Invalid system id", e);
                    bindRequest.reject(SMPPConstant.STAT_ESME_RINVSYSID);
                }
            } catch (IllegalStateException e) {
                log.error("System error", e);
            } catch (TimeoutException e) {
                log.warn("Timeout: {}", e.getMessage());
            } catch (IOException e) {
                log.error("Failed accepting bind request for session {}", serverSession.getSessionId());
            }
        }
    }

    private static class DeliveryReceiptTask implements Runnable {
        private final SMPPServerSession session;
        private final SubmitSm submitSm;
        private MessageId messageId;

        public DeliveryReceiptTask(SMPPServerSession session, SubmitSm submitSm, MessageId messageId) {
            this.session = session;
            this.submitSm = submitSm;
            this.messageId = messageId;
        }

        @Override
        public void run() {
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
                log.debug("Sending delivery receipt for message id {}: {}", messageId, stringValue);
            } catch (Exception e) {
                log.error("Failed sending delivery_receipt for message id " + messageId + ":" + stringValue, e);
            }
        }
    }

    private class TrafficWatcherThread extends Thread {
        @Override
        public void run() {
            log.info("Starting traffic watcher...");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                int requestsPerSecond = requestCounter.getAndSet(0);
                if (requestsPerSecond != 0) {
                    log.debug("Requests per second: {}", requestsPerSecond);
                }
            }
        }
    }

    public static void main(String[] args) {
        int port;
        try {
            port = Integer.parseInt(System.getProperty("jsmpp.server.port", Integer.toString(DEFAULT_PORT)));
        } catch (NumberFormatException e) {
            port = DEFAULT_PORT;
        }

        int processorDegree;
        try {
            processorDegree = Integer.parseInt(System.getProperty("jsmpp.server.procDegree",  Integer.toString(DEFAULT_PROCESSOR_DEGREE)));
        } catch (NumberFormatException e) {
            processorDegree = DEFAULT_PROCESSOR_DEGREE;
        }

        log.info("Processor degree: {}", processorDegree);
        StressServer stressServer = new StressServer(port, processorDegree);
        stressServer.run();
    }
}
