package org.jsmpp.extra;

import static org.testng.Assert.*;

import org.jsmpp.session.state.State;
import org.testng.annotations.Test;

public class SessionStateTest {
    
    @Test(groups="checkintest")
    public void testClosedState() {
        State sessionState = State.CLOSED;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testOpenState() {
        State sessionState = State.OPEN;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTxState() {
        State sessionState = State.BOUND_TX;
        assertFalse(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundRxState() {
        State sessionState = State.BOUND_RX;
        assertTrue(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testBoundTrxState() {
        State sessionState = State.BOUND_TRX;
        assertTrue(sessionState.isReceivable());
        assertTrue(sessionState.isTransmittable());
        assertTrue(sessionState.isBound());
    }
    
    @Test(groups="checkintest")
    public void testUnbound() {
        State sessionState = State.UNBOUND;
        assertFalse(sessionState.isReceivable());
        assertFalse(sessionState.isTransmittable());
        assertFalse(sessionState.isBound());
    }
}
