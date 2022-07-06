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
package org.jsmpp.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SubmitSmResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This stress client is an example of submit bulk messages asynchronously.
 *
 * <table border="1" summary="Properties table">
 *     <tr><td><strong>Name</strong></td><td><strong>Description</strong></td><td><strong>Default value</strong></td></tr>
 *     <tr><td>jsmpp.client.host</td><td>Server host address</td><td>localhost</td></tr>
 *     <tr><td>jsmpp.client.port</td><td>Server port</td><td>8056</td></tr>
 *     <tr><td>jsmpp.client.systemId</td><td>System Identifier</td><td>j</td></tr>
 *     <tr><td>jsmpp.client.password</td><td>Password</td><td>jpwd</td></tr>
 *     <tr><td>jsmpp.client.sourceAddr</td><td>Submit Source Address</td><td>1616</td></tr>
 *     <tr><td>jsmpp.client.destinationAddr</td><td>Submit Destination Address</td><td>62161616</td>
 *     <tr><td>jsmpp.client.transactionTimer</td><td>Transaction timer</td><td>2000</td></tr>
 *     <tr><td>jsmpp.client.bulkSize</td><td>Amount of bulk messages</td><td>100000</td></tr>
 *     <tr><td>jsmpp.client.procDegree</td><td>Max parallel processor for PDU reading</td><td>3</td></tr>
 *     <tr><td>jsmpp.client.maxOutstanding</td><td>Maximum outstanding messages</td><td>10</td></tr>
 * </table>
 *
 * @author uudashr
 */
public class StressClient implements Runnable {
  private static final Logger log = LoggerFactory.getLogger(StressClient.class);

  private static final String DEFAULT_SYSID = "j";
  private static final String DEFAULT_PASSWORD = "jpwd";
  private static final String DEFAULT_SOURCEADDR = "1616";
  private static final String DEFAULT_DESTADDR = "62161616";
  private static final String DEFAULT_HOST = "localhost";
  private static final Integer DEFAULT_PORT = 8056;
  private static final Long DEFAULT_TRANSACTIONTIMER = 2000L;
  private static final Integer DEFAULT_BULK_SIZE = 100000;
  private static final Integer DEFAULT_PROCESSOR_DEGREE = 10;
  private static final Integer DEFAULT_MAX_OUTSTANDING = 100;
  private final int id;
  private final String host;
  private final int port;
  private final int bulkSize;
  private final String systemId;
  private final String password;
  private final String sourceAddr;
  private final String destinationAddr;
  private AtomicInteger requestCounter = new AtomicInteger();
  private AtomicInteger totalRequestCounter = new AtomicInteger();
  private AtomicInteger responseCounter = new AtomicInteger();
  private AtomicInteger totalResponseCounter = new AtomicInteger();
  private AtomicLong maxDelay = new AtomicLong();
  private ExecutorService execService;
  private SMPPSession smppSession = new SMPPSession();
  private AtomicBoolean exit = new AtomicBoolean();

  public StressClient(int id, String host, int port, int bulkSize,
                      String systemId, String password, String sourceAddr,
                      String destinationAddr, long transactionTimer,
                      int pduProcessorDegree, int maxOutstanding) {
    this.id = id;
    this.host = host;
    this.port = port;
    this.bulkSize = bulkSize;
    this.systemId = systemId;
    this.password = password;
    this.sourceAddr = sourceAddr;
    this.destinationAddr = destinationAddr;
    smppSession.setPduProcessorDegree(pduProcessorDegree);
    smppSession.setTransactionTimer(transactionTimer);
    execService = Executors.newFixedThreadPool(maxOutstanding);
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
    } catch (NumberFormatException e) {
      port = DEFAULT_PORT;
    }

    long transactionTimer;
    try {
      transactionTimer = Integer.parseInt(System.getProperty("jsmpp.client.transactionTimer", DEFAULT_TRANSACTIONTIMER.toString()));
    } catch (NumberFormatException e) {
      transactionTimer = DEFAULT_TRANSACTIONTIMER;
    }

    int bulkSize;
    try {
      bulkSize = Integer.parseInt(System.getProperty("jsmpp.client.bulkSize", DEFAULT_BULK_SIZE.toString()));
    } catch (NumberFormatException e) {
      bulkSize = DEFAULT_BULK_SIZE;
    }

    int processorDegree;
    try {
      processorDegree = Integer.parseInt(System.getProperty("jsmpp.client.procDegree", DEFAULT_PROCESSOR_DEGREE.toString()));
    } catch (NumberFormatException e) {
      processorDegree = DEFAULT_PROCESSOR_DEGREE;
    }

    int maxOutstanding;
    try {
      maxOutstanding = Integer.parseInt(System.getProperty("jsmpp.client.maxOutstanding", DEFAULT_MAX_OUTSTANDING.toString()));
    } catch (NumberFormatException e) {
      maxOutstanding = DEFAULT_MAX_OUTSTANDING;
    }

