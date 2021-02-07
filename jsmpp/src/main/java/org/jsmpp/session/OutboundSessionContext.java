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

import org.jsmpp.bean.BindType;
import org.jsmpp.extra.SessionState;

/**
 * Context defined for outbound session life cycle.
 *
 * Possible states: OPEN -&gt; BOUND_TX | BOUND_RX | BOUND_TRX -&gt; UNBOUND -&gt; CLOSE.
 * 
 * @author uudashr
 *
 */
public interface OutboundSessionContext extends ActivityNotifier {
    /**
     * Change state to open.
     */
    void open();
    
    /**
     * Change state to bound state.
     * @param bindType the bindType enum
     */
    void bound(BindType bindType);
    
    /**
     * Change state to unbound.
     */
    void unbound();
    
    /**
     * Change state to close.
     */
    void close();
    
    /**
     * Get current session state.
     * 
     * @return the current session state
     */
    SessionState getSessionState();
    
    /**
     * Get the last activity of a session.
     * 
     * @return the last activity timestamp
     */
    long getLastActivityTimestamp();
}