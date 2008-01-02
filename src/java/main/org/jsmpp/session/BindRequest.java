package org.jsmpp.session;

import java.io.IOException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.BindType;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.Bind;
import org.jsmpp.extra.ProcessRequestException;

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
    
    private final ServerResponseHandler responseHandler;
    public BindRequest(int sequenceNumber, BindType bindType, String systemId, String password, 
            String systemType, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, 
            String addressRange, ServerResponseHandler responseHandler) {
        this.originalSequenceNumber = sequenceNumber;
        bindParam = new BindParameter(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange);
        this.responseHandler = responseHandler;
    }
    
    public BindRequest(Bind bind, ServerResponseHandler responseHandler) {
        this(bind.getSequenceNumber(), BindType.valueOf(bind.getCommandId()), bind.getSystemId(), 
                bind.getPassword(), bind.getSystemType(), 
                TypeOfNumber.valueOf(bind.getAddrTon()), 
                NumberingPlanIndicator.valueOf(bind.getAddrNpi()), 
                bind.getAddressRange(), responseHandler);
    }
    
    public BindParameter getBindParameter() {
        return bindParam;
    }
    
    /**
     * Accept the bind request.
     * 
     * @throws IllegalStateException if the acceptance or rejection has been made.
     * @throws IOException is the connection already closed.
     * @see #reject(ProcessRequestException)
     */
    public void accept() throws IllegalStateException, IOException {
        lock.lock();
        try {
            if (!done) {
                done = true;
                try {
                    responseHandler.sendBindResp(bindParam.getBindType(), originalSequenceNumber);
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
     * @param errorCode is the reason of rejection.
     * @throws IllegalStateException if the acceptance or rejection has been made.
     * @throws IOException if the connection already closed.
     * @see {@link #accept()}
     */
    public void reject(int errorCode) throws IllegalStateException, IOException {
        lock.lock();
        try {
            if (done) {
                throw new IllegalStateException("Response already initiated");
            } else {
                done = true;
                try {
                    responseHandler.sendNegativeResponse(bindParam.getBindType().commandId(), errorCode, originalSequenceNumber);
                } finally {
                    condition.signal();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
