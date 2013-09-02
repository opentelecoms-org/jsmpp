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

import static org.testng.Assert.*;

import org.jsmpp.PDUStringException;
import org.testng.annotations.Test;

public class StringValidatorTest {

    @Test(groups="checkintest")
    public void validateStringOctedStringWithString() throws Exception {
        StringValidator.validateString("", StringParameter.SHORT_MESSAGE);
        StringValidator.validateString("short messages", StringParameter.SHORT_MESSAGE);
        String shortMessage = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567"
            + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
            + "234567890123456789012345678901234567890123456789012345678901234";
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        try {
            shortMessage = shortMessage + "5";
            StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "Octet String value '" + shortMessage + "' cannot more than 254. Actual length"
                + " of string is 255");
        }
    }

    @Test(groups="checkintest")
    public void validateStringOctedStringWithByteArray() throws Exception {
        StringValidator.validateString("".getBytes("UTF-8"), StringParameter.SHORT_MESSAGE);
        StringValidator.validateString("short messages".getBytes("UTF-8"), StringParameter.SHORT_MESSAGE);
        String shortMessage = "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567"
            + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901"
            + "234567890123456789012345678901234567890123456789012345678901234";
        StringValidator.validateString(shortMessage.getBytes("UTF-8"), StringParameter.SHORT_MESSAGE);

        try {
            shortMessage = shortMessage + "5";
            StringValidator.validateString(shortMessage.getBytes("UTF-8"), StringParameter.SHORT_MESSAGE);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "Octet String value '" + shortMessage + "' cannot more than 254. Actual length"
                + " of string is 255");
        }
    }

    @Test(groups="checkintest")
    public void validateStringCOctedStringWithString() throws Exception {
        StringValidator.validateString("", StringParameter.SYSTEM_ID);
        StringValidator.validateString("System ID", StringParameter.SYSTEM_ID);
        StringValidator.validateString("1234567890123456", StringParameter.SYSTEM_ID);

        try {
            StringValidator.validateString("12345678901234567", StringParameter.SYSTEM_ID);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '12345678901234567' cannot more than 16. Actual length "
                + "of string is 17");
        }
    }

    @Test(groups="checkintest")
    public void validateStringCOctedStringWithByteArray() throws Exception {
        StringValidator.validateString("".getBytes("UTF-8"), StringParameter.SYSTEM_ID);
        StringValidator.validateString("System ID".getBytes("UTF-8"), StringParameter.SYSTEM_ID);
        StringValidator.validateString("1234567890123456".getBytes("UTF-8"), StringParameter.SYSTEM_ID);

        try {
            StringValidator.validateString("12345678901234567".getBytes("UTF-8"), StringParameter.SYSTEM_ID);
            fail("PDUStringException expected");
        } catch (PDUStringException e) {
            // expected
            assertEquals(e.getMessage(), "C-Octet String value '12345678901234567' cannot more than 16. Actual length "
                + "of string is 17");
        }
    }
}