    log.info("Target server {}:{}", host, port);
    log.info("System ID: {}", systemId);
    log.info("Password: {}", password);
    log.info("Source address: {}", sourceAddr);
    log.info("Destination address: {}", destinationAddr);
    log.info("Transaction timer: {}", transactionTimer);
    log.info("Bulk size: {}", bulkSize);
    log.info("Max outstanding: {}", maxOutstanding);
    log.info("Processor degree: {}", processorDegree);

    StressClient stressClient = new StressClient(0, host, port, bulkSize,
        systemId, password, sourceAddr, destinationAddr,
        transactionTimer, processorDegree, maxOutstanding);
    stressClient.run();
  }

  private void shutdown() {
    execService.shutdown();
    exit.set(true);
  }

  @Override
  public void run() {

    new TrafficWatcherThread().start();

    try {
      final BindParameter bindParameter = new BindParameter(BindType.BIND_TRX, systemId,
          password, "CLN", TypeOfNumber.UNKNOWN,
          NumberingPlanIndicator.UNKNOWN, null, InterfaceVersion.IF_50);
      smppSession.connectAndBind(host, port, bindParameter);
//      smppSession.connectAndBind(host, port, BindType.BIND_TRX, systemId,
//          password, "cln", TypeOfNumber.UNKNOWN,
//          NumberingPlanIndicator.UNKNOWN, null);
      log.info("Bound to {}:{}", host, port);

      log.info("Starting to send {} bulk messages", bulkSize);
      for (int i = 0; i < bulkSize && !exit.get(); i++) {
        execService.execute(newSendTaskData("Hello " + id + " idx=" + i));
      }
      while (!exit.get()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
      }
      log.info("Done");
      smppSession.unbindAndClose();
    } catch (IOException e) {
      log.error("Failed initialize connection or bind", e);
    }
  }

  private Runnable newSendTask(final String message) {
    return new Runnable() {
      public void run() {
        try {
          requestCounter.incrementAndGet();
          long startTime = System.currentTimeMillis();
          SubmitSmResult submitSmResult = smppSession.submitShortMessage(null, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, sourceAddr,
              TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, destinationAddr,
              new ESMClass(), (byte) 0, (byte) 0,
              null, null, new RegisteredDelivery(0),
              (byte) 0,
              DataCodings.ZERO,
              (byte) 0, message.getBytes());
          log.info("There are {} unacknowledged requests", smppSession.getUnacknowledgedRequests());

          OptionalParameter.Congestion_state congestionState = OptionalParameters.get(OptionalParameter.Congestion_state.class,
              submitSmResult.getOptionalParameters());
          if (congestionState != null) {
            log.info("Remote congestion state: {}", (congestionState.getValue() & 0xff));
          }

          long delay = System.currentTimeMillis() - startTime;
          responseCounter.incrementAndGet();
          if (maxDelay.get() < delay) {
            maxDelay.set(delay);
          }
        } catch (PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException e) {
          log.error("Failed submit short message '" + message + "'", e);
          shutdown();
        }
      }
    };
  }

  private Runnable newSendTaskData(final String message) {
    return new Runnable() {
      public void run() {
        try {
          requestCounter.incrementAndGet();
          long startTime = System.currentTimeMillis();
          OptionalParameter.Message_payload messagePayload = new OptionalParameter.Message_payload(message.getBytes(StandardCharsets.US_ASCII));
          DataSmResult dataSmResult = smppSession.dataShortMessage(null, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, sourceAddr,
              TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, destinationAddr,
              new ESMClass(), new RegisteredDelivery(0),
              DataCodings.ZERO, new OptionalParameter[]{ messagePayload });
          log.info("There are {} unacknowledged requests", smppSession.getUnacknowledgedRequests());

          log.info("dataSmresult {}", dataSmResult.getMessageId());
          log.info("dataSmresult {}", dataSmResult.getOptionalParameters());

          OptionalParameter.Congestion_state congestionState = OptionalParameters.get(OptionalParameter.Congestion_state.class,
              dataSmResult.getOptionalParameters());
          if (congestionState != null) {
            log.info("Remote congestion state: {}", (congestionState.getValue() & 0xff));
          }

          long delay = System.currentTimeMillis() - startTime;
          responseCounter.incrementAndGet();
          if (maxDelay.get() < delay) {
            maxDelay.set(delay);
          }
        } catch (PDUException | ResponseTimeoutException | InvalidResponseException | NegativeResponseException | IOException e) {
          log.error("Failed submit short message '" + message + "'", e);
          shutdown();
        }
      }
    };
  }

  private class TrafficWatcherThread extends Thread {
    @Override
    public void run() {
      log.info("Starting traffic watcher...");
      while (!exit.get()) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          break;
        }
        int requestPerSecond = requestCounter.getAndSet(0);
        int responsePerSecond = responseCounter.getAndSet(0);
        long maxDelayPerSecond = maxDelay.getAndSet(0);
        totalRequestCounter.addAndGet(requestPerSecond);
        int total = totalResponseCounter.addAndGet(responsePerSecond);
        log.info("Request/Response per second: {}/{} of {} maxDelay={}",
            requestPerSecond, responsePerSecond, total, maxDelayPerSecond);
        if (total == bulkSize) {
          shutdown();
        }
      }
    }
  }
}
