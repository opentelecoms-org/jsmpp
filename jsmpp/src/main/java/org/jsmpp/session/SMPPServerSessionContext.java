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

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.state.SMPPServerSessionState;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author uudashr
 *
 */
public class SMPPServerSessionContext extends AbstractSessionContext {
    private SMPPServerSessionState stateProcessor = SMPPServerSessionState.CLOSED;
    private final SMPPServerSession smppServerSession;
    private final ReadWriteLock stateProcessorLock = new ReentrantReadWriteLock();
    
    public SMPPServerSessionContext(SMPPServerSession smppServerSession) {
        this.smppServerSession = smppServerSession;
    }
    
    @Override
    protected void changeState(SessionState newState) {
        if (!stateProcessor.getSessionState().equals(newState)) {
            SessionState oldState;
            try {
                stateProcessorLock.writeLock().lock();
                oldState = stateProcessor.getSessionState();

                // change the session state processor
                if (newState == SessionState.OPEN) {
                    stateProcessor = SMPPServerSessionState.OPEN;
                } else if (newState == SessionState.BOUND_RX) {
                    stateProcessor = SMPPServerSessionState.BOUND_RX;
                } else if (newState == SessionState.BOUND_TX) {
                    stateProcessor = SMPPServerSessionState.BOUND_TX;
                } else if (newState == SessionState.BOUND_TRX) {
                    stateProcessor = SMPPServerSessionState.BOUND_TRX;
                } else if (newState == SessionState.UNBOUND) {
                    stateProcessor = SMPPServerSessionState.UNBOUND;
                } else if (newState == SessionState.CLOSED) {
                    stateProcessor = SMPPServerSessionState.CLOSED;
                }
            } finally {
                stateProcessorLock.writeLock().unlock();
            }
            if (oldState != null) {
                fireStateChanged(newState, oldState, smppServerSession);
            }
        }
    }

    public SMPPServerSessionState getStateProcessor() {
        try {
            stateProcessorLock.readLock().lock();
            return stateProcessor;
        } finally {
            stateProcessorLock.readLock().unlock();
        }
    }
    
    public SessionState getSessionState() {
        try {
            stateProcessorLock.readLock().lock();
            return stateProcessor.getSessionState();
        } finally {
            stateProcessorLock.readLock().unlock();
        }
    }
}
