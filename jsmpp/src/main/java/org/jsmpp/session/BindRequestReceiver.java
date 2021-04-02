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
package org.jsmpp.session;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.jsmpp.bean.Bind;

/**
 * @author uudashr
 *
 */
class BindRequestReceiver {
    private final Lock lock = new ReentrantLock();
    private final Condition requestCondition = lock.newCondition();
    private final GenericServerResponseHandler responseHandler;
    private BindRequest request;
    private boolean alreadyWaitForRequest;
    
    BindRequestReceiver(GenericServerResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }
    
    /**
     * Wait until the bind request received for specified timeout.
     * 
     * @param timeout is the timeout in milliseconds.
     * @return the {@link BindRequest}.
     * @throws IllegalStateException if this method already called before.
     * @throws TimeoutException if the timeout has been reached.
     */
    BindRequest waitForRequest(long timeout) throws IllegalStateException, TimeoutException {
        lock.lock();
        try {
            if (alreadyWaitForRequest) {
                throw new IllegalStateException("waitForRequest(long) method already invoked");
            } else if (request == null) {
                try {
                    requestCondition.await(timeout, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            if (request != null) {
                return request;
            } else {
                throw new TimeoutException("No bind request after " + timeout + "ms");
            }
        } finally {
            alreadyWaitForRequest = true;
            lock.unlock();
        }
    }
    
    /**
     * Notify that the bind has accepted.
     * 
     * @param bindParameter is the {@link Bind} command.
     * @throws IllegalStateException if this method is already called before.
     */
    void notifyAcceptBind(Bind bindParameter) throws IllegalStateException {
        lock.lock();
        try {
            if (request == null) {
                request = new BindRequest(bindParameter, responseHandler);
                requestCondition.signal();
            } else {
                throw new IllegalStateException("Already waiting for acceptance bind");
            }
        } finally {
            lock.unlock();
        }
    }
}
