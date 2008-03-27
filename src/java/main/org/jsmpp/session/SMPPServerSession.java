package org.jsmpp.session;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.SessionStateFinder;
import org.jsmpp.session.state.server.ServerSessionStateFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSession extends BaseSMPPSession {
    static final Logger logger = LoggerFactory.getLogger(SMPPServerSession.class);

    private SessionStateListener sessionStateListener;

    private final SessionStateFinder<ServerResponseHandler> sessionStateFinder;

    final SMPPServerSessionResponseHandler responseHandler = new SMPPServerSessionResponseHandler(this);
    ServerMessageReceiverListener messageReceiverListener;
    BindRequestReceiver bindRequestReceiver;

    public SMPPServerSession(Connection conn, SessionStateListener sessionStateListener, ServerMessageReceiverListener messageReceiverListener) {
        super(conn);
        pduSender = new DefaultPDUSender(conn.getOutputStream());
        pduReader = new DefaultPDUReader(conn.getInputStream());

        sessionStateFinder = new ServerSessionStateFinder();
        state = sessionStateFinder.getSessionState(responseHandler, SessionState.CLOSED);
        changeState(SessionState.OPEN);
        this.sessionStateListener = sessionStateListener;
        this.messageReceiverListener = messageReceiverListener;
        enquireLinkSender = new EnquireLinkSender(this, pduSender);
        bindRequestReceiver = new BindRequestReceiver(responseHandler);
    }

    public BindRequest waitForBind(long timeout) throws IllegalStateException, TimeoutException {
        if (getSessionState().equals(SessionState.OPEN)) {
            new PDUReaderWorker(this).start();
            try {
                return bindRequestReceiver.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw e;
            }
        }
        throw new IllegalStateException("waitForBind() should be invoked on OPEN state, actual state is " + getSessionState());
    }

    public void deliverShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<DeliverSmResp> pendingResp = pendingResponses.add(DeliverSmResp.class);
        try {
            pduSender.sendDeliverSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, registeredDelivery, dataCoding, shortMessage, params);
        } catch (IOException e) {
            pendingResponses.remove(pendingResp);
            logger.error("Failed deliver short message", e);
            close();
            throw e;
        }
        pendingResponses.wait(pendingResp);
    }

    @Override
    synchronized void changeState(SessionState newState) {
        if (!state.getSessionState().equals(newState)) {
            final SessionState oldState = state.getSessionState();
            // change the session state processor
            state = sessionStateFinder.getSessionState(responseHandler, newState);
            if (newState.isBound()) {
                enquireLinkSender.start();
            }
            fireChangeState(newState, oldState);
        }
    }

    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }

    public void setMessageReceiverListener(ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    private void fireChangeState(SessionState newState, SessionState oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, this);
        } else {
            logger.warn("SessionStateListener is null");
        }
    }
}
