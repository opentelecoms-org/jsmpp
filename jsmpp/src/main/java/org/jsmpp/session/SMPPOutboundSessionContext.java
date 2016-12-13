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
import org.jsmpp.session.state.SMPPOutboundSessionState;

/**
 * @author uudashr
 *
 */
public class SMPPOutboundSessionContext extends AbstractSessionContext {
    private final SMPPOutboundSession smppSession;
    private SMPPOutboundSessionState stateProcessor = SMPPOutboundSessionState.CLOSED;

    public SMPPOutboundSessionContext(SMPPOutboundSession smppSession,
                                      SessionStateListener sessionStateListener) {
        super(sessionStateListener);
        this.smppSession = smppSession;
    }
    
    public synchronized SMPPOutboundSessionState getStateProcessor() {
        return stateProcessor;
    }
    
    public synchronized SessionState getSessionState() {
        return stateProcessor.getSessionState();
    }
    
    @Override
    protected void changeState(SessionState newState) {
        if (!stateProcessor.getSessionState().equals(newState)) {
            final SessionState oldState = stateProcessor.getSessionState();
            
            // change the session state processor
            if (newState == SessionState.OPEN) {
                stateProcessor = SMPPOutboundSessionState.OPEN;
            } else if (newState == SessionState.BOUND_RX) {
                stateProcessor = SMPPOutboundSessionState.BOUND_RX;
            } else if (newState == SessionState.BOUND_TX) {
                stateProcessor = SMPPOutboundSessionState.BOUND_TX;
            } else if (newState == SessionState.BOUND_TRX) {
                stateProcessor = SMPPOutboundSessionState.BOUND_TRX;
            } else if (newState == SessionState.UNBOUND) {
                stateProcessor = SMPPOutboundSessionState.UNBOUND;
            } else if (newState == SessionState.CLOSED) {
                stateProcessor = SMPPOutboundSessionState.CLOSED;
            }
            fireStateChanged(newState, oldState, smppSession);
        }
    }
}
