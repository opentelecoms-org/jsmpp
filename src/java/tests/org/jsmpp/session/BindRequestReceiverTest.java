package org.jsmpp.session;

import static org.junit.Assert.*;

import java.util.concurrent.TimeoutException;

import org.jsmpp.BindType;
import org.jsmpp.bean.Bind;
import org.junit.Before;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class BindRequestReceiverTest {
    private BindRequestReceiver requestReceiver;
    
    @Before
    public void setUp() {
        requestReceiver = new BindRequestReceiver(new DummyResponseHandler());
    }
    
    //@Test
    public void testWaitTimeout() {
        
        try {
            BindRequest request = requestReceiver.waitForRequest(1000);
            fail("Should fail since no request for 1000 millis");
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest");
        } catch (TimeoutException e) {
        }
    }
    
    @Test
    public void testReceiveRequest() {
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            BindRequest request = requestReceiver.waitForRequest(1000);
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        } catch (TimeoutException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
        
        try {
            requestReceiver.waitForRequest(1000);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        } catch (TimeoutException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    @Test
    public void testNoSingleAccept() {
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }
        
        try {
            requestReceiver.notifyAcceptBind(dummyBind());
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }
    
    @Test
    public void testNonSingleWait() {
        
        try {
            BindRequest request = requestReceiver.waitForRequest(1000);
            fail("Should throw TimeoutException");
        } catch (IllegalStateException e) {
            fail("Should throw TimeoutException");
        } catch (TimeoutException e) {
        }
        
        try {
            requestReceiver.waitForRequest(1000);
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        } catch (TimeoutException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    private static final Bind dummyBind() {
        Bind bind = new Bind();
        bind.setCommandId(BindType.BIND_RX.commandId());
        return bind;
    }
}
