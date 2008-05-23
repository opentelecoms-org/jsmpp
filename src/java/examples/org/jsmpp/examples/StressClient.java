package org.jsmpp.examples;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.PropertyConfigurator;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class StressClient implements Runnable {
    private static final String DEFAULT_PASSWORD = "jpwd";
    private static final String DEFAULT_SYSID = "j";
    private static final String DEFAULT_DESTADDR = "62161616";
    private static final String DEFAULT_SOURCEADDR = "1616";
    private static final Logger logger = LoggerFactory.getLogger(StressClient.class);
    private static final String DEFAULT_LOG4J_PATH = "stress/client-log4j.properties";
    private static final String DEFAULT_HOST = "localhost";
    private static final Integer DEFAULT_PORT = 8056;
    private static final Integer DEFAULT_BULK_SIZE = 100000;
    private static final Integer DEFAULT_PROCESSOR_DEGREE = 3;
    private static final Integer DEFAULT_MAX_OUTSTANDING = 1000;
    
    private AtomicInteger requestCounter = new AtomicInteger();
    private AtomicInteger totalRequestCounter = new AtomicInteger();
    private AtomicInteger responseCounter = new AtomicInteger();
    private AtomicInteger totalResponseCounter = new AtomicInteger();
    private ExecutorService execService;
    private String host;
    private int port;
    private int bulkSize;
    private SMPPSession smppSession = new SMPPSession();
    private AtomicBoolean exit = new AtomicBoolean();
    private int id;
    private String systemId;
    private String password;
    private String sourceAddr;
    private String destinationAddr;
    
    public StressClient(int id, String host, int port, int bulkSize,
            String systemId, String password, String sourceAddr,
            String destinationAddr, int pduProcessorDegree, int maxOutstanding) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.bulkSize = bulkSize;
        this.systemId = systemId;
        this.password = password;
        this.sourceAddr = sourceAddr;
        this.destinationAddr = destinationAddr;
        smppSession.setPduProcessorDegree(pduProcessorDegree);
        
        execService = Executors.newFixedThreadPool(maxOutstanding);
    }

    private void shutdown() {
        execService.shutdown();
        exit.set(true);
    }
    
    public void run() {
        try {
            smppSession.connectAndBind(host, port, BindType.BIND_TRX, systemId,
                    password, "cln", TypeOfNumber.UNKNOWN,
                    NumberingPlanIndicator.UNKNOWN, null);
            logger.info("Bound to " + host + ":" + port);
        } catch (IOException e) {
            logger.error("Failed initialize connection or bind", e);
            return;
        }
        new TrafficWatcherThread().start();
        
        logger.info("Starting send " + bulkSize + " bulk message...");
        for (int i = 0; i < bulkSize && !exit.get(); i++) {
            //execService.execute(newSendTask("Hello " + id + " idx=" + i));
            newSendTask("Hello " + id + " idx=" + i).run();
        }
        
        while (!exit.get()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        logger.info("Done");
        smppSession.unbindAndClose();
    }
    
    private Runnable newSendTask(final String message) {
        return new Runnable() {
            public void run() {
                try {
                    requestCounter.incrementAndGet();
                    smppSession.submitShortMessage(null, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, sourceAddr, 
                            TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, destinationAddr, 
                            new ESMClass(), (byte)0, (byte)0, 
                            null, null, new RegisteredDelivery(0), 
                            (byte)0, 
                            new GeneralDataCoding(true, true, MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT), 
                            (byte)0, message.getBytes());
                    responseCounter.incrementAndGet();
                } catch (PDUStringException e) {
                    logger.error("Failed submit short message '" + message + "'", e);
                    shutdown();
                } catch (ResponseTimeoutException e) {
                    logger.error("Failed submit short message '" + message + "'", e);
                    shutdown();
                } catch (InvalidResponseException e) {
                    logger.error("Failed submit short message '" + message + "'", e);
                    shutdown();
                } catch (NegativeResponseException e) {
                    logger.error("Failed submit short message '" + message + "'", e);
                    shutdown();
                } catch (IOException e) {
                    logger.error("Failed submit short message '" + message + "'", e);
                    shutdown();
                }
            }
        };
    }
    
    private class TrafficWatcherThread extends Thread {
        @Override
        public void run() {
            logger.info("Starting traffic watcher...");
            while (!exit.get()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                int requestPerSecond = requestCounter.getAndSet(0);
                int responsePerSecond = responseCounter.getAndSet(0);
                totalRequestCounter.addAndGet(requestPerSecond);
                int total = totalResponseCounter.addAndGet(responsePerSecond);
                logger.info("Request/Response per second : " + requestPerSecond + "/" + responsePerSecond + " of " + total);
                if (total == bulkSize) {
                    shutdown();
                }
            }
        }
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
        
        int bulkSize;
        try {
            bulkSize = Integer.parseInt(System.getProperty("jsmpp.client.bulksize", DEFAULT_BULK_SIZE.toString()));
        } catch (NumberFormatException e) {
            bulkSize = DEFAULT_BULK_SIZE;
        }
        
        int processorDegree;
        try {
            processorDegree = Integer.parseInt(System.getProperty("jsmpp.client.procdegree", DEFAULT_PROCESSOR_DEGREE.toString()));
        } catch (NumberFormatException e) {
            processorDegree = DEFAULT_PROCESSOR_DEGREE;
        }
        
        int maxOutstanding;
        try {
            maxOutstanding = Integer.parseInt(System.getProperty("jsmpp.client.maxoutstanding", DEFAULT_MAX_OUTSTANDING.toString()));
        } catch (NumberFormatException e) {
            maxOutstanding = DEFAULT_MAX_OUTSTANDING;
        }
        
        String log4jPath = System.getProperty("jsmpp.log4j.path", DEFAULT_LOG4J_PATH);
        PropertyConfigurator.configure(log4jPath);
        
        
        logger.info("Target server {}:{}", host, port);
        logger.info("System ID: {}", systemId);
        logger.info("Password: {}", password);
        logger.info("Source address: " + sourceAddr);
        logger.info("Destination address: " + destinationAddr);
        logger.info("Bulk size: {}", bulkSize);
        logger.info("Max outstanding: {}", maxOutstanding);
        logger.info("Processor degree: {}", processorDegree);
        
        StressClient stressClient = new StressClient(0, host, port, bulkSize,
                systemId, password, sourceAddr, destinationAddr,
                processorDegree, maxOutstanding);
        stressClient.run();
    }
}
