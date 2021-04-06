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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DataSm;
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
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSmppClient implements Runnable, MessageReceiverListener {

  private static final Logger log = LoggerFactory.getLogger(TestSmppClient.class);

  private static final String NOT_IMPLEMENTED = "the command is not implemented";
  private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
  private final AbsoluteTimeFormatter timeFormatter = new AbsoluteTimeFormatter();
  private ExecutorService waitBindExecService = Executors.newFixedThreadPool(5);
  private ScheduledExecutorService scheduledExecService = Executors.newScheduledThreadPool(5);
  private int processorDegree;
  private int port;
  private int enquireLinkTimer;

  private Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

  public TestSmppClient(final int port, final int processorDegree) {
    this.port = port;
    this.processorDegree = processorDegree;
  }

  public void setEnquireLinkTimer(final int enquireLinkTimer) {
    this.enquireLinkTimer = enquireLinkTimer;
  }

  @Override
  public void run() {
    try {
      SMPPSession session = new SMPPSession();
      session.setPduProcessorDegree(processorDegree);
      session.setEnquireLinkTimer(enquireLinkTimer);
      session.setMessageReceiverListener(this);

      // BindParameter bindParameter = new BindParameter();

      session.connect("localhost", port);


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
//    try {
//      // session
//    } catch (IOException e) {
//      log.error("I/O error occurred on close", e);
//    }
  }

  @Override
  public DataSmResult onAcceptDataSm(final DataSm dataSm, final Session source) throws ProcessRequestException {
    return null;
  }

  @Override
  public void onAcceptDeliverSm(final DeliverSm deliverSm) throws ProcessRequestException {

  }

  @Override
  public void onAcceptAlertNotification(final AlertNotification alertNotification) {
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
