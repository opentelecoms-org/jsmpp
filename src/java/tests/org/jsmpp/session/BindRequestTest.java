package org.jsmpp.session;

import static org.testng.Assert.fail;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jsmpp.BindType;
import org.jsmpp.session.state.server.ServerSessionState;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 * 
 */
public class BindRequestTest {
    private ServerSession serverSession;
    private BindRequest bindRequest;
    private Mockery mockery;

    @BeforeMethod
    public void setUp() {
        mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        serverSession = mockery.mock(SMPPServerSession.class);
        serverSession.state = mockery.mock(ServerSessionState.class);
        ServerResponseHandler responseHandler = mockery.mock(ServerResponseHandler.class);
        serverSession.responseHandler = responseHandler;
        bindRequest = new BindRequest(1, BindType.BIND_TRX, null, null, null, null, null, null, serverSession.state);
    }

    @Test(groups = "checkintest")
    public void testSucceedAccept() {
        mockery.checking(new Expectations() {
            {
                one(serverSession.state).sendBindResp("sys", BindType.BIND_TRX, 1);
            }
        });
        try {
            bindRequest.accept("sys");
        } catch (IllegalStateException e1) {
            fail("Should success accepting bind request");
        }
    }

    @Test(groups = "checkintest")
    public void testSucceedReject() {
        mockery.checking(new Expectations() {
            {
                one(serverSession.state).sendNegativeResponse(9, -1, 1);
            }
        });

        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should succes rejecting bind request");
        }
    }

    @Test(groups = "checkintest")
    public void testNonSingleAccept() {
        mockery.checking(new Expectations() {
            {
                one(serverSession.state).sendBindResp("sys", BindType.BIND_TRX, 1);
            }
        });

        try {
            bindRequest.accept("sys");
        } catch (IllegalStateException e1) {
            fail("Should success accepting bind request");
        }
        try {
            bindRequest.accept("sys");
            fail("Should fail on 2nd accept");
        } catch (IllegalStateException e) {
        }
    }

    @Test(groups = "checkintest")
    public void testNonSingleReject() {
        mockery.checking(new Expectations() {
            {
                one(serverSession.state).sendNegativeResponse(9, -1, 1);
            }
        });

        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should success rejecting bind request");
        }
        try {
            bindRequest.reject(-1);
            fail("Should fail on 2nd reject");
        } catch (IllegalStateException e) {
        }
    }

}
