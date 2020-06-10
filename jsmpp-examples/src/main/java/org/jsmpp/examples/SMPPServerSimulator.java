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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindType;
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
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.session.ServerResponseDeliveryAdapter;
import org.jsmpp.session.Session;
import org.jsmpp.examples.session.connection.socket.KeyStoreSSLServerSocketConnectionFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(SMPPServerSimulator.class);
    private static final String QUERYSM_NOT_IMPLEMENTED = "query_sm not implemented";
    private static final String CANCELSM_NOT_IMPLEMENTED = "cancel_sm not implemented";
    private static final String DATASM_NOT_IMPLEMENTED = "data_sm not implemented";
    private static final String REPLACESM_NOT_IMPLEMENTED = "replace_sm not implemented";
    private static final Integer DEFAULT_PORT = 8056;
    private static final String DEFAULT_SYSID = "j";
    private static final String DEFAULT_PASSWORD = "jpwd";
    private static final String SMSC_SYSTEMID = "sys";
    private final ExecutorService execService = Executors.newFixedThreadPool(5);
    private final ExecutorService execServiceDelReceipt = Executors.newFixedThreadPool(100);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private boolean useSsl;
    private int port;
    private String systemId;
    private String password;

    public SMPPServerSimulator(boolean useSsl, int port, String systemId, String password) {
        this.useSsl = useSsl;
        this.port = port;
        this.systemId = systemId;
        this.password = password;
    }

    public void run() {
        try {
            /*
             * for SSL use the SSLServerSocketConnectionFactory() or DefaultSSLServerSocketConnectionFactory()
             */
            SMPPServerSessionListener sessionListener = useSsl ?
                new SMPPServerSessionListener(port, new KeyStoreSSLServerSocketConnectionFactory())
                : new SMPPServerSessionListener(port);
            LOGGER.info("Listening on port {}{}", port, useSsl ? " (SSL)" : "");
            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                LOGGER.info("Accepting connection for session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                serverSession.setResponseDeliveryListener(this);
                Future<Boolean> bindResult = execService.submit(new WaitBindTask(serverSession, systemId, password));
                try {
                    boolean bound = bindResult.get(60000, TimeUnit.MILLISECONDS);
                    if (bound) {
                        // Could start deliver_sm to ESME
                        LOGGER.info("The session is now in state {}", serverSession.getSessionState());
                    }
                } catch (InterruptedException e){
                    LOGGER.info("Interrupted WaitBind task: {}", e.getMessage());
                } catch (ExecutionException e){
                    LOGGER.info("Exception on execute WaitBind task: {}", e.getMessage());
                } catch (TimeoutException e){
                    LOGGER.info("Timeout on bind result: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            LOGGER.error("IO error occurred", e);
        }
    }

    @Override
    public QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession source) throws ProcessRequestException {
        LOGGER.info("QuerySm not implemented");
        throw new ProcessRequestException(QUERYSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RINVCMDID);
    }

    @Override
    public MessageId onAcceptSubmitSm(SubmitSm submitSm,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        LOGGER.info("Receiving submit_sm '{}', and return message id {}", new String(submitSm.getShortMessage()), messageId);
        if (SMSCDeliveryReceipt.FAILURE.containedIn(submitSm.getRegisteredDelivery()) || SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitSm.getRegisteredDelivery())) {
            execServiceDelReceipt.execute(new DeliveryReceiptTask(source, submitSm, messageId));
        }
        return messageId;
    }

    public void onSubmitSmRespSent(MessageId messageId,
            SMPPServerSession source) {
        LOGGER.debug("submit_sm_resp with message_id {} has been sent", messageId);
    }

    @Override
    public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti, SMPPServerSession source)
        throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        LOGGER.debug("Receiving submit_multi_sm '{}', and return message id {}",
                new String(submitMulti.getShortMessage()), messageId);
        if (SMSCDeliveryReceipt.FAILURE.containedIn(submitMulti.getRegisteredDelivery())
                || SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(submitMulti.getRegisteredDelivery())) {
            execServiceDelReceipt.execute(new DeliveryReceiptTask(source, submitMulti, messageId));
        }

        return new SubmitMultiResult(messageId.getValue(), new UnsuccessDelivery[0]);
    }

    @Override
    public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
            throws ProcessRequestException {
        LOGGER.info("Accepting DataSm, but not implemented");
        throw new ProcessRequestException(DATASM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RSYSERR);
    }

    @Override
    public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
            throws ProcessRequestException {
        LOGGER.info("Accepting CancelSm, but not implemented");
        throw new ProcessRequestException(CANCELSM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RCANCELFAIL);
    }

    @Override
    public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
            throws ProcessRequestException {
        LOGGER.info("AcceptingReplaceSm, but not implemented");
        throw new ProcessRequestException(REPLACESM_NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RREPLACEFAIL);
    }

    private static class WaitBindTask implements Callable<Boolean> {
        private final SMPPServerSession serverSession;
        private String systemId;
        private String password;

        public WaitBindTask(SMPPServerSession serverSession, String systemId, String password) {
            this.serverSession = serverSession;
            this.systemId = systemId;
            this.password = password;
        }

        @Override
        public Boolean call() {
            try {
                BindRequest bindRequest = serverSession.waitForBind(5000);
                try {
                    if (BindType.BIND_TRX.equals(bindRequest.getBindType())) {
                      if (systemId.equals(bindRequest.getSystemId())) {
                        if (password.equals(bindRequest.getPassword())) {
                          LOGGER.info("Accepting bind for session {}, interface version {}", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                          // The systemId identifies the SMSC to the ESME.
                          bindRequest.accept(SMSC_SYSTEMID, InterfaceVersion.IF_34);
                          return true;
                        } else {
                          LOGGER.info("Rejecting bind for session {}, interface version {}, invalid password", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                          bindRequest.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                        }
                      } else {
                        LOGGER.info("Rejecting bind for session {}, interface version {}, invalid system id", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                        bindRequest.reject(SMPPConstant.STAT_ESME_RINVSYSID);
                      }
                    } else {
                      LOGGER.info("Rejecting bind for session {}, interface version {}, only accept transceiver", serverSession.getSessionId(), bindRequest.getInterfaceVersion());
                      bindRequest.reject(SMPPConstant.STAT_ESME_RBINDFAIL);
                    }
                } catch (PDUStringException e) {
                  LOGGER.error("Invalid system id: " + SMSC_SYSTEMID, e);
                  bindRequest.reject(SMPPConstant.STAT_ESME_RSYSERR);
                }

            } catch (IllegalStateException e) {
                LOGGER.error("System error", e);
            } catch (TimeoutException e) {
                LOGGER.warn("Wait for bind has reach timeout", e);
            } catch (IOException e) {
                LOGGER.error("Failed accepting bind request for session {}", serverSession.getSessionId());
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
            } catch (InterruptedException e1) {
                LOGGER.error("Interupted", e1);
            }
            SessionState state = session.getSessionState();
            if (!state.isReceivable()) {
                LOGGER.debug("Not sending delivery receipt for message id {} since session state is {}", messageId, state);
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
                LOGGER.debug("Sending delivery receipt for message id {}: {}", messageId, stringValue);
            } catch (Exception e) {
                LOGGER.error("Failed sending delivery_receipt for message id " + messageId + ":" + stringValue, e);
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
