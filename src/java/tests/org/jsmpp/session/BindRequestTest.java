package org.jsmpp.session;

import java.io.IOException;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.jsmpp.BindType;
import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 * 
 */
public class BindRequestTest {
    private SMPPServerSession serverSession;
    private BindRequest bindRequest;
    private Mockery mockery;

    @BeforeMethod
    public void setUp() {
        mockery = new Mockery();
        mockery.setImposteriser(ClassImposteriser.INSTANCE);
        serverSession = mockery.mock(SMPPServerSession.class);
        SMPPServerSessionResponseHandler responseHandler = mockery.mock(SMPPServerSessionResponseHandler.class);
        serverSession.responseHandler = responseHandler;
        bindRequest = new BindRequest(1, BindType.BIND_TRX, null, null, null, null, null, null, responseHandler);
    }

    @Test(groups = "checkintest")
    public void testSucceedAccept() throws IOException {
        mockery.checking(new Expectations() {
            {
                one(serverSession.responseHandler).sendBindResp("sys", BindType.BIND_TRX, 1);
            }
        });
        try {
            bindRequest.accept("sys");
        } catch (IllegalStateException e1) {
            fail("Should succes accepting bind request");
        } catch (IOException e1) {
            fail("Should succes accepting bind request");
        }
    }

    @Test(groups = "checkintest")
    public void testSucceedReject() throws IOException {
        mockery.checking(new Expectations() {
            {
                one(serverSession.responseHandler).sendNegativeResponse(9, -1, 1);
            }
        });

        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should succes rejecting bind request");
        } catch (IOException e1) {
            fail("Should succes rejecting bind request");
        }
    }

    @Test(groups = "checkintest")
    public void testNonSingleAccept() throws IOException {
        mockery.checking(new Expectations() {
            {
                one(serverSession.responseHandler).sendBindResp("sys", BindType.BIND_TRX, 1);
            }
        });

        try {
            bindRequest.accept("sys");
        } catch (IllegalStateException e1) {
            fail("Should success accepting bind request");
        } catch (IOException e1) {
            fail("Should success accepting bind request");
        }
        try {
            bindRequest.accept("sys");
            fail("Should fail on 2nd accept");
        } catch (IllegalStateException e) {
        } catch (IOException e) {
            fail("Should throw IllegalStateException");
        }
    }

    @Test(groups = "checkintest")
    public void testNonSingleReject() throws IOException {
        mockery.checking(new Expectations() {
            {
                one(serverSession.responseHandler).sendNegativeResponse(9, -1, 1);
            }
        });

        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should success rejecting bind request");
        } catch (IOException e1) {
            fail("Should success rejecting bind request");
        }
        try {
            bindRequest.reject(-1);
            fail("Should fail on 2nd reject");
        } catch (IllegalStateException e) {
        } catch (IOException e) {
            fail("Should throw IllegalStateException");
        }
    }

}
