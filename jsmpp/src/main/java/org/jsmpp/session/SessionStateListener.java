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

/**
 * This listener contains an event related to the session state.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface SessionStateListener {

    /**
     * Raised when the session state changed.
     * 
     * @param newState is the new state.
     * @param oldState is the old state.
     * @param source is source of changed state.
     */
    void onStateChange(SessionState newState, SessionState oldState, Session source);
}
