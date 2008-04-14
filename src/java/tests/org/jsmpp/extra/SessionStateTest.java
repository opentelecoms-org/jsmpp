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
    }
    
    @Test(groups="checkintest")
    public void testOpenState() {
        SessionState sessionState = SessionState.OPEN;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTxState() {
        SessionState sessionState = SessionState.BOUND_TX;
        assertFalse(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundRxState() {
        SessionState sessionState = SessionState.BOUND_RX;
        assertTrue(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTrxState() {
        SessionState sessionState = SessionState.BOUND_TRX;
        assertTrue(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testUnbound() {
        SessionState sessionState = SessionState.UNBOUND;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
}
