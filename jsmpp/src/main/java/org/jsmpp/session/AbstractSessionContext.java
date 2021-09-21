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

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.extra.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public abstract class AbstractSessionContext implements SessionContext {
    private static final Logger log = LoggerFactory.getLogger(AbstractSessionContext.class);
    private final List<SessionStateListener> sessionStateListeners = new CopyOnWriteArrayList<>();
    private long lastActivityTimestamp;
    private InterfaceVersion interfaceVersion = InterfaceVersion.IF_34;
    
    public AbstractSessionContext() {
    }
    
    public AbstractSessionContext(SessionStateListener sessionStateListener) {
        sessionStateListeners.add(sessionStateListener);
    }

    @Override
    public synchronized void open() {
        changeState(SessionState.OPEN);
    }

    @Override
    public synchronized void bound(BindType bindType, InterfaceVersion interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
        if (bindType.equals(BindType.BIND_TX)) {
            changeState(SessionState.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_RX)) {
            changeState(SessionState.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            changeState(SessionState.BOUND_TRX);
        } else {
            throw new IllegalArgumentException("Bind type " + bindType + " not supported");
        }
    }

    @Override
    public synchronized void unbound() {
        changeState(SessionState.UNBOUND);
    }

    @Override
    public synchronized void close() {
        changeState(SessionState.CLOSED);
    }
    
    public void addSessionStateListener(SessionStateListener listener) {
        sessionStateListeners.add(listener);
    }
    
    public void removeSessionStateListener(SessionStateListener l) {
        sessionStateListeners.remove(l);
    }

    protected void fireStateChanged(SessionState newState, SessionState oldState, Session source) {

        for (SessionStateListener l : sessionStateListeners) {
            if (newState.equals(oldState)){
                throw new IllegalStateException("State is already " + newState);
            }
            try {
                l.onStateChange(newState, oldState, source);
            } catch (Exception e) {
                log.error("Invalid runtime exception thrown when calling onStateChange for {}", source, e);
            }
        }

    }

    @Override
    public void notifyActivity() {
        lastActivityTimestamp = System.currentTimeMillis();
    }

    @Override
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public InterfaceVersion getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(final InterfaceVersion interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    protected abstract void changeState(SessionState newState);
}
