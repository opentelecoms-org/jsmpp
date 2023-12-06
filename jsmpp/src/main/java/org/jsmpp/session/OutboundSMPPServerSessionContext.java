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
import org.jsmpp.session.state.SMPPOutboundServerSessionState;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author pmoerenhout
 *
 */
class OutboundSMPPServerSessionContext extends AbstractSessionContext {
    private SMPPOutboundServerSessionState stateProcessor = SMPPOutboundServerSessionState.CLOSED;
    private final SMPPOutboundServerSession smppSession;
    private final ReadWriteLock stateProcessorLock = new ReentrantReadWriteLock();

    public OutboundSMPPServerSessionContext(SMPPOutboundServerSession smppSession, SessionStateListener sessionStateListener) {
        super(sessionStateListener);
        this.smppSession = smppSession;
    }
    
    public SMPPOutboundServerSessionState getStateProcessor() {
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

    public synchronized void outbind() {
        changeState(SessionState.OUTBOUND);
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
                    stateProcessor = SMPPOutboundServerSessionState.OPEN;
                } else if (newState == SessionState.OUTBOUND) {
                    stateProcessor = SMPPOutboundServerSessionState.OUTBOUND;
                } else if (newState == SessionState.BOUND_RX) {
                    stateProcessor = SMPPOutboundServerSessionState.BOUND_RX;
                } else if (newState == SessionState.BOUND_TX) {
                    stateProcessor = SMPPOutboundServerSessionState.BOUND_TX;
                } else if (newState == SessionState.BOUND_TRX) {
                    stateProcessor = SMPPOutboundServerSessionState.BOUND_TRX;
                } else if (newState == SessionState.UNBOUND) {
                    stateProcessor = SMPPOutboundServerSessionState.UNBOUND;
                } else if (newState == SessionState.CLOSED) {
                    stateProcessor = SMPPOutboundServerSessionState.CLOSED;
                }
            } finally {
                stateProcessorLock.writeLock().unlock();
            }
            if (oldState != null) {
                fireStateChanged(newState, oldState, smppSession);
            }
        }
    }
}
