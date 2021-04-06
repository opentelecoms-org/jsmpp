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

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class SessionStateTest {
    
    @Test(groups="checkintest")
    public void testClosedState() {
        SessionState sessionState = SessionState.CLOSED;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
        assertFalse(sessionState.isNotClosed());
    }
    
    @Test(groups="checkintest")
    public void testOpenState() {
        SessionState sessionState = SessionState.OPEN;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
        assertTrue(sessionState.isNotClosed());
    }
    
    @Test(groups="checkintest")
    public void testBoundTxState() {
        SessionState sessionState = SessionState.BOUND_TX;
        assertFalse(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
        assertTrue(sessionState.isNotClosed());
    }
    
    @Test(groups="checkintest")
    public void testBoundRxState() {
        SessionState sessionState = SessionState.BOUND_RX;
        assertTrue(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
        assertTrue(sessionState.isNotClosed());
    }
    
    @Test(groups="checkintest")
    public void testBoundTrxState() {
        SessionState sessionState = SessionState.BOUND_TRX;
        assertTrue(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
        assertTrue(sessionState.isNotClosed());
    }
    
    @Test(groups="checkintest")
    public void testUnbound() {
        SessionState sessionState = SessionState.UNBOUND;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
        assertTrue(sessionState.isNotClosed());
    }
}
