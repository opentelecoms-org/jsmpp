package org.jsmpp.extra;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.bean.Command;

/**
 * This class is utility that able wait for a response for specified timeout.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class PendingResponse<T extends Command> {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    
    private long timeout;
    private T response;
    private InvalidResponseException illegalResponseException;

    /**
     * Construct with specified timeout.
     * 
     * @param timeout the timeout in milliseconds.
     */
    public PendingResponse(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Check whether if we already received the response.
     * 
     * @return {@code true} if the response is already received.
     */
    private boolean isDoneResponse() {
        return response != null;
    }

    /**
     * Done with valid response and notify that response already received.
     * 
     * @param response is the response.
     * @throws IllegalArgumentException thrown if response is null.
     */
    public void done(T response) throws IllegalArgumentException {
        lock.lock();
        try {
            if (response != null) {
                this.response = response;
                condition.signal();
            } else {
                throw new IllegalArgumentException("response cannot be null");
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Done with invalid response (negative response/non OK command_status).
     *  
     * @param e is the {@link InvalidResponseException}.
     */
    public void doneWithInvalidResponse(InvalidResponseException e) {
        lock.lock();
        try {
            illegalResponseException = e;
            condition.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get the response.
     * 
     * @return the response.
     */
    public T getResponse() {
        lock.lock();
        try {
            return response;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Wait until response received or timeout already reached.
     * 
     * @throws ResponseTimeoutException if timeout reached.
     * @throws InvalidResponseException if received invalid response.
     */
    public void waitDone() throws ResponseTimeoutException,
            InvalidResponseException {
        lock.lock();
        try {
            if (!isDoneResponse()) {
                try {
                    condition.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (illegalResponseException != null) {
                throw illegalResponseException;
            }
            
            if (!isDoneResponse()) {
                throw new ResponseTimeoutException("No response after " + timeout
                        + " millis");
            }
        } finally {
            lock.unlock();
        }
    }
}
