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
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.PriorityFlag;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPOutboundSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomDecimalMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pmoerenhout
 */
public class OpenAndOutbindExample implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(OpenAndOutbindExample.class);
  private static final String DEFAULT_HOST = "localhost";
  private static final Integer DEFAULT_PORT = 8056;
  private static final String DEFAULT_SYSID = "jsysid";
  private static final String DEFAULT_PASSWORD = "jpwd";
  private static final String DEFAULT_DESTADDR = "62161616";
  private static final String DEFAULT_SOURCEADDR = "1616";

  private static final Long DEFAULT_TRANSACTIONTIMER = 2000L;
  private static final Integer DEFAULT_PROCESSOR_DEGREE = 3;

  private SMPPOutboundSession session = new SMPPOutboundSession();
  private MessageIDGenerator messageIDGenerator = new RandomDecimalMessageIDGenerator();
  private String host;
  private int port;
  private String systemId;
  private String password;
  private String sourceAddr;
  private String destinationAddr;

  private AtomicBoolean exit = new AtomicBoolean();

  private OpenAndOutbindExample(String host, int port,
                               String systemId, String password, String sourceAddr,
                               String destinationAddr, long transactionTimer,
                               int pduProcessorDegree) {
    this.host = host;
    this.port = port;
    this.systemId = systemId;
    this.password = password;
    this.sourceAddr = sourceAddr;
    this.destinationAddr = destinationAddr;
    session.setPduProcessorDegree(pduProcessorDegree);
    session.setTransactionTimer(transactionTimer);
  }

  public static void main(String[] args) {

    String host = System.getProperty("jsmpp.client.host", DEFAULT_HOST);
    String systemId = System.getProperty("jsmpp.client.systemId", DEFAULT_SYSID);
    String password = System.getProperty("jsmpp.client.password", DEFAULT_PASSWORD);
    String sourceAddr = System.getProperty("jsmpp.client.sourceAddr", DEFAULT_SOURCEADDR);
    String destinationAddr = System.getProperty("jsmpp.client.destinationAddr", DEFAULT_DESTADDR);

    int port;
    try {
      port = Integer.parseInt(System.getProperty("jsmpp.client.port", DEFAULT_PORT.toString()));
    }
    catch (NumberFormatException e) {
      port = DEFAULT_PORT;
    }

    long transactionTimer;
    try {
      transactionTimer = Integer
          .parseInt(System.getProperty("jsmpp.client.transactionTimer", DEFAULT_TRANSACTIONTIMER.toString()));
    }
    catch (NumberFormatException e) {
      transactionTimer = DEFAULT_TRANSACTIONTIMER;
    }

    int processorDegree;
    try {
      processorDegree = Integer
          .parseInt(System.getProperty("jsmpp.server.procDegree", DEFAULT_PROCESSOR_DEGREE.toString()));
    }
    catch (NumberFormatException e) {
      processorDegree = DEFAULT_PROCESSOR_DEGREE;
    }

    LOG.info("Processor degree: " + processorDegree);
    OpenAndOutbindExample openAndOutbindExample = new OpenAndOutbindExample(host, port, systemId, password,
        sourceAddr, destinationAddr, transactionTimer, processorDegree);
    openAndOutbindExample.run();
  }

  private void shutdown() {
    exit.set(true);
  }

  public void run() {
    try {
      session.setEnquireLinkTimer(30000);
      session.setTransactionTimer(2000);
      session.addSessionStateListener(new SessionStateListenerImpl());

      LOG.info("Connect and outbind to {} port {}", host, port);
      BindRequest bindRequest = session.connectAndOutbind(host, port, systemId, password);
      LOG.info("Received bind request system_id:'{}' password:'{}", bindRequest.getSystemId(), bindRequest.getPassword());

      try {
        bindRequest.accept("sys", InterfaceVersion.IF_34);
      }
      catch (PDUStringException e) {
        LOG.error("Invalid system id", e);
        bindRequest.reject(SMPPConstant.STAT_ESME_RSYSERR);
      }

    }
    catch (IOException e) {
      LOG.error("Failed initialize connection, outbind, or bind", e);
      return;
    }

    int deliverSmCount = 0;
    while (!exit.get()) {
      /* now send some deliver_sm receipts to the ESME */
      deliverSmCount++;
      try {
        MessageId messageId = messageIDGenerator.newMessageId();
        DeliveryReceipt delRec = new DeliveryReceipt(messageId.getValue(), 1, 1, new Date(),
            new Date(), DeliveryReceiptState.DELIVRD, "000", "#" + deliverSmCount);
        session.deliverShortMessage("cm", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, sourceAddr,
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, destinationAddr,
            new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT),
            (byte) 0x00, PriorityFlag.GsmSms.NORMAL.value(), new RegisteredDelivery(0),
            DataCodings.ZERO, delRec.toString().getBytes(StandardCharsets.ISO_8859_1));
        LOG.info("The deliver_sm request #{} was sent", deliverSmCount);
      }
      catch (IllegalStateException e) {
        LOG.error("IllegalStateException error", e);
      }
      catch (PDUException e) {
        LOG.error("PDUException error", e);
      }
      catch (ResponseTimeoutException e) {
        LOG.warn("Response reached timeout", e);
      }
      catch (InvalidResponseException e) {
        LOG.warn("Invalid response received", e);
      }
      catch (NegativeResponseException e) {
        LOG.warn("Negative response received", e);
      }
      catch (IOException e) {
        LOG.warn("I/O exception", e);
      }

      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        LOG.error("SMPP Server simulator was interrupted", e);
        //re-interrupt the current thread
        Thread.currentThread().interrupt();
      }

      if (!session.getSessionState().isBound()) {
        shutdown();
      }
    }
    LOG.info("Outbind session ended");
    session.unbindAndClose();
  }

  private class SessionStateListenerImpl implements SessionStateListener {
    public void onStateChange(SessionState newState, SessionState oldState, Session source) {
      LOG.info("Session state changed from {} to {}", oldState, newState);
    }
  }

}
