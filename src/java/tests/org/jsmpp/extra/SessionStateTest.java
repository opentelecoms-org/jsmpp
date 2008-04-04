package org.jsmpp.extra;

import static org.testng.Assert.*;

import org.jsmpp.session.state.Mode;
import org.testng.annotations.Test;

public class SessionStateTest {
    
    @Test(groups="checkintest")
    public void testClosedState() {
        Mode sessionState = Mode.CLOSED;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testOpenState() {
        Mode sessionState = Mode.OPEN;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTxState() {
        Mode sessionState = Mode.BOUND_TX;
        assertFalse(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundRxState() {
        Mode sessionState = Mode.BOUND_RX;
        assertTrue(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTrxState() {
        Mode sessionState = Mode.BOUND_TRX;
        assertTrue(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testUnbound() {
        Mode sessionState = Mode.UNBOUND;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
}
