package org.jsmpp.session;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.state.SMPPSessionState;

/**
 * @author uudashr
 *
 */
public class SMPPSessionContext extends AbstractSessionContext {
    private final SMPPSession smppSession;
    private SMPPSessionState stateProcessor = SMPPSessionState.CLOSED;
    
    public SMPPSessionContext(SMPPSession smppSession,
            SessionStateListener sessionStateListener) {
        super(sessionStateListener);
        this.smppSession = smppSession;
    }
    
    public SMPPSessionContext(SMPPSession smppSession) {
        this(smppSession, null);
    }
    
    public synchronized SMPPSessionState getStateProcessor() {
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
                stateProcessor = SMPPSessionState.OPEN;
            } else if (newState == SessionState.BOUND_RX) {
                stateProcessor = SMPPSessionState.BOUND_RX;
            } else if (newState == SessionState.BOUND_TX) {
                stateProcessor = SMPPSessionState.BOUND_TX;
            } else if (newState == SessionState.BOUND_TRX) {
                stateProcessor = SMPPSessionState.BOUND_TRX;
            } else if (newState == SessionState.UNBOUND) {
                stateProcessor = SMPPSessionState.UNBOUND;
            } else if (newState == SessionState.CLOSED) {
                stateProcessor = SMPPSessionState.CLOSED;
            }
            fireStateChanged(newState, oldState, smppSession);
        }
    }
}
