package org.jsmpp.session;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUSender;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EnquireLinkSender extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(EnquireLinkSender.class);

    private final AtomicBoolean sendingEnquireLink = new AtomicBoolean(false);
    BaseSession session;
    PDUSender pduSender;

    public EnquireLinkSender(BaseSession session, PDUSender pduSender) {
        this.session = session;
        this.pduSender = pduSender;
    }

    @Override
    public void run() {
        ServerSession.logger.info("Starting EnquireLinkSender");
        while (this.session.isConnected()) {
            while (!sendingEnquireLink.compareAndSet(true, false) && this.session.isConnected()) {
                synchronized (sendingEnquireLink) {
                    try {
                        sendingEnquireLink.wait(500);
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (!this.session.isConnected()) {
                break;
            }
            try {
                sendEnquireLink();
            } catch (ResponseTimeoutException e) {
                this.session.close();
            } catch (InvalidResponseException e) {
                // lets unbind gracefully
                this.session.unbindAndClose();
            } catch (RuntimeException e) {
                this.session.close();
            }
        }
        ServerSession.logger.info("EnquireLinkSender stop");
    }

    /**
     * This method will send enquire link asynchronously.
     */
    public void enquireLink() {
        if (sendingEnquireLink.compareAndSet(false, true)) {
            synchronized (sendingEnquireLink) {
                sendingEnquireLink.notify();
            }
        }
    }

    /**
     * Ensure we have proper link.
     * 
     * @throws ResponseTimeoutException
     *             if there is no valid response after defined millisecond.
     * @throws InvalidResponseException
     *             if there is invalid response found.
     */
    private void sendEnquireLink() throws ResponseTimeoutException, InvalidResponseException {
        PendingResponse<EnquireLinkResp> pendingResp = this.session.pendingResponses.add(EnquireLinkResp.class);
        try {
            logger.debug("Sending enquire_link");
            pduSender.sendEnquireLink(pendingResp.getSequenceNumber());
        } catch (RuntimeException e) {
            logger.error("Failed sending enquire link", e);
            this.session.pendingResponses.remove(pendingResp);
            throw e;
        }
        this.session.pendingResponses.tryWait(pendingResp);
    }
}