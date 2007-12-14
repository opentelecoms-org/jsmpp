package org.jsmpp.extra.test;

import java.io.File;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class PendingResponseTest extends TestCase {
    private PendingResponse<Command> pendingResponse;
    
    @Override
    protected void setUp() throws Exception {
        pendingResponse = new PendingResponse<Command>(1000);
    }
    
    /**
     * Test {@link PendingResponse} for reached timeout.
     */
    public void testReachTimeout() {
        try {
            notifyDone(1010, pendingResponse);
            pendingResponse.waitDone();
            fail("Timeout should be reached");
        } catch (ResponseTimeoutException e) {
        } catch (InvalidResponseException e) {
            fail("Timeout should be reached");
        }
    }
    
    public void testUnreachTimeout() {
        try {
            notifyDone(90, pendingResponse);
            pendingResponse.waitDone();
        } catch (ResponseTimeoutException e) {
            fail("Should be done with valid response");
        } catch (InvalidResponseException e) {
            fail("Should be done with valid response");
        }
    }
    
    public void testDoneWithInvalidResponse() {
        try {
            notifyInvalidResponse(90, pendingResponse);
            pendingResponse.waitDone();
            fail("Should throw InvalidResponseException");
        } catch (ResponseTimeoutException e) {
            fail("Should throw InvalidResponseException");
        } catch (InvalidResponseException e) {
        }
    }
    
    /**
     * Notify done of a pendingResponse on specified interval.
     * 
     * @param timemillis is interval in millisecond.
     * @param pendingResponse is the pending response.
     */
    private static void notifyDone(final long timemillis,
            final PendingResponse<Command> pendingResponse) {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(timemillis);
                    pendingResponse.done(new Command());
                } catch (InterruptedException e) {
                }
            }
        }.start();
    }
    
    /**
     * Notify invalid response of a pendingResponse on specified interval.
     * 
     * @param timemillis is interval in millisecond.
     * @param pendingResponse is the pending response.
     */
    private static void notifyInvalidResponse(final long timemillis,
            final PendingResponse<Command> pendingResponse) {
        new Thread() {
            @Override
            public void run() {
                pendingResponse.doneWithInvalidResponse(
                        new InvalidResponseException("Invalid response message"));
            }
        }.start();
    }
}
