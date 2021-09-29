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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindType;
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
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.session.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.examples.session.connection.socket.KeyStoreSSLServerSocketConnectionFactory;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.BroadcastSmResult;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.QueryBroadcastSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.session.ServerResponseDeliveryAdapter;
import org.jsmpp.session.Session;
import org.jsmpp.session.SubmitSmResult;
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
public class SMPPServerSimulator extends ServerResponseDeliveryAdapter implements Runnable, ServerMessageReceiverListener {

    private static final Logger log = LoggerFactory.getLogger(SMPPServerSimulator.class);
    private static final String QUERYSM_NOT_IMPLEMENTED = "query_sm not implemented";
    private static final String CANCELSM_NOT_IMPLEMENTED = "cancel_sm not implemented";
    private static final String DATASM_NOT_IMPLEMENTED = "data_sm not implemented";
    private static final String REPLACESM_NOT_IMPLEMENTED = "replace_sm not implemented";
    private static final String BROADCASTSM_NOT_IMPLEMENTED = "broadcast_sm not implemented";
    private static final String CANCELBROADCASTSM_NOT_IMPLEMENTED = "cancel_broadcast_sm not implemented";
    private static final String QUERYBROADCASTSM_NOT_IMPLEMENTED = "query_broadcast_sm not implemented";
    private static final Integer DEFAULT_PORT = 8056;
    private static final String DEFAULT_SYSID = "j";
    private static final String DEFAULT_PASSWORD = "jpwd";
    private static final String SMSC_SYSTEMID = "sys";
    private final ExecutorService execService = Executors.newFixedThreadPool(5);
    private final ExecutorService execServiceDelReceipt = Executors.newFixedThreadPool(100);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private final boolean useSsl;
    private final int port;
    private final String systemId;
    private final String password;

    public SMPPServerSimulator(boolean useSsl, int port, String systemId, String password) {
        this.useSsl = useSsl;
        this.port = port;
        this.systemId = systemId;
        this.password = password;
    }

