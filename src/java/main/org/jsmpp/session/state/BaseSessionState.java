package org.jsmpp.session.state;

import org.jsmpp.BindType;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.session.Session;
import org.jsmpp.session.PendingResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSessionState<T extends Session<?>> implements SessionState {
    private static final Logger logger = LoggerFactory.getLogger(BaseSessionState.class);

    private final T session;

    public BaseSessionState(T session) {
        this.session = session;
    }

    public void notifyUnbonded() {
        changeState(State.UNBOUND);
    }

    private void changeState(State state) {
        getSession().changeState(state);
    }

    public void sendBindResp(String systemId, BindType bindType, int sequenceNumber) {
        if (bindType.equals(BindType.BIND_RX)) {
            changeState(State.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TX)) {
            changeState(State.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            changeState(State.BOUND_TRX);
        }
        try {
            pduSender().sendBindResp(bindType.commandId() | SMPPConstant.MASK_CID_RESP, sequenceNumber, systemId);
        } catch (PDUStringException e) {
            logger.error("Failed sending bind response", e);
            // FIXME uud: validate the systemId when the setting up the
            // value, so it never throws PDUStringException on above block
        }
    }

    public void sendEnquireLinkResp(int sequenceNumber) {
        pduSender().sendEnquireLinkResp(sequenceNumber);
    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber) {
        pduSender().sendGenericNack(commandStatus, sequenceNumber);
    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) {
        pduSender().sendHeader(originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
    }

    public void sendUnbindResp(int sequenceNumber) {
        pduSender().sendUnbindResp(SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    protected PDUSender pduSender() {
        return getSession().getPDUSender();
    }

    public PendingResponses pendingResponses() {
        return getSession().getPendingResponses();
    }

    public T getSession() {
        return session;
    }
}
