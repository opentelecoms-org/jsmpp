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
package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.SynchronizedPDUSender;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SMPPServerSession extends AbstractSession implements ServerSession {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSession.class);
    
    private final Connection conn;
    private final DataInputStream in;
    private final OutputStream out;
    
    private final PDUReader pduReader;
    
    private SMPPServerSessionContext sessionContext = new SMPPServerSessionContext(this);
    private final ServerResponseHandler responseHandler = new ResponseHandlerImpl();
    
    private ServerMessageReceiverListener messageReceiverListener;
    private ServerResponseDeliveryListener responseDeliveryListener;
    private final EnquireLinkSender enquireLinkSender;
    private BindRequestReceiver bindRequestReceiver = new BindRequestReceiver(responseHandler);
    
    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener,
            ServerResponseDeliveryListener responseDeliveryListener,
            int pduProcessorDegree) {
        this(conn, sessionStateListener, messageReceiverListener,
                responseDeliveryListener, pduProcessorDegree,
                new SynchronizedPDUSender(new DefaultPDUSender()),
                new DefaultPDUReader());
    }
    
    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener,
            ServerResponseDeliveryListener responseDeliveryListener,
            int pduProcessorDegree, PDUSender pduSender, PDUReader pduReader) {
        super(pduSender);
        this.conn = conn;
        this.messageReceiverListener = messageReceiverListener;
        this.responseDeliveryListener = responseDeliveryListener;
        this.pduReader = pduReader;
        this.in = new DataInputStream(conn.getInputStream());
        this.out = conn.getOutputStream();
        enquireLinkSender = new EnquireLinkSender();
        addSessionStateListener(new BoundStateListener());
        addSessionStateListener(sessionStateListener);
        setPduProcessorDegree(pduProcessorDegree);
        sessionContext.open();
    }
    
    /**
     * Wait for bind request.
     * 
     * @param timeout is the timeout.
     * @return the {@link BindRequest}.
     * @throws IllegalStateException if this invocation of this method has been
     *         made or invoke when state is not OPEN.
     * @throws TimeoutException if the timeout has been reach and
     *         {@link SMPPServerSession} are no more valid because the
     *         connection will be close automatically.
     */
    public BindRequest waitForBind(long timeout) throws IllegalStateException, TimeoutException {
        if (getSessionState().equals(SessionState.OPEN)) {
            new PDUReaderWorker().start();
            try {
                return bindRequestReceiver.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw e;
            }
        } else {
            throw new IllegalStateException("waitForBind() should be invoked on OPEN state, actual state is " + SessionState.OPEN);
        }
    }
    
    private synchronized boolean isReadPdu() {
        SessionState sessionState = sessionContext.getSessionState();
        return sessionState.isBound() || sessionState.equals(SessionState.OPEN);
    }
    
    public void deliverShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocoId, byte priorityFlag,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        
        DeliverSmCommandTask task = new DeliverSmCommandTask(pduSender(),
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId,
                protocoId, registeredDelivery, dataCoding, shortMessage,
                optionalParameters);
        
        executeSendCommand(task, getTransactionTimer());
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerSession#alertNotification(int, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.OptionalParameter[])
     */
    public void alertNotification(int sequenceNumber,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber esmeAddrTon,
            NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException {
        pduSender().sendAlertNotification(connection().getOutputStream(),
                sequenceNumber, sourceAddrTon.value(), sourceAddrNpi.value(),
                sourceAddr, esmeAddrTon.value(), esmeAddrNpi.value(), esmeAddr,
                optionalParameters);
    }
    
    private MessageId fireAcceptSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptSubmitSm(submitSm, this);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private SubmitMultiResult fireAcceptSubmitMulti(SubmitMulti submitMulti) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptSubmitMulti(submitMulti, this);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private QuerySmResult fireAcceptQuerySm(QuerySm querySm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptQuerySm(querySm, this);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private void fireAcceptReplaceSm(ReplaceSm replaceSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            messageReceiverListener.onAcceptReplaceSm(replaceSm, this);
        } else {
            throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
        }
    }
    
    private void fireAcceptCancelSm(CancelSm cancelSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            messageReceiverListener.onAcceptCancelSm(cancelSm, this);
        } else {
            throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
        }
    }
    
    private void fireSubmitSmRespSent(MessageId messageId) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitSmRespSent(messageId, this);
        }
    }
    
    private void fireSubmitSmRespFailed(MessageId messageId, Exception cause) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitSmRespError(messageId, cause,
                    this);
        }
    }
    
    private void fireSubmitMultiRespSent(SubmitMultiResult submitMultiResult) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitMultiRespSent(
                    submitMultiResult, this);
        }
    }
    
    private void fireSubmitMultiRespSentError(
            SubmitMultiResult submitMultiResult, Exception cause) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitMultiRespError(
                    submitMultiResult, cause, this);
        }
    }
    
    @Override
    protected Connection connection() {
        return conn;
    }
    
    @Override
    protected AbstractSessionContext sessionContext() {
        return sessionContext;
    }
    
    @Override
    protected GenericMessageReceiverListener messageReceiverListener() {
        return messageReceiverListener;
    }
    
    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }
    
    public void setMessageReceiverListener(
            ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }
    
    public void setResponseDeliveryListener(
            ServerResponseDeliveryListener responseDeliveryListener) {
        this.responseDeliveryListener = responseDeliveryListener;
    }
    
    private class ResponseHandlerImpl implements ServerResponseHandler {
        public PendingResponse<Command> removeSentItem(int sequenceNumber) {
            return removePendingResponse(sequenceNumber);
        }
        
        public void notifyUnbonded() {
            sessionContext.unbound();
        }
        
        public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
            logger.debug("Sending enquire_link_resp");
            pduSender().sendEnquireLinkResp(out, sequenceNumber);
        }

        public void sendGenerickNack(int commandStatus, int sequenceNumber)
                throws IOException {
            pduSender().sendGenericNack(out, commandStatus, sequenceNumber);
        }

        public void sendNegativeResponse(int originalCommandId,
                int commandStatus, int sequenceNumber) throws IOException {
            pduSender().sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
        }

        public void sendUnbindResp(int sequenceNumber) throws IOException {
            pduSender().sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        }
        
        public void sendBindResp(String systemId, BindType bindType, int sequenceNumber) throws IOException {
            sessionContext.bound(bindType);
            try {
                pduSender().sendBindResp(out, bindType.responseCommandId(), sequenceNumber, systemId);
            } catch (PDUStringException e) {
                logger.error("Failed sending bind response", e);
                // TODO uudashr: we have double checking when accept the bind request
            }
        }
        
        public void processBind(Bind bind) {
            bindRequestReceiver.notifyAcceptBind(bind);
        }
        
        public MessageId processSubmitSm(SubmitSm submitSm)
                throws ProcessRequestException {
            return fireAcceptSubmitSm(submitSm);
        }
        
        public void sendSubmitSmResponse(MessageId messageId, int sequenceNumber)
                throws IOException {
            try {
                pduSender().sendSubmitSmResp(out, sequenceNumber,
                        messageId.getValue());
                fireSubmitSmRespSent(messageId);
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be save.
                 */
                logger.error("SYSTEM ERROR. Failed sending submitSmResp", e);
                fireSubmitSmRespFailed(messageId, e);
            } catch (IOException e) {
                fireSubmitSmRespFailed(messageId, e);
                throw e;
            } catch (RuntimeException e) {
                fireSubmitSmRespFailed(messageId, e);
                throw e;
            }
        }
        
        public SubmitMultiResult processSubmitMulti(SubmitMulti submitMulti)
                throws ProcessRequestException {
            return fireAcceptSubmitMulti(submitMulti);
        }
        
        public void sendSubmitMultiResponse(
                SubmitMultiResult submitMultiResult, int sequenceNumber)
                throws IOException {
            try {
                pduSender().sendSubmitMultiResp(out, sequenceNumber,
                        submitMultiResult.getMessageId(),
                        submitMultiResult.getUnsuccessDeliveries());
                fireSubmitMultiRespSent(submitMultiResult);
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of the response parameter has been validated.
                 */
                logger.error("SYSTEM ERROR. Failed sending submitMultiResp", e);
                fireSubmitMultiRespSentError(submitMultiResult, e);
            } catch (IOException e) {
                fireSubmitMultiRespSentError(submitMultiResult, e);
                throw e;
            } catch (RuntimeException e) {
                fireSubmitMultiRespSentError(submitMultiResult, e);
                throw e;
            }
        }
        
        public QuerySmResult processQuerySm(QuerySm querySm)
                throws ProcessRequestException {
            return fireAcceptQuerySm(querySm);
        }
        
        public void sendQuerySmResp(String messageId, String finalDate,
                MessageState messageState, byte errorCode, int sequenceNumber) throws IOException {
            try {
                pduSender().sendQuerySmResp(out, sequenceNumber, messageId,
                        finalDate, messageState, errorCode);
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of parsed messageId has been validated.
                 */
                logger.error("SYSTEM ERROR. Failed sending cancelSmResp", e);
            }
        }
        
        public DataSmResult processDataSm(DataSm dataSm)
                throws ProcessRequestException {
            return fireAcceptDataSm(dataSm);
        }
        
        // TODO uudashr: we can generalize this method 
        public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
                throws IOException {
            try {
                pduSender().sendDataSmResp(out, sequenceNumber,
                        dataSmResult.getMessageId(),
                        dataSmResult.getOptionalParameters());
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be save.
                 */
                logger.error("SYSTEM ERROR. Failed sending dataSmResp", e);
            }
        }
        
        public void processCancelSm(CancelSm cancelSm)
                throws ProcessRequestException {
            fireAcceptCancelSm(cancelSm);
        }
        
        public void sendCancelSmResp(int sequenceNumber) throws IOException {
            pduSender().sendCancelSmResp(out, sequenceNumber);
        }
        
        
        public void processReplaceSm(ReplaceSm replaceSm)
                throws ProcessRequestException {
            fireAcceptReplaceSm(replaceSm);
        }
        
        public void sendReplaceSmResp(int sequenceNumber) throws IOException {
            pduSender().sendReplaceSmResp(out, sequenceNumber);
        }
    }
    
    private class PDUReaderWorker extends Thread {
        private ExecutorService executorService = Executors.newFixedThreadPool(getPduProcessorDegree());
        private Runnable onIOExceptionTask = new Runnable() {
            public void run() {
                close();
            };
        };
        
        @Override
        public void run() {
            logger.info("Starting PDUReaderWorker with processor degree:{} ...", getPduProcessorDegree());
            while (isReadPdu()) {
                readPDU();
            }
            close();
            executorService.shutdown();
            logger.info("PDUReaderWorker stop");
        }
        
        private void readPDU() {
            try {
                Command pduHeader = null;
                byte[] pdu = null;

                pduHeader = pduReader.readPDUHeader(in);
                pdu = pduReader.readPDU(in, pduHeader);
                
                PDUProcessServerTask task = new PDUProcessServerTask(pduHeader,
                        pdu, sessionContext.getStateProcessor(),
                        sessionContext, responseHandler, onIOExceptionTask);
                executorService.execute(task);
            } catch (InvalidCommandLengthException e) {
                logger.warn("Receive invalid command length", e);
                try {
                    pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
                } catch (IOException ee) {
                    logger.warn("Failed sending generic nack", ee);
                }
                unbindAndClose();
            } catch (SocketTimeoutException e) {
                notifyNoActivity();
            } catch (IOException e) {
                close();
            }
        }
        
        /**
         * Notify for no activity.
         */
        private void notifyNoActivity() {
            logger.debug("No activity notified");
            enquireLinkSender.enquireLink();
        }
    }
    
    private class EnquireLinkSender extends Thread {
        private final AtomicBoolean sendingEnquireLink = new AtomicBoolean(false);
        
        @Override
        public void run() {
            logger.info("Starting EnquireLinkSender");
            while (isReadPdu()) {
                while (!sendingEnquireLink.compareAndSet(true, false) && isReadPdu()) {
                    synchronized (sendingEnquireLink) {
                        try {
                            sendingEnquireLink.wait(500);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if (!isReadPdu()) {
                    break;
                }
                try {
                    sendEnquireLink();
                } catch (ResponseTimeoutException e) {
                    close();
                } catch (InvalidResponseException e) {
                    // lets unbind gracefully
                    unbindAndClose();
                } catch (IOException e) {
                    close();
                }
            }
            logger.info("EnquireLinkSender stop");
        }
        
        /**
         * This method will send enquire link asynchronously.
         */
        public void enquireLink() {
            if (sendingEnquireLink.compareAndSet(false, true)) {
                synchronized (sendingEnquireLink) {
                    sendingEnquireLink.notify();
                }
            }
        }
    }
    
    private class BoundStateListener implements SessionStateListener {
        public void onStateChange(SessionState newState, SessionState oldState,
                Object source) {
            if (newState.isBound()) {
                enquireLinkSender.start();
            }
        }
    }
}
