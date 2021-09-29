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

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
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
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.HexUtil;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSmppServer implements Runnable, ServerMessageReceiverListener {

  private static final Logger log = LoggerFactory.getLogger(TestSmppServer.class);

  private static final String NOT_IMPLEMENTED = "the command is not implemented";
  private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
  private final AbsoluteTimeFormatter timeFormatter = new AbsoluteTimeFormatter();
  private ExecutorService waitBindExecService = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService scheduledExecService = Executors.newScheduledThreadPool(5);
  private SMPPServerSessionListener sessionListener;
  private int processorDegree;
  private int port;
  private long bindTimeout;
  private int enquireLinkTimer;
  private int initiationTimer;

  private Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();
  private Map<String, SMPPServerSession> sessions = new ConcurrentHashMap<>();

  private CountDownLatch started = new CountDownLatch(1);
  private volatile boolean running = true;

  public TestSmppServer(final int port, final int processorDegree) {
    this.port = port;
    this.processorDegree = processorDegree;
  }

  public void setBindTimeout(final long bindTimeout) {
    this.bindTimeout = bindTimeout;
  }

  public void setEnquireLinkTimer(final int enquireLinkTimer) {
    this.enquireLinkTimer = enquireLinkTimer;
  }

  public void setInitiationTimer(final int initiationTimer) {
    this.initiationTimer = initiationTimer;
  }

  @Override
  public void run() {
    try {
      sessionListener = new SMPPServerSessionListener(port);
      sessionListener.setSessionStateListener(new SessionStateListenerImpl());
      sessionListener.setPduProcessorDegree(processorDegree);
      sessionListener.setInitiationTimer(initiationTimer);
      log.info("Server listening on port {}", port);
      started.countDown();
      while (running) {
        SMPPServerSession serverSession = sessionListener.accept();
        log.info("Accepting connection for session {}", serverSession.getSessionId());
        serverSession.setEnquireLinkTimer(enquireLinkTimer);
        sessions.put(serverSession.getSessionId(), serverSession);
        serverSession.setMessageReceiverListener(this);

        waitBindExecService.execute(new WaitBindTask(serverSession, bindTimeout));
      }
    } catch (SocketException e) {
      log.info("Listen socket: {}", e.getMessage());
    } catch (IOException e) {
      log.error("I/O error occurred", e);
    } finally {
    }
    waitBindExecService.shutdown();
    scheduledExecService.shutdown();
  }

  public void stop() throws InterruptedException {
    started.await();
    running = false;
    try {
      sessionListener.close();
    } catch (IOException e) {
      log.error("I/O error occurred on close", e);
    }
  }

  @Override
  public QuerySmResult onAcceptQuerySm(QuerySm querySm,
                                       SMPPServerSession source) throws ProcessRequestException {
    String finalDate = timeFormatter.format(new Date());
    log.info("Receiving query_sm, and return {}", finalDate);
    return new QuerySmResult(finalDate, MessageState.DELIVERED, (byte) 0x00);
  }

  @Override
  public SubmitSmResult onAcceptSubmitSm(SubmitSm submitSm,
                                         SMPPServerSession source) throws ProcessRequestException {
    MessageId messageId = messageIDGenerator.newMessageId();
    byte[] shortMessage = submitSm.getShortMessage();
    String message = null;
    if (submitSm.isUdhi()) {
      int udhl = (shortMessage[0] & 0xff);
      // For now assume LATIN-1 encoding
      message = new String(shortMessage, 1 + udhl, shortMessage.length - udhl - 1, StandardCharsets.ISO_8859_1);
      log.debug("Receiving submit_sm {} {}, return message id {}",
          HexUtil.convertBytesToHexString(shortMessage, 0, 1 + udhl),
          message,
          messageId.getValue());
    } else {
      message = new String(shortMessage, StandardCharsets.ISO_8859_1);
      log.debug("Receiving submit_sm {}, return message id {}", new String(submitSm.getShortMessage()), messageId.getValue());
    }

    increment("submit_sm");

    if ("close".equals(message)) {
      // Allow some time to send the response before closing
      scheduledExecService.schedule(new CloseTask(source), 25, TimeUnit.MILLISECONDS);
    }
    // TODO: Depend on interface version
    return new SubmitSmResult(messageId, new OptionalParameter[]{});
  }

  @Override
  public SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti,
                                               SMPPServerSession source) throws ProcessRequestException {
    MessageId messageId = messageIDGenerator.newMessageId();
    log.info("Receiving submit_multi {}, and return message id {}", new String(submitMulti.getShortMessage()), messageId.getValue());
    increment("submit_multi");
    return new SubmitMultiResult(messageId.getValue(), new UnsuccessDelivery[0], new OptionalParameter[0]);
  }

  @Override
  public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
      throws ProcessRequestException {
    MessageId messageId = messageIDGenerator.newMessageId();
    OptionalParameter.Message_payload messagePayload = (OptionalParameter.Message_payload) dataSm.getOptionalParameter(OptionalParameter.Tag.MESSAGE_PAYLOAD);
    log.info("Receiving data_sm {}, and return message id {}", messagePayload.getValueAsString(), messageId.getValue());
    increment("data_sm");
    return new DataSmResult(messageId, new OptionalParameter[]{});
  }

  @Override
  public void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
      throws ProcessRequestException {
    log.warn("CancelSm not implemented");
    increment("cancel_sm");
    throw new ProcessRequestException(NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RCANCELFAIL);
  }

  @Override
  public void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
      throws ProcessRequestException {
    log.warn("ReplaceSm not implemented");
    increment("replace_sm");
    throw new ProcessRequestException(NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RREPLACEFAIL);
  }

  @Override
  public BroadcastSmResult onAcceptBroadcastSm(final BroadcastSm broadcastSm, final SMPPServerSession source) throws ProcessRequestException {
    log.warn("BroadcastSm not implemented");
    increment("broadcast_sm");
    throw new ProcessRequestException(NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RBCASTFAIL);
  }

  @Override
  public QueryBroadcastSmResult onAcceptQueryBroadcastSm(final QueryBroadcastSm queryBroadcastSm,
                                                         final SMPPServerSession source) throws ProcessRequestException {
    increment("query_broadcast_sm");
    throw new ProcessRequestException(NOT_IMPLEMENTED, SMPPConstant.STAT_ESME_RBCASTQUERYFAIL);
  }

  @Override
  public void onAcceptCancelBroadcastSm(final CancelBroadcastSm cancelBroadcastSm, final SMPPServerSession source) throws ProcessRequestException {
    increment("cancel_broadcast_sm");
  }

  public void closeBoundSessions() {
    for (SMPPServerSession session : sessions.values()) {
      if (session.getSessionState().isBound()) {
        session.close();
      }
    }
  }

  public void logSessions() {
    for (SMPPServerSession session : sessions.values()) {
      log.info("Session {} in state {}", session.getSessionId(), session.getSessionState());
    }
  }

  public void increment(String counterName) {
    final AtomicInteger counter = counters.get(counterName);
    if (counter != null) {
      counter.incrementAndGet();
    } else {
      counters.put(counterName, new AtomicInteger(1));
    }
  }

  public int get(String counterName) {
    final AtomicInteger counter = counters.get(counterName);
    if (counter != null) {
      return counter.get();
    } else {
      return 0;
    }
  }

  private class SessionStateListenerImpl implements SessionStateListener {
    public void onStateChange(SessionState newState, SessionState oldState, Session source) {
      log.debug("New state of session {} is {}", source.getSessionId(), newState);
    }
  }

  private class CloseTask implements Runnable {
    private SMPPServerSession serverSession;

    public CloseTask(SMPPServerSession serverSession) {
      this.serverSession = serverSession;
    }

    public void run() {
      serverSession.close();
    }
  }

  private class WaitBindTask implements Runnable {
    private SMPPServerSession serverSession;
    private long timeout;

    public WaitBindTask(SMPPServerSession serverSession, long timeout) {
      this.serverSession = serverSession;
      this.timeout = timeout;
    }

    public void run() {
      try {
        BindRequest bindRequest = serverSession.waitForBind(timeout);
        log.info("Accepting bind for session {}", serverSession.getSessionId());
        try {
          bindRequest.accept("sys", InterfaceVersion.IF_34);
        } catch (PDUStringException e) {
          log.error("Invalid system id", e);
          bindRequest.reject(SMPPConstant.STAT_ESME_RSYSERR);
        }
      } catch (IllegalStateException e) {
        log.error("System error", e);
      } catch (TimeoutException e) {
        log.warn("Wait for bind has reach timeout", e);
      } catch (IOException e) {
        log.error("Failed accepting bind request for session {}", serverSession.getSessionId());
      } catch (Exception e) {
        log.error("Failed accepting bind request for session", e);
      }
    }
  }

  private class DeliveryReceiptTask implements Runnable {
    private final SMPPServerSession session;
    private final SubmitSm submitSm;
    private MessageId messageId;

    public DeliveryReceiptTask(SMPPServerSession session, SubmitSm submitSm, MessageId messageId) {
      this.session = session;
      this.submitSm = submitSm;
      this.messageId = messageId;
    }

    public void run() {
      String stringValue = Integer.valueOf(messageId.getValue(), 16).toString();
      try {

        DeliveryReceipt delRec = new DeliveryReceipt(stringValue, 1, 1, new Date(), new Date(), DeliveryReceiptState.DELIVRD, null,
            new String(submitSm.getShortMessage()));
        session.deliverShortMessage(
            "mc",
            TypeOfNumber.valueOf(submitSm.getDestAddrTon()),
            NumberingPlanIndicator.valueOf(submitSm.getDestAddrNpi()),
            submitSm.getDestAddress(),
            TypeOfNumber.valueOf(submitSm.getSourceAddrTon()),
            NumberingPlanIndicator.valueOf(submitSm.getSourceAddrNpi()),
            submitSm.getSourceAddr(),
            new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT),
            (byte) 0,
            (byte) 0,
            new RegisteredDelivery(0),
            DataCodings.ZERO,
            delRec.toString().getBytes());
        log.debug("Sending delivery receipt for message id {}: {}", messageId, stringValue);
      } catch (Exception e) {
        log.error("Failed sending delivery_receipt for message id " + messageId + ":" + stringValue, e);
      }
    }
  }

}
