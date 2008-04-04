package org.jsmpp.session;

import static org.testng.Assert.fail;

import java.util.concurrent.TimeoutException;

import org.jmock.Mockery;
import org.jsmpp.BindType;
import org.jsmpp.bean.Bind;
import org.jsmpp.session.state.SessionState;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 * 
 */
public class BindRequestReceiverTest {
    private BindRequestReceiver requestReceiver;
    private SessionState<?> sessionState;

    @BeforeMethod
    public void setUp() {
        Mockery mockery = new Mockery();
        sessionState = mockery.mock(SessionState.class);
        requestReceiver = new BindRequestReceiver();
    }

    @Test(groups = "checkintest")
    public void testWaitTimeout() {

        try {
            BindRequest request = requestReceiver.waitForRequest(1000);
            fail("Should fail since no request for 1000 millis");
        } catch (IllegalStateException e) {
            e.printStackTrace();
            fail("Should not fail waitForRequest");
        } catch (TimeoutException e) {
        }
    }

    @Test(groups = "checkintest")
    public void testReceiveRequest() {

        try {
            requestReceiver.notifyAcceptBind(sessionState, dummyBind());
            BindRequest request = requestReceiver.waitForRequest(1000);
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        } catch (TimeoutException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }

        try {
            requestReceiver.notifyAcceptBind(sessionState, dummyBind());
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

    @Test(groups = "checkintest")
    public void testNoSingleAccept() {
        try {
            requestReceiver.notifyAcceptBind(sessionState, dummyBind());
        } catch (IllegalStateException e) {
            fail("Should not fail waitForRequest and success accepting request");
        }

        try {
            requestReceiver.notifyAcceptBind(sessionState, dummyBind());
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    @Test(groups = "checkintest")
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
