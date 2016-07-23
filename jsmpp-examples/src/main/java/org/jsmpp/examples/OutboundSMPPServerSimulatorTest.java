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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.OutbindRequest;
import org.jsmpp.session.OutboundSMPPServerSessionListener;
import org.jsmpp.session.OutboundServerMessageReceiverListener;
import org.jsmpp.session.SMPPOutboundServerSession;
import org.jsmpp.session.ServerResponseDeliveryAdapter;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 */
public class OutboundSMPPServerSimulatorTest extends ServerResponseDeliveryAdapter
    implements Runnable, OutboundServerMessageReceiverListener {
  private static final Integer DEFAULT_PORT = 8056;
  private static final Logger logger = LoggerFactory.getLogger(OutboundSMPPServerSimulatorTest.class);
  private final ExecutorService execService = Executors.newFixedThreadPool(100);
  private final ExecutorService execServiceDelReceipt = Executors.newFixedThreadPool(100);
  private int port;

  private AtomicBoolean exit = new AtomicBoolean();

  OutboundSMPPServerSimulatorTest(int port) {
    this.port = port;
  }

  public static void main(String[] args) {
    int port;
    try {
      port = Integer.parseInt(System.getProperty("jsmpp.simulator.port", DEFAULT_PORT.toString()));
    }
    catch (NumberFormatException e) {
      port = DEFAULT_PORT;
    }
    BasicConfigurator.configure();
    OutboundSMPPServerSimulatorTest smppServerSim = new OutboundSMPPServerSimulatorTest(port);
    logger.info("run {}", smppServerSim);
    smppServerSim.run();
  }

  private void shutdown() {
    exit.set(true);
  }

  public void run() {
    try {
      logger.info("OutboundSMPPServerSessionListener {}", port);
      OutboundSMPPServerSessionListener sessionListener = new OutboundSMPPServerSessionListener(port);

      logger.info("Listening on port {}", port);
      while (!exit.get()) {
        final SMPPOutboundServerSession outboundServerSession = sessionListener.accept();
        logger.info("Accepting connection from {} for session {}", outboundServerSession.getInetAddress(),
            outboundServerSession.getSessionId());
        outboundServerSession.setEnquireLinkTimer(30000);
        outboundServerSession.addSessionStateListener(new SessionStateListenerImpl());
        outboundServerSession.setOutboundServerMessageReceiverListener(this);

        execService.execute(new OutboundSMPPServerSimulatorTest.WaitOutbindTask(outboundServerSession));

        try {
          Thread.sleep(60 * 60 * 1000L);
        }
        catch (InterruptedException e) {
          logger.info("Thread was interrupted");
          shutdown();
        }
        outboundServerSession.close();
      }

      logger.info("close listener {}", sessionListener);
      sessionListener.close();
      execService.shutdown();
    }
    catch (IOException e) {
      logger.error("IO error occurred", e);
    }
  }

  public void onAcceptDeliverSm(DeliverSm deliverSm, SMPPOutboundServerSession source)
      throws ProcessRequestException {
    logger.info("deliver_sm: {} {} => {} {}", deliverSm.getSequenceNumber(), deliverSm.getSourceAddr(),
        deliverSm.getDestAddress(), new String(deliverSm.getShortMessage()));
  }

  private static class WaitOutbindTask implements Runnable {
    private final SMPPOutboundServerSession serverSession;

    WaitOutbindTask(SMPPOutboundServerSession serverSession) {
      this.serverSession = serverSession;
    }

    public void run() {
      try {
        logger.info("Waiting for outbind request");
        OutbindRequest outbindRequest = serverSession.waitForOutbind(15000);
        logger.info("Received outbind for session {}, systemid {}, password {}", serverSession.getSessionId(),
            outbindRequest.getSystemId(), outbindRequest.getPassword());

        serverSession.bind(new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN,
            NumberingPlanIndicator.UNKNOWN, null), 60000);
      }
      catch (IllegalStateException e) {
        logger.error("System error", e);
      }
      catch (TimeoutException e) {
        logger.warn("Wait for outbind has reached timeout", e);
      }
      catch (IOException e) {
        logger.warn("IO exception", e);
      }
    }
  }

  private class SessionStateListenerImpl implements SessionStateListener {
    public void onStateChange(SessionState newState, SessionState oldState, Session source) {
      logger.info("Session state changed from " + oldState + " to " + newState);
    }
  }

}