    @Override
    public void run() {
        boolean running = true;
        /*
         * for SSL use the SSLServerSocketConnectionFactory() or DefaultSSLServerSocketConnectionFactory()
         */
        try (SMPPServerSessionListener sessionListener = useSsl ?
            new SMPPServerSessionListener(port, new KeyStoreSSLServerSocketConnectionFactory())
            : new SMPPServerSessionListener(port)) {
            /*
             * for SSL use the SSLServerSocketConnectionFactory() or DefaultSSLServerSocketConnectionFactory()
             */
            log.info("Listening on port {}{}", port, useSsl ? " (SSL)" : "");
            while (running) {
                SMPPServerSession serverSession = sessionListener.accept();
                log.info("Accepted connection with session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                serverSession.setResponseDeliveryListener(this);
                Future<Boolean> bindResult = execService.submit(new WaitBindTask(serverSession, 30000, systemId, password));
                try {
                    boolean bound = bindResult.get();
                    if (bound) {
                        // Could start deliver_sm to ESME
                        log.info("The session is now in state {}", serverSession.getSessionState());

                        serverSession.deliverShortMessage("CMT",
                            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "555",
                            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "12345",
                            new ESMClass(0), (byte) 0x00, (byte) 0x00, new RegisteredDelivery(), DataCodings.ZERO,
                            "Hello World".getBytes());
                    }
                } catch (InterruptedException e){
                    log.info("Interrupted WaitBind task: {}", e.getMessage());
                    Thread.currentThread().interrupt();
                    running = false;
                } catch (ExecutionException e){
                    log.info("Exception on execute WaitBind task: {}", e.getMessage());
                    running = false;
                } catch (NegativeResponseException | ResponseTimeoutException | PDUException | InvalidResponseException e){
                    log.info("Could not send deliver_sm: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            log.error("IO error occurred", e);
        }
    }

    @Override
    public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession source) throws ProcessRequestException {
        log.info("QuerySm not implemented");
        throw new ProcessRequestException(QUERYSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RINVCMDID);
    }

    @Override
    public SubmitSmResult onAcceptSubmitSm(SubmitSm submitSm,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        log.info("Receiving submit_sm '{}', and return message id {}", new String(submitSm.getShortMessage()), messageId);
        if (SMSCDeliveryReceipt.FAILURE.containedIn(submitSm.getRegisteredDelivery()) || SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitSm.getRegisteredDelivery())) {
            execServiceDelReceipt.execute(new DeliveryReceiptTask(source, submitSm, messageId));
        }
        /*
         * SMPP 5.0 allows the following optional parameters (SMPP 5.0 paragraph 4.2.5):
         * additional_status_info_text, delivery_failure_reason, dpf_result, network_error_code
         * Add the congestionState for SMPP 5.0 connections.
         */
        if (source.getInterfaceVersion().value() >= InterfaceVersion.IF_50.value()) {
            final int congestionRatio = source.getCongestionRatio();
            OptionalParameter.Congestion_state congestionState = new OptionalParameter.Congestion_state((byte) congestionRatio);
            return new SubmitSmResult(messageId, new OptionalParameter[]{ congestionState });
        }
        return new SubmitSmResult(messageId, new OptionalParameter[0]);
    }

    @Override
    public void onSubmitSmRespSent(SubmitSmResult submitSmResult,
            SMPPServerSession source) {
        log.debug("submit_sm_resp with message_id {} has been sent", submitSmResult.getMessageId());
    }

    @Override
    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti, SMPPServerSession source)
        throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        log.debug("Receiving submit_multi_sm '{}', and return message id {}",
                new String(submitMulti.getShortMessage()), messageId);
        if (SMSCDeliveryReceipt.FAILURE.containedIn(submitMulti.getRegisteredDelivery())
                || SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitMulti.getRegisteredDelivery())) {
            execServiceDelReceipt.execute(new DeliveryReceiptTask(source, submitMulti, messageId));
        }
        /*
         * SMPP 5.0 allows the following optional parameters (SMPP 5.0 paragraph 4.2.5):
         * additional_status_info_text, delivery_failure_reason, dpf_result, network_error_code
         * Add the congestionState for SMPP 5.0 connections.
         */
        if (source.getInterfaceVersion().value() >= InterfaceVersion.IF_50.value()) {
            final int congestionRatio = source.getCongestionRatio();
            OptionalParameter.Congestion_state congestionState = new OptionalParameter.Congestion_state((byte) congestionRatio);
            return new SubmitMultiResult(messageId.getValue(), new UnsuccessDelivery[0], new OptionalParameter[]{ congestionState });
        }
        return new SubmitMultiResult(messageId.getValue(), new UnsuccessDelivery[0], new OptionalParameter[0]);
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
        log.info("Accepting data_sm, but not implemented");
        throw new ProcessRequestException(DATASM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RSYSERR);
    }

    @Override
    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
            throws ProcessRequestException {
        log.info("Accepting cancel_sm, but not implemented");
        throw new ProcessRequestException(CANCELSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RCANCELFAIL);
    }

    @Override
    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
            throws ProcessRequestException {
        log.info("Accepting replace_sm, but not implemented");
        throw new ProcessRequestException(REPLACESM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RREPLACEFAIL);
    }

    @Override
    public BroadcastSmResult onAcceptBroadcastSm(final BroadcastSm broadcastSm, final SMPPServerSession source)
        throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        log.debug("Receiving broadcast_sm '{}', and return message id {}",
            new String(broadcastSm.getOptionalParameter(OptionalParameter.Tag.MESSAGE_PAYLOAD).serialize()), messageId);
        return new BroadcastSmResult(messageId, new OptionalParameter[0]);
    }

    @Override
    public void onAcceptCancelBroadcastSm(final CancelBroadcastSm cancelBroadcastSm, final SMPPServerSession source)
        throws ProcessRequestException {
        log.info("Accepting cancel_broadcast_sm, but not implemented");
        throw new ProcessRequestException(CANCELBROADCASTSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RBCASTCANCELFAIL);
    }

    @Override
    public QueryBroadcastSmResult onAcceptQueryBroadcastSm(final QueryBroadcastSm queryBroadcastSm,
                                                           final SMPPServerSession source) throws ProcessRequestException {
        log.info("Accepting query_broadcast_sm, but not implemented");
        throw new ProcessRequestException(QUERYBROADCASTSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RBCASTQUERYFAIL);
    }

    private static class WaitBindTask implements Callable<Boolean> {
        private final SMPPServerSession serverSession;
        private final long timeout;
        private final String systemId;
        private final String password;

        public WaitBindTask(SMPPServerSession serverSession, long timeout, String systemId, String password) {
            this.serverSession = serverSession;
            this.timeout = timeout;
            this.systemId = systemId;
            this.password = password;
        }

        @Override
        public Boolean call() {
            try {
                BindRequest bindRequest = serverSession.waitForBind(timeout);
                try {
                    if (BindType.BIND_TRX.equals(bindRequest.getBindType())) {
                      if (systemId.equals(bindRequest.getSystemId())) {
                        if (password.equals(bindRequest.getPassword())) {
                          log.info("Accepting bind for session {}, interface version {}", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                          serverSession.setInterfaceVersion(InterfaceVersion.IF_50.min(bindRequest.getInterfaceVersion()));
                          // The systemId identifies the SMSC to the ESME.
                          bindRequest.accept(SMSC_SYSTEMID, InterfaceVersion.IF_50);
                          return true;
                        } else {
                          log.info("Rejecting bind for session {}, interface version {}, invalid password", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                          bindRequest.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                        }
                      } else {
                        log.info("Rejecting bind for session {}, interface version {}, invalid system id", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                        bindRequest.reject(SMPPConstant.STAT_ESME_RINVSYSID);
                      }
                    } else {
                      log.info("Rejecting bind for session {}, interface version {}, only accept transceiver", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                      bindRequest.reject(SMPPConstant.STAT_ESME_RBINDFAIL);
                    }
                } catch (PDUStringException e) {
                  log.error("Invalid system id: " + SMSC_SYSTEMID, e);
                  bindRequest.reject(SMPPConstant.STAT_ESME_RSYSERR);
                }

            } catch (IllegalStateException e) {
                log.error("System error", e);
            } catch (TimeoutException e) {
                log.warn("Wait for bind has reach timeout", e);
            } catch (IOException e) {
                log.error("Failed accepting bind request for session {}", serverSession.getSessionId());
            }
            return false;
        }
    }

    private static class DeliveryReceiptTask implements Runnable {
        private final SMPPServerSession session;
        private final MessageId messageId;

        private final TypeOfNumber sourceAddrTon;
        private final NumberingPlanIndicator sourceAddrNpi;
        private final String sourceAddress;

        private final TypeOfNumber destAddrTon;
        private final NumberingPlanIndicator destAddrNpi;
        private final String destAddress;

        private final int totalSubmitted;
        private final int totalDelivered;

        private final byte[] shortMessage;

        public DeliveryReceiptTask(SMPPServerSession session,
                SubmitSm submitSm, MessageId messageId) {
            this.session = session;
            this.messageId = messageId;

            // reversing destination to source
            sourceAddrTon = TypeOfNumber.valueOf(submitSm.getDestAddrTon());
            sourceAddrNpi = NumberingPlanIndicator.valueOf(submitSm.getDestAddrNpi());
            sourceAddress = submitSm.getDestAddress();

            // reversing source to destination
            destAddrTon = TypeOfNumber.valueOf(submitSm.getSourceAddrTon());
            destAddrNpi = NumberingPlanIndicator.valueOf(submitSm.getSourceAddrNpi());
            destAddress = submitSm.getSourceAddr();

            totalSubmitted = totalDelivered = 1;

            shortMessage = submitSm.getShortMessage();
        }

        public DeliveryReceiptTask(SMPPServerSession session,
                SubmitMulti submitMulti, MessageId messageId) {
            this.session = session;
            this.messageId = messageId;

            // set to unknown and null, since it was submit_multi
            sourceAddrTon = TypeOfNumber.UNKNOWN;
            sourceAddrNpi = NumberingPlanIndicator.UNKNOWN;
            sourceAddress = null;

            // reversing source to destination
            destAddrTon = TypeOfNumber.valueOf(submitMulti.getSourceAddrTon());
            destAddrNpi = NumberingPlanIndicator.valueOf(submitMulti.getSourceAddrNpi());
            destAddress = submitMulti.getSourceAddr();

            // distribution list assumed only contains single address
            totalSubmitted = totalDelivered = submitMulti.getDestAddresses().length;

            shortMessage = submitMulti.getShortMessage();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Interrupted", e);
                //re-interrupt the current thread
                Thread.currentThread().interrupt();
            }
            SessionState state = session.getSessionState();
            if (!state.isReceivable()) {
                log.debug("Not sending delivery receipt for message id {} since session state is {}", messageId, state);
                return;
            }
            String stringValue = Integer.valueOf(messageId.getValue(), 16).toString();
            try {

                DeliveryReceipt delRec = new DeliveryReceipt(stringValue, totalSubmitted, totalDelivered, new Date(), new Date(), DeliveryReceiptState.DELIVRD, "000", new String(shortMessage));
                session.deliverShortMessage(
                        "mc",
                        sourceAddrTon, sourceAddrNpi, sourceAddress,
                        destAddrTon, destAddrNpi, destAddress,
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

    public static void main(String[] args) {
        // System.setProperty("javax.net.debug", "ssl");
        /*
         * To use SSL, add -Djsmpp.simulator.ssl=true
         * To debug SSL, add -Djavax.net.debug=ssl
         */
        String systemId = System.getProperty("jsmpp.client.systemId", DEFAULT_SYSID);
        String password = System.getProperty("jsmpp.client.password", DEFAULT_PASSWORD);
        int port;
        try {
            port = Integer.parseInt(System.getProperty("jsmpp.simulator.port", DEFAULT_PORT.toString()));
        } catch (NumberFormatException e) {
            port = DEFAULT_PORT;
        }
        boolean useSsl = Boolean.parseBoolean(System.getProperty("jsmpp.simulator.ssl", "false"));
        SMPPServerSimulator smppServerSim = new SMPPServerSimulator(useSsl, port, systemId, password);
        smppServerSim.run();
    }
}
