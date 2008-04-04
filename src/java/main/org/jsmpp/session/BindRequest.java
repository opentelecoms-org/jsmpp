package org.jsmpp.session;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.BindType;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.Bind;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.state.SessionState;

/**
 * @author uudashr
 * 
 */
public class BindRequest {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private final BindParameter bindParam;
    private final int originalSequenceNumber;
    private boolean done;
    SessionState<?> state;

    public BindRequest(int sequenceNumber, BindType bindType, String systemId, String password, String systemType, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange, SessionState<?> state) {
        this.originalSequenceNumber = sequenceNumber;
        this.state = state;
        bindParam = new BindParameter(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange);
    }

    public BindRequest(Bind bind, SessionState<?> state) {
        this(bind.getSequenceNumber(), BindType.valueOf(bind.getCommandId()), bind.getSystemId(), bind.getPassword(), bind.getSystemType(), TypeOfNumber.valueOf(bind.getAddrTon()), NumberingPlanIndicator.valueOf(bind.getAddrNpi()), bind.getAddressRange(), state);
    }

    public BindParameter getBindParameter() {
        return bindParam;
    }

    /**
     * Accept the bind request.
     * 
     * @param systemId
     *            is the system identifier that will be send to ESME.
     * @throws IllegalStateException
     *             if the acceptance or rejection has been made.
     * @see #reject(ProcessRequestException)
     */
    public void accept(String systemId) throws IllegalStateException {
        lock.lock();
        try {
            if (!done) {
                done = true;
                try {
                    state.sendBindResp(systemId, bindParam.getBindType(), originalSequenceNumber);
                } finally {
                    condition.signal();
                }
            } else {
                throw new IllegalStateException("Response already initiated");
            }
        } finally {
            lock.unlock();
        }
        done = true;
    }

    /**
     * Reject the bind request.
     * 
     * @param errorCode
     *            is the reason of rejection.
     * @throws IllegalStateException
     *             if the acceptance or rejection has been made.
     * @see {@link #accept()}
     */
    public void reject(int errorCode) throws IllegalStateException {
        lock.lock();
        try {
            if (done) {
                throw new IllegalStateException("Response already initiated");
            }
            done = true;
            try {
                state.sendNegativeResponse(bindParam.getBindType().commandId(), errorCode, originalSequenceNumber);
            } finally {
                condition.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
