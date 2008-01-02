package org.jsmpp.session;

import static org.junit.Assert.*;

import java.io.IOException;

import org.jsmpp.BindType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class BindRequestTest {
    private DummyResponseHandler responseHandler;
    private BindRequest bindRequest;
    
    @Before
    public void setUp() {
        responseHandler = new DummyResponseHandler();
        bindRequest = new BindRequest(1, BindType.BIND_TRX, null, null, null, null, null, null, responseHandler);
    }
    
    
    @Test
    public void testSucceedAccept() {
        try {
            bindRequest.accept();
        } catch (IllegalStateException e1) {
            fail("Should succes accepting bind request");
        } catch (IOException e1) {
            fail("Should succes accepting bind request");
        }
    }
    
    @Test
    public void testFailedAccept() {
        responseHandler.closeConnection();
        try {
            bindRequest.accept();
            fail("Should throw IOException");
        } catch (IllegalStateException e) {
            fail("Should throw IOException");
        } catch (IOException e) {
        }
    }
    
    @Test
    public void testSucceedReject() {
        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException e1) {
            fail("Should succes rejecting bind request");
        } catch (IOException e1) {
            fail("Should succes rejecting bind request");
        }
    }
    
    @Test
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
    
    @Test
    public void testNonSingleAccept() {
        try {
            bindRequest.accept();
        } catch (IllegalStateException e1) {
            fail("Should success accepting bind request");
        } catch (IOException e1) {
            fail("Should success accepting bind request");
        }
        try {
            bindRequest.accept();
            fail("Should fail on 2nd accept");
        } catch (IllegalStateException e) {
        } catch (IOException e) {
            fail("Should throw IllegalStateException");
        }
    }
    
    
    @Test
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
