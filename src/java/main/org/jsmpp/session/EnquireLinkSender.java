package org.jsmpp.session;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnquireLinkSender extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EnquireLinkSender.class);
    private boolean running = true;
    Session<?> session;

    public EnquireLinkSender(Session<?> session) {
        this.session = session;
    }

    @Override
    public void run() {
        ServerSession.logger.info("Starting EnquireLinkSender");
        while (running) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            if (session.isBound()) {
                sendEnquireLink();
            }
        }
        ServerSession.logger.info("EnquireLinkSender stop");
    }

    synchronized void sendEnquireLink() throws ResponseTimeoutException, InvalidResponseException {
        PendingResponse<EnquireLinkResp> pendingResp = this.session.pendingResponses.add(EnquireLinkResp.class);
        try {
            logger.debug("Sending enquire_link");
            session.getPDUSender().sendEnquireLink(pendingResp.getSequenceNumber());
        } catch (RuntimeException e) {
            logger.error("Failed sending enquire link", e);
            this.session.pendingResponses.remove(pendingResp);
            throw e;
        }
        this.session.pendingResponses.tryWait(pendingResp);
    }

    public void shutdown() {
        running = false;
    }
}