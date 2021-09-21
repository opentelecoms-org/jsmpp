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
package org.jsmpp.util;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.nio.charset.StandardCharsets;

import org.jsmpp.PDUStringException;
import org.testng.annotations.Test;

/**
 * Test case of string validation.
 * @author uudashr
 * @version 1.0
 *
 */
public class StringValidatorTest {
    
    @Test(groups="checkintest")
    public void testValidation() {
        try {
            StringValidator.validateString("", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting empty string");
        }
        
        try {
            StringValidator.validateString((String)null, StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting null string");
        }
        
        try {
            StringValidator.validateString("smsgw", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting string that has length less than 16");
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgw", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting 15 char of string");
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgwe", StringParameter.SYSTEM_ID);
            fail("Should fail inserting 16 char of string");
        } catch (PDUStringException e) {
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgwee", StringParameter.SYSTEM_ID);
            fail("Should be fail inserting 17 char of string");
        } catch (PDUStringException e) {
        }
    }

    @Test(groups="checkintest")
    public void validateStringOctetStringWithString() throws Exception {
        StringValidator.validateString("", StringParameter.SHORT_MESSAGE);
        StringValidator.validateString("short messages", StringParameter.SHORT_MESSAGE);
        String shortMessage = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567"
            + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
            + "234567890123456789012345678901234567890123456789012345678901234";
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        try {
            shortMessage += "5";
            StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "Octet String value '" + shortMessage + "' length must be less than or equal "
                + "to 254. Actual length is 255");
        }
    }

    @Test(groups="checkintest")
    public void validateStringOctetStringWithByteArray() throws Exception {
        StringValidator.validateString("".getBytes(StandardCharsets.UTF_8), StringParameter.SHORT_MESSAGE);
        StringValidator.validateString("short messages".getBytes(StandardCharsets.UTF_8), StringParameter.SHORT_MESSAGE);
        String shortMessage = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567"
            + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
            + "234567890123456789012345678901234567890123456789012345678901234";
        StringValidator.validateString(shortMessage.getBytes(StandardCharsets.UTF_8), StringParameter.SHORT_MESSAGE);

        try {
            shortMessage = shortMessage + "5";
            StringValidator.validateString(shortMessage.getBytes(StandardCharsets.UTF_8), StringParameter.SHORT_MESSAGE);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "Octet String value '" + shortMessage + "' length must be less than or equal "
                + "to 254. Actual length is 255");
        }
    }

    @Test(groups="checkintest")
    public void validateStringCOctetStringWithString() throws Exception {
        StringValidator.validateString("", StringParameter.SYSTEM_ID);
        StringValidator.validateString("System ID", StringParameter.SYSTEM_ID);
        StringValidator.validateString("123456789012345", StringParameter.SYSTEM_ID);

        try {
            StringValidator.validateString("1234567890123456", StringParameter.SYSTEM_ID);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '1234567890123456' length must be less than 16. "
                + "Actual length is 16");
        }
    }

    @Test(groups="checkintest")
    public void validateStringCOctetStringWithByteArray() throws Exception {
        StringValidator.validateString("".getBytes(StandardCharsets.UTF_8), StringParameter.SYSTEM_ID);
        StringValidator.validateString("System ID".getBytes(StandardCharsets.UTF_8), StringParameter.SYSTEM_ID);
        StringValidator.validateString("123456789012345".getBytes(StandardCharsets.UTF_8), StringParameter.SYSTEM_ID);

        try {
            StringValidator.validateString("1234567890123456".getBytes(StandardCharsets.UTF_8), StringParameter.SYSTEM_ID);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '1234567890123456' length must be less than 16. "
                + "Actual length is 16");
        }
    }
    
    @Test(groups="checkintest")
    public void validateStringCOctetStringWithStringAndWithoutARange() throws Exception {
        StringValidator.validateString("", StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString("020610233429000R", StringParameter.SCHEDULE_DELIVERY_TIME);

        try {
            StringValidator.validateString("020610233429000RX", StringParameter.SCHEDULE_DELIVERY_TIME);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '020610233429000RX' length should be 1 or 16. Actual "
                + "length is 17");
        }
    }
    
    @Test(groups="checkintest")
    public void validateStringCOctetStringWithByteArrayAndWithoutARange() throws Exception {
        StringValidator.validateString("".getBytes(StandardCharsets.UTF_8), StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString("020610233429000R".getBytes(StandardCharsets.UTF_8), StringParameter.SCHEDULE_DELIVERY_TIME);

        try {
            StringValidator.validateString("020610233429000RX".getBytes(StandardCharsets.UTF_8), StringParameter.SCHEDULE_DELIVERY_TIME);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '020610233429000RX' length should be 1 or 16. Actual "
                + "length is 17");
        }
    }
}
