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
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSmppOutboundServer implements Runnable, OutboundServerMessageReceiverListener {

  private static final Logger log = LoggerFactory.getLogger(TestSmppOutboundServer.class);

  private ExecutorService waitOutbindExecService = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService scheduledExecService = Executors.newScheduledThreadPool(5);
  private OutboundSMPPServerSessionListener sessionListener;
  private int port;
  private int processorDegree;
  private long outbindTimeout = 60000L;
  private long bindTimeout = 60000L;
  private int enquireLinkTimer;
  private int initiationTimer;
  // Default version for new sessions
  private InterfaceVersion interfaceVersion;

  private Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();
  private Map<String, SMPPOutboundServerSession> sessions = new ConcurrentHashMap<>();

  private CountDownLatch started = new CountDownLatch(1);
  private volatile boolean running = true;

  public TestSmppOutboundServer(final int port, final int processorDegree) {
    this.port = port;
    this.processorDegree = processorDegree;
  }

  public void setBindTimeout(final long bindTimeout) {
    this.bindTimeout = bindTimeout;
  }

  public void setOutbindTimeout(final long outbindTimeout) {
    this.outbindTimeout = outbindTimeout;
  }

  public void setEnquireLinkTimer(final int enquireLinkTimer) {
    this.enquireLinkTimer = enquireLinkTimer;
  }

  public void setInitiationTimer(final int initiationTimer) {
    this.initiationTimer = initiationTimer;
  }

  public void setInterfaceVersion(final InterfaceVersion interfaceVersion) {
    this.interfaceVersion = interfaceVersion;
  }

  @Override
  public void run() {
    try {
      sessionListener = new OutboundSMPPServerSessionListener(port);
      sessionListener.setSessionStateListener(new SessionStateListenerImpl());
      sessionListener.setPduProcessorDegree(processorDegree);
      sessionListener.setInitiationTimer(initiationTimer);
      log.info("Outbound server listening on port {}", port);
      started.countDown();
      while (running) {
        SMPPOutboundServerSession serverSession = sessionListener.accept();
        log.info("Accepting connection for session {}", serverSession.getSessionId());
        serverSession.setInterfaceVersion(interfaceVersion);
        serverSession.setEnquireLinkTimer(enquireLinkTimer);
        sessions.put(serverSession.getSessionId(), serverSession);
        serverSession.setOutboundServerMessageReceiverListener(this);

        BindParameter bindParameter = new BindParameter(BindType.BIND_TRX, "emse_system_id", "secret",
            "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null, interfaceVersion);
        waitOutbindExecService.execute(new WaitOutbindTask(serverSession, outbindTimeout, bindParameter, bindTimeout));
      }
    } catch (SocketException e) {
      log.info("Listen socket: {}", e.getMessage());
    } catch (IOException e) {
      log.error("I/O error occurred", e);
    } finally {
    }
    waitOutbindExecService.shutdown();
    scheduledExecService.shutdown();
  }

  public void waitStarted() throws InterruptedException {
    started.await();
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
  public void onAcceptDeliverSm(final DeliverSm deliverSm, final SMPPOutboundServerSession source) throws ProcessRequestException {
    log.info("Received deliver_sm from {} to {}", deliverSm.getSourceAddr(), deliverSm.getDestAddress());
    increment("deliver_sm");
  }

  public void closeBoundSessions() {
    for (SMPPOutboundServerSession session : sessions.values()) {
      if (session.getSessionState().isBound()) {
        session.close();
      }
    }
  }

  public void logSessions() {
    for (SMPPOutboundServerSession session : sessions.values()) {
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
    @Override
    public void onStateChange(SessionState newState, SessionState oldState, Session source) {
      log.debug("New state of session {} is {}", source.getSessionId(), newState);
    }
  }

  private class CloseTask implements Runnable {
    private SMPPServerSession serverSession;

    public CloseTask(SMPPServerSession serverSession) {
      this.serverSession = serverSession;
    }

    @Override
    public void run() {
      serverSession.close();
    }
  }

  private class WaitOutbindTask implements Runnable {
    private SMPPOutboundServerSession serverSession;
    private BindParameter bindParameter;
    private long outbindTimeout;
    private long bindTimeout;

    public WaitOutbindTask(SMPPOutboundServerSession serverSession, long outbindTimeout, BindParameter bindParameter, long bindTimeout) {
      this.serverSession = serverSession;
      this.outbindTimeout = outbindTimeout;
      this.bindParameter = bindParameter;
      this.bindTimeout = bindTimeout;
    }

    @Override
    public void run() {
      try {
        OutbindRequest outbindRequest = serverSession.waitForOutbind(outbindTimeout);
        log.info("Received outbind for session {}", serverSession.getSessionId());
        log.info("Received outbind {} {}", outbindRequest.getSystemId(), outbindRequest.getPassword());

        // TODO: Check the systemID and password

        String mcSystemId = serverSession.bind(bindParameter, bindTimeout);

        log.info("Received bind_resp with system id {} (MC)", mcSystemId);

      } catch (IllegalStateException e) {
        log.error("System error", e);
      } catch (TimeoutException e) {
        log.warn("Wait for outbind has reach timeout", e);
      } catch (IOException e) {
        log.error("Failed accepting outbind request for session {}", serverSession.getSessionId());
      } catch (Exception e) {
        log.error("Failed accepting outbind request for session", e);
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

    @Override
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
