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
 */
package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.QueueMaxException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 */
public class SMPPServerSession extends AbstractSession implements ServerSession {
    private static final String MESSAGE_RECEIVER_LISTENER_IS_NULL = "Received {} but message receiver listener is null";
    private static final String NO_MESSAGE_RECEIVER_LISTENER_REGISTERED = "No message receiver listener registered";

    private static final Logger log = LoggerFactory.getLogger(SMPPServerSession.class);

    private final Connection conn;
    private final DataInputStream in;
    private final OutputStream out;
    
    private final PDUReader pduReader;

    private final SMPPServerSessionContext sessionContext = new SMPPServerSessionContext(this);
    private final ServerResponseHandler responseHandler = new ResponseHandlerImpl();

    private PDUReaderWorker pduReaderWorker;
    private ServerMessageReceiverListener messageReceiverListener;
    private ServerResponseDeliveryListener responseDeliveryListener;
    private final BindRequestReceiver bindRequestReceiver = new BindRequestReceiver(responseHandler);

    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener,
            ServerResponseDeliveryListener responseDeliveryListener,
            int pduProcessorDegree, int queueCapacity) {
        this(conn, sessionStateListener, messageReceiverListener,
                responseDeliveryListener, pduProcessorDegree, queueCapacity,
                new SynchronizedPDUSender(new DefaultPDUSender()),
                new DefaultPDUReader());
    }
    
    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener,
            ServerResponseDeliveryListener responseDeliveryListener,
            int pduProcessorDegree, int queueCapacity, PDUSender pduSender, PDUReader pduReader) {
        super(pduSender);
        this.conn = conn;
        this.messageReceiverListener = messageReceiverListener;
        this.responseDeliveryListener = responseDeliveryListener;
        this.pduReader = pduReader;
        this.in = new DataInputStream(conn.getInputStream());
        this.out = conn.getOutputStream();
        enquireLinkSender = new EnquireLinkSender();
        addSessionStateListener(new BoundSessionStateListener());
        addSessionStateListener(sessionStateListener);
        setPduProcessorDegree(pduProcessorDegree);
        setQueueCapacity(queueCapacity);
        sessionContext.open();
    }
    
    public InetAddress getInetAddress() {
        return connection().getInetAddress();
    }

    public int getPort() {
        return connection().getPort();
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
    public BindRequest waitForBind(long timeout) throws IllegalStateException,
            TimeoutException {
        SessionState currentSessionState = getSessionState();
        if (currentSessionState.equals(SessionState.OPEN)) {
            pduReaderWorker = new PDUReaderWorker(getPduProcessorDegree(), getQueueCapacity());
            pduReaderWorker.start();
            try {
                return bindRequestReceiver.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException(
                        "Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw e;
            }
        } else {
            throw new IllegalStateException(
                    "waitForBind() should be invoked on OPEN state, actual state is "
                            + currentSessionState);
        }
    }

    @Override
    public void deliverShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        
        ensureReceivable("deliverShortMessage");
        
        DeliverSmCommandTask task = new DeliverSmCommandTask(pduSender(),
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destAddrTon, destAddrNpi, destinationAddr, esmClass, protocolId,
            priorityFlag, registeredDelivery, dataCoding, shortMessage,
                optionalParameters);
        
        executeSendCommand(task, getTransactionTimer());
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerSession#alertNotification(org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public void alertNotification(TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber esmeAddrTon, NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUException,
            IOException {
        
        ensureReceivable("alertNotification");

        AlertNotificationCommandTask task = new AlertNotificationCommandTask(pduSender(),
             sourceAddrTon, sourceAddrNpi, sourceAddr,
             esmeAddrTon, esmeAddrNpi, esmeAddr,
             optionalParameters);

        executeSendCommandWithNoResponse(task);
    }
    
    private SubmitSmResult fireAcceptSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptSubmitSm(submitSm, this);
        }
        log.warn("Received submit_sm but MessageReceiverListener is null, returning SMPP error");
        throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
                SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private SubmitMultiResult fireAcceptSubmitMulti(SubmitMulti submitMulti) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptSubmitMulti(submitMulti, this);
        }
        log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "submit_multi");
        throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
                SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private QuerySmResult fireAcceptQuerySm(QuerySm querySm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptQuerySm(querySm, this);
        }
        log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "query_sm");
        throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED, 
                SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private void fireAcceptReplaceSm(ReplaceSm replaceSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            messageReceiverListener.onAcceptReplaceSm(replaceSm, this);
        } else {
            log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "replace_sm");
            throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
                    SMPPConstant.STAT_ESME_RX_R_APPN);
        }
    }
    
    private void fireAcceptCancelSm(CancelSm cancelSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            messageReceiverListener.onAcceptCancelSm(cancelSm, this);
        } else {
            log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "cancel_sm");
            throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
                    SMPPConstant.STAT_ESME_RX_R_APPN);
        }
    }

    private BroadcastSmResult fireAcceptBroadcastSm(BroadcastSm broadcastSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptBroadcastSm(broadcastSm, this);
        }
        log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "broadcast_sm");
        throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
            SMPPConstant.STAT_ESME_RX_R_APPN);
    }

    private void fireAcceptCancelBroadcastSm(CancelBroadcastSm cancelBroadcastSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            messageReceiverListener.onAcceptCancelBroadcastSm(cancelBroadcastSm, this);
        } else {
            log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "cancel_broadcast_sm");
            throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
                SMPPConstant.STAT_ESME_RX_R_APPN);
        }
    }

    private QueryBroadcastSmResult fireAcceptQueryBroadcastSm(QueryBroadcastSm queryBroadcastSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptQueryBroadcastSm(queryBroadcastSm, this);
        }
        log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "query_broadcast_sm");
        throw new ProcessRequestException(NO_MESSAGE_RECEIVER_LISTENER_REGISTERED,
            SMPPConstant.STAT_ESME_RX_R_APPN);
    }

    private void fireSubmitSmRespSent(SubmitSmResult submitSmResult) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitSmRespSent(submitSmResult, this);
        }
    }
    
    private void fireSubmitSmRespFailed(SubmitSmResult submitSmResult, Exception cause) {
        if (responseDeliveryListener != null) {
            responseDeliveryListener.onSubmitSmRespError(submitSmResult, cause,
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

    /*
     * Return an integer between 0 and 100.
     * Only used for SMPP 5.0
     */
    public int getCongestionRatio() {
        return pduReaderWorker.getCongestionRatio();
    }
    
    private class ResponseHandlerImpl implements ServerResponseHandler {

        @Override
        public PendingResponse<Command> removeSentItem(int sequenceNumber) {
            return removePendingResponse(sequenceNumber);
        }

        @Override
        public void notifyUnbonded() {
            sessionContext.unbound();
        }

        @Override
        public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
            pduSender().sendEnquireLinkResp(out, sequenceNumber);
        }

        @Override
        public void sendGenericNack(int commandStatus, int sequenceNumber)
                throws IOException {
            pduSender().sendGenericNack(out, commandStatus, sequenceNumber);
        }

        @Override
        public void sendNegativeResponse(int originalCommandId,
                int commandStatus, int sequenceNumber) throws IOException {
            pduSender().sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
        }

        @Override
        public void sendUnbindResp(int sequenceNumber) throws IOException {
            pduSender().sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        }

        @Override
        public void sendBindResp(String systemId, InterfaceVersion interfaceVersion, BindType bindType, int sequenceNumber) throws IOException {
            sessionContext.bound(bindType, interfaceVersion);
            try {
                pduSender().sendBindResp(out, bindType.responseCommandId(), sequenceNumber, systemId, interfaceVersion);
            } catch (PDUStringException e) {
                log.error("Failed sending bind response", e);
            }
        }

        @Override
        public void processBind(Bind bind) {
            SMPPServerSession.this.bindRequestReceiver.notifyAcceptBind(bind);
        }

        @Override
        public SubmitSmResult processSubmitSm(SubmitSm submitSm)
                throws ProcessRequestException {
            try {
                SubmitSmResult submitSmResult = fireAcceptSubmitSm(submitSm);
                if (submitSmResult == null) {
                    String msg = "Invalid submitSmResult, shouldn't null value. " + ServerMessageReceiverListener.class + "#onAcceptSubmitSm(SubmitSm) return null value";
                    log.error(msg);
                    throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_R_APPN);
                }
                return submitSmResult;
            } 
            catch(ProcessRequestException e) {
				        throw e;
            }
            catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing submit_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendSubmitSmResponse(SubmitSmResult submitSmResult, int sequenceNumber)
                throws IOException {
            try {
                pduSender().sendSubmitSmResp(out, sequenceNumber,
                    submitSmResult.getMessageId(), submitSmResult.getOptionalParameters());
                fireSubmitSmRespSent(submitSmResult);
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be safe.
                 */
                log.error("Failed sending submit_sm_resp", e);
                fireSubmitSmRespFailed(submitSmResult, e);
            } catch (IOException | RuntimeException e) {
                fireSubmitSmRespFailed(submitSmResult, e);
                throw e;
            }
        }

        @Override
        public SubmitMultiResult processSubmitMulti(SubmitMulti submitMulti)
                throws ProcessRequestException {
            try {
                return fireAcceptSubmitMulti(submitMulti);
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing SubmitMultiSm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
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
                log.error("Failed sending submit_multi_resp", e);
                fireSubmitMultiRespSentError(submitMultiResult, e);
            } catch (IOException | RuntimeException e) {
                fireSubmitMultiRespSentError(submitMultiResult, e);
                throw e;
            }
        }

        @Override
        public QuerySmResult processQuerySm(QuerySm querySm)
                throws ProcessRequestException {
            try {
                return fireAcceptQuerySm(querySm);
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing query_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
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
                log.error("Failed sending query_sm_resp", e);
            }
        }

        @Override
        public DataSmResult processDataSm(DataSm dataSm)
                throws ProcessRequestException {
            try {
                return fireAcceptDataSm(dataSm);
            } catch(ProcessRequestException e) {
		throw e;
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing data_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
                throws IOException {
            try {
                pduSender().sendDataSmResp(out, sequenceNumber,
                        dataSmResult.getMessageId(),
                        dataSmResult.getOptionalParameters());
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be safe.
                 */
                log.error("Failed sending data_sm_resp", e);
            }
        }

        @Override
        public void processCancelSm(CancelSm cancelSm)
                throws ProcessRequestException {
            try {
                fireAcceptCancelSm(cancelSm);
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing cancel_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendCancelSmResp(int sequenceNumber) throws IOException {
            pduSender().sendCancelSmResp(out, sequenceNumber);
        }

        @Override
        public void processReplaceSm(ReplaceSm replaceSm)
                throws ProcessRequestException {
            try {
                fireAcceptReplaceSm(replaceSm);
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing replace_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendReplaceSmResp(int sequenceNumber) throws IOException {
            pduSender().sendReplaceSmResp(out, sequenceNumber);
        }

        @Override
        public BroadcastSmResult processBroadcastSm(final BroadcastSm broadcastSm) throws ProcessRequestException {
            try {
                BroadcastSmResult broadcastSmResult = fireAcceptBroadcastSm(broadcastSm);
                if (broadcastSmResult == null) {
                    String msg = "Invalid broadcastSmResult, shouldn't null value. " + ServerMessageReceiverListener.class + "#onAcceptBroadcastSm(broadcastSm) return null value";
                    log.error(msg);
                    throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_R_APPN);
                }
                return broadcastSmResult;
            }
            catch (ProcessRequestException e) {
                throw e;
            }
            catch (Exception e) {
                String msg = "Invalid runtime exception thrown when processing broadcast_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendBroadcastSmResp(final BroadcastSmResult broadcastSmResult, final int sequenceNumber) throws IOException {
            try {
                pduSender().sendBroadcastSmResp(out, sequenceNumber,
                    broadcastSmResult.getMessageId(),
                    broadcastSmResult.getOptionalParameters());
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be safe.
                 */
                log.error("Failed sending broadcast_sm_resp", e);
            }
        }

        @Override
        public void processCancelBroadcastSm(final CancelBroadcastSm cancelBroadcastSm)
            throws ProcessRequestException {
            try {
                fireAcceptCancelBroadcastSm(cancelBroadcastSm);
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing cancel_broadcast_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendCancelBroadcastSmResp(final int sequenceNumber) throws IOException {
            pduSender().sendCancelBroadcastSmResp(out, sequenceNumber);
        }

        @Override
        public QueryBroadcastSmResult processQueryBroadcastSm(final QueryBroadcastSm queryBroadcastSm)
            throws ProcessRequestException {
            try {
                QueryBroadcastSmResult queryBroadcastSmResult = fireAcceptQueryBroadcastSm(queryBroadcastSm);
                if (queryBroadcastSmResult == null) {
                    String msg = "Invalid queryBroadcastSmResult, shouldn't null value. " + ServerMessageReceiverListener.class + "#onAcceptQueryBroadcastSm(broadcastSm) return null value";
                    log.error(msg);
                    throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_R_APPN);
                }
                return queryBroadcastSmResult;
            } catch(Exception e) {
                String msg = "Invalid runtime exception thrown when processing query_broadcast_sm";
                log.error(msg, e);
                throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RSYSERR);
            }
        }

        @Override
        public void sendQueryBroadcastSmResp(final QueryBroadcastSmResult queryBroadcastSmResult, int sequenceNumber) throws IOException {
            try {
                pduSender().sendQueryBroadcastSmResp(out, sequenceNumber,
                    queryBroadcastSmResult.getMessageId(),
                    queryBroadcastSmResult.getOptionalParameters());
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be safe.
                 */
                log.error("Sending failed query_broadcast_sm_resp", e);
            }
        }

        @Override
        public void processEnquireLink(final EnquireLink enquireLink) {
            try {
                fireAcceptEnquirelink(enquireLink);
            } catch (Exception e) {
                log.error("Invalid runtime exception thrown when processing enquire_link", e);
            }
        }
    }
    
    private class PDUReaderWorker extends Thread {
        private ThreadPoolExecutor pduExecutor;
        private LinkedBlockingQueue<Runnable> workQueue;
        private int queueCapacity;
        private final Runnable onIOExceptionTask = () -> close();

        private PDUReaderWorker(final int pduProcessorDegree, final int queueCapacity) {
            super("PDUReaderWorker-" + getSessionId());
            this.queueCapacity = queueCapacity;
            workQueue = new LinkedBlockingQueue<>(queueCapacity);
            pduExecutor = new ThreadPoolExecutor(pduProcessorDegree, pduProcessorDegree,
                0L, TimeUnit.MILLISECONDS,
                workQueue, (runnable, executor) -> {
                    log.info("Receiving queue is full, please increasing queue capacity, and/or let other side obey the window size");
                    Command pduHeader = ((PDUProcessServerTask)runnable).getPduHeader();
                    if ((pduHeader.getCommandId() & SMPPConstant.MASK_CID_RESP) == SMPPConstant.MASK_CID_RESP) {
                        try {
                            boolean success = executor.getQueue().offer(runnable, 60000, TimeUnit.MILLISECONDS);
                            if (!success){
                                log.warn("Offer to queue failed for {}", pduHeader);
                            }
                        }
                        catch (InterruptedException e){
                            Thread.currentThread().interrupt();
                        }
                    } else {
                        throw new QueueMaxException("Receiving queue capacity " + queueCapacity + " exceeded");
                    }
                });
        }

        @Override
        public void run() {
            while (isReadPdu()) {
                readPDU();
            }
            close();
            pduExecutor.shutdown();
            try {
                pduExecutor.awaitTermination(getTransactionTimer(), TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                log.warn("Interrupted while waiting for PDU executor pool to finish");
                Thread.currentThread().interrupt();
            }
            log.debug("{} stopped", getName());
        }
        
        private void readPDU() {
            Command pduHeader = null;
            try {
                pduHeader = pduReader.readPDUHeader(in);
                byte[] pdu = pduReader.readPDU(in, pduHeader);

                PDUProcessServerTask task = new PDUProcessServerTask(pduHeader,
                        pdu, sessionContext.getStateProcessor(),
                        sessionContext, responseHandler, onIOExceptionTask);
                pduExecutor.execute(task);
            } catch (QueueMaxException e) {
                log.info("Notify other side to throttle: {} ({} threads active)", e.getMessage(), pduExecutor.getActiveCount());
                try {
                    responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RTHROTTLED, pduHeader.getSequenceNumber());
                } catch (IOException ioe) {
                    log.warn("Failed sending negative response: {}", ioe.getMessage());
                    close();
                }
            } catch (InvalidCommandLengthException e) {
                log.warn("Received invalid command length: {}", e.getMessage());
                try {
                    pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
                } catch (IOException ee) {
                    log.warn("Failed sending generic_nack", ee);
                }
                unbindAndClose();
            } catch (SocketTimeoutException e) {
                notifyNoActivity();
            } catch (EOFException e) {
                if (sessionContext.getSessionState() == SessionState.UNBOUND){
                    log.info("Unbound session {} socket closed", getSessionId());
                } else {
                    log.warn("Session {} socket closed unexpected", getSessionId());
                }
                close();
            } catch (IOException e) {
                log.info("Reading PDU session {} in state {}: {}", getSessionId(), getSessionState(), e.getMessage());
                close();
            } catch (RuntimeException e) {
                log.warn("Runtime error while reading", e);
                close();
            }
        }
        
        /**
         * Notify for no activity.
         */
        private void notifyNoActivity() {
            log.debug("No activity notified, sending enquire_link");
            enquireLinkSender.enquireLink();
        }

        /*
         * Return an integer between 0 (Idle) and 100 (Congested/Maximum Load). Only used for SMPP 5.0.
         */
        public int getCongestionRatio() {
            return ((80 * pduExecutor.getActiveCount()) / pduExecutor.getMaximumPoolSize()) +
                ((20 * workQueue.size()) / queueCapacity);
        }
    }
    
    private class BoundSessionStateListener implements SessionStateListener {
        @Override
        public void onStateChange(SessionState newState, SessionState oldState, Session source) {
            if (newState.equals(SessionState.OPEN)) {
                /*
                 * We need to set SO_TIMEOUT to session timer so when timeout occurs,
                 * a SocketTimeoutException will be raised. When Exception raised we
                 * can send an enquireLinkCommand.
                 */
                try {
                    connection().setSoTimeout(source.getEnquireLinkTimer());
                } catch (IOException e) {
                    log.error("Failed setting so_timeout for session timer", e);
                }
                enquireLinkSender.start();
            }
        }
    }
}
