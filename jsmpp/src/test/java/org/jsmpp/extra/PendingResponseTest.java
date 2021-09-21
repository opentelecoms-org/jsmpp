/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.extra;

import static org.testng.Assert.fail;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.bean.Command;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class PendingResponseTest {
    private PendingResponse<Command> pendingResponse;
    
    @BeforeMethod
    public void setUp() throws Exception {
        pendingResponse = new PendingResponse<>(1000);
    }
    
    /**
     * Test {@link PendingResponse} for reached timeout.
     */
    @Test(groups="checkintest")
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
    
    @Test(groups="checkintest")
    public void testUnreachTimeout() {
        try {
            notifyDone(90, pendingResponse);
            pendingResponse.waitDone();
        } catch (ResponseTimeoutException | InvalidResponseException e) {
            fail("Should be done with valid response");
        }
    }

    @Test(groups="checkintest")
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
                    //re-interrupt the current thread
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(e);
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
                try {
                    Thread.sleep(timemillis);
                    pendingResponse.doneWithInvalidResponse(
                            new InvalidResponseException("Invalid response message"));
                } catch (InterruptedException e) {
                    //re-interrupt the current thread
                    Thread.currentThread().interrupt();
                }
            }
        }.start();
    }
}
