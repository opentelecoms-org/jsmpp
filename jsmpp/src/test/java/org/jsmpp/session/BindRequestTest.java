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
        bindRequest = new BindRequest(1, BindType.BIND_TRX, null, null, null, null, null, null, null, responseHandler);
    }

    @Test(groups="checkintest")
    public void testSucceedAccept() {
        try {
            bindRequest.accept("sys");
        } catch (PDUStringException | IllegalStateException | IOException e1) {
            fail("Should success accepting bind request");
        }
    }

    @Test(groups="checkintest")
    public void testFailedAccept() {
        responseHandler.closeConnection();
        try {
            bindRequest.accept("sys");
            fail("Should throw IOException");
        } catch (PDUStringException | IllegalStateException e) {
            fail("Should throw IOException");
        } catch (IOException e) {
        }
    }

    @Test(groups="checkintest")
    public void testSucceedReject() {
        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException | IOException e) {
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
        } catch (PDUStringException | IllegalStateException | IOException e) {
            fail("Should success accepting bind request");
        }
        try {
            bindRequest.accept("sys");
            fail("Should fail on 2nd accept");
        } catch (PDUStringException | IOException e) {
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    @Test(groups="checkintest")
    public void testNonSingleReject() {
        try {
            bindRequest.reject(-1);
        } catch (IllegalStateException|IOException e1) {
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
