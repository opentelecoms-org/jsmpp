package org.jsmpp.session;

import static org.testng.Assert.fail;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.bean.BindType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class BindRequestTest {
    private DummyResponseHandler responseHandler;
    private BindRequest bindRequest;
    
    @BeforeMethod
    public void setUp() {
        responseHandler = new DummyResponseHandler();
        bindRequest = new BindRequest(1, BindType.BIND_TRX, null, null, null, null, null, null, responseHandler);
    }
    
    
    @Test(groups="checkintest")
    public void testSucceedAccept() {
        try {
            bindRequest.accept("sys");
        } catch (PDUStringException e) {
            fail("Should succes accepting bind request");
        } catch (IllegalStateException e1) {
            fail("Should succes accepting bind request");
        } catch (IOException e1) {
            fail("Should succes accepting bind request");
        }
    }
    
    @Test(groups="checkintest")
    public void testFailedAccept() {
        responseHandler.closeConnection();
        try {
            bindRequest.accept("sys");
            fail("Should throw IOException");
        } catch (PDUStringException e) {
            fail("Should throw IOException");
        } catch (IllegalStateException e) {
            fail("Should throw IOException");
        } catch (IOException e) {
        }
    }
    
    @Test(groups="checkintest")
    public void testSucceedReject() {
        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should succes rejecting bind request");
        } catch (IOException e1) {
            fail("Should succes rejecting bind request");
        }
    }
    
    @Test(groups="checkintest")
    public void testFailedReject() {
        responseHandler.closeConnection();
        try {
            bindRequest.reject(-1);
            fail("Should throw IOException");
        } catch (IllegalStateException e) {
            fail("Should throw IOException");
        } catch (IOException e) {
        }
    }
    
    @Test(groups="checkintest")
    public void testNonSingleAccept() {
        try {
            bindRequest.accept("sys");
        } catch (PDUStringException e) {
            fail("Should success accepting bind request");
        } catch (IllegalStateException e1) {
            fail("Should success accepting bind request");
        } catch (IOException e1) {
            fail("Should success accepting bind request");
        }
        try {
            bindRequest.accept("sys");
            fail("Should fail on 2nd accept");
        } catch (PDUStringException e) {
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        } catch (IOException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    
    @Test(groups="checkintest")
    public void testNonSingleReject() {
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
