package org.jsmpp.session;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.PDU;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PDUReaderWorker extends Thread {
    private final Session<?> session;
    static final Logger logger = LoggerFactory.getLogger(PDUReaderWorker.class);

    PDUReaderWorker(Session<?> session) {
        this.session = session;
    }

    @Override
    public void run() {
        logger.info("Starting PDUReaderWorker");
        while (this.session.isConnected()) {
            readPDU();
        }
        this.session.close();
        logger.info("PDUReaderWorker stop");
    }

    private void readPDU() {
        try {
            PDU pdu = this.session.pduReader.readPDU();
            this.session.notifyActivity();
            this.session.state.process(pdu);
        } catch (InvalidCommandLengthException e) {
            logger.warn("Receive invalid command length", e);
            try {
                this.session.pduSender.sendGenericNack(SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
            } catch (RuntimeException ee) {
                logger.warn("Failed sending generic nack", ee);
            }
            this.session.unbindAndClose();
        } catch (SocketTimeoutException e) {
            notifyNoActivity();
        } catch (IOException e) {
            this.session.close();
        }
    }

    /**
     * Notify for no activity.
     */
    private void notifyNoActivity() {
        logger.debug("No activity notified");
        session.enquireLinkSender.sendEnquireLink();
    }
}