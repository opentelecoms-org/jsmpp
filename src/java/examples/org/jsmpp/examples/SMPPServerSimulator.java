package org.jsmpp.examples;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SMPPServerSimulator implements Runnable, ServerMessageReceiverListener {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSimulator.class);
    private final ExecutorService execService = Executors.newFixedThreadPool(5);
    private final MessageIDGenerator messageIDGenerator = new RandomMessageIDGenerator();
    private int port;
    
    public SMPPServerSimulator(int port) {
        this.port = port;
    }
    
    public void run() {
        try {
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);
            
            logger.info("Listening on port {}", port);
            while (true) {
                SMPPServerSession serverSession = sessionListener.accept();
                logger.info("Accepting connection for session {}", serverSession.getSessionId());
                serverSession.setMessageReceiverListener(this);
                execService.execute(new WaitBindTask(serverSession));
            }
        } catch (IOException e) {
            logger.error("IO error occured", e);
        }
    }
    
    public QuerySmResult onAcceptQuerySm(QuerySm querySm,
            SMPPServerSession source) throws ProcessRequestException {
        logger.info("Accepting query sm, but not implemented");
        return null;
    }
    
    public MessageId onAcceptSubmitSm(SubmitSm submitSm,
            SMPPServerSession source) throws ProcessRequestException {
        MessageId messageId = messageIDGenerator.newMessageId();
        logger.debug("Receiving submit_sm {}, and return message id {}", new String(submitSm.getShortMessage()), messageId.getValue());
        return messageId;
    }
    
    private class WaitBindTask implements Runnable {
        private final SMPPServerSession serverSession;
        
        public WaitBindTask(SMPPServerSession serverSession) {
            this.serverSession = serverSession;
        }

        public void run() {
            try {
                BindRequest bindRequest = serverSession.waitForBind(1000);
                logger.info("Accepting bind for session {}", serverSession.getSessionId());
                try {
                    bindRequest.accept("sys");
                } catch (IOException e) {
                    logger.error("Failed accepting bind request for session {}", serverSession.getSessionId());
                }
            } catch (IllegalStateException e) {
                logger.error("System error", e);
            } catch (TimeoutException e) {
                logger.warn("Wait for bind has reach timeout", e);
            }
        }
    }
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        SMPPServerSimulator smppServerSim = new SMPPServerSimulator(8056);
        smppServerSim.run();
    }
}
