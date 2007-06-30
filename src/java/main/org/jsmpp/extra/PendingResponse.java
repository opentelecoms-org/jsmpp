package org.jsmpp.extra;


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
    private long timeout;
    private T response;
    private InvalidResponseException illegalResponseException;

    /**
     * Construct with specified timeout.
     * 
     * @param timeout is the timeout in millis.
     */
    public PendingResponse(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Check whether if we already receive response.
     * 
     * @return <tt>true</tt> if response already receive.
     */
    public boolean isDoneResponse() {
        return response != null;
    }

    /**
     * Done with valid response and notify that response already received.
     * 
     * @param response is the response.
     * @throws IllegalArgumentException thrown if response is null.
     */
    public synchronized void done(T response) throws IllegalArgumentException {
        if (response != null) {
            this.response = response;
            notify();
        } else {
            throw new IllegalArgumentException("response cannot be null");
        }
    }

    /**
     * Done with invalid response (negative response/non OK command_status).
     *  
     * @param e is the {@link InvalidResponseException}.
     */
    public synchronized void doneWithInvalidResponse(InvalidResponseException e) {
        illegalResponseException = e;
        notify();
    }

    /**
     * Get the response.
     * 
     * @return the response.
     */
    public T getResponse() {
        return response;
    }

    /**
     * Wait until response receive or timeout already reach.
     * 
     * @throws ResponseTimeoutException if timeout reach.
     * @throws InvalidResponseException if receive invalid response.
     */
    public synchronized void waitDone() throws ResponseTimeoutException,
            InvalidResponseException {
        try {
            wait(timeout);
        } catch (InterruptedException e) {
        }

        if (illegalResponseException != null)
            throw illegalResponseException;

        if (!isDoneResponse())
            throw new ResponseTimeoutException("No response after " + timeout
                    + " millis");
    }
}
