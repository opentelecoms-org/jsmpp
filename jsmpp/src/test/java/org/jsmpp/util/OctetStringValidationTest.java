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
import static org.testng.Assert.assertTrue;

import org.jsmpp.PDUStringException;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class OctetStringValidationTest {
    private static final String FULL_LENGTH_TEXT = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor";
    private static final int MAX_SHORT_MESSAGE_LENGTH = 254;
    
    @Test
    public void zeroLength() throws Exception {
        String shortMessage = "";
        assertEquals(shortMessage.length(), 0);
        StringValidator.validateString(shortMessage.getBytes(), StringParameter.SHORT_MESSAGE);
    }
    
    @Test
    public void nonZeroBelow255Length() throws Exception {
        String shortMessage = "This is a simple message";
        assertTrue(shortMessage.length() > 0);
        assertTrue(shortMessage.length() <= MAX_SHORT_MESSAGE_LENGTH);
        StringValidator.validateString(shortMessage.getBytes(), StringParameter.SHORT_MESSAGE);
    }
    
    
    
    @Test
    public void fullLegth() throws Exception {
        assertEquals(FULL_LENGTH_TEXT.length(), MAX_SHORT_MESSAGE_LENGTH);
        StringValidator.validateString(FULL_LENGTH_TEXT.getBytes(), StringParameter.SHORT_MESSAGE);
    }
    
    @Test(expectedExceptions = PDUStringException.class)
    public void overLengthText() throws Exception {
        String shortMessage = FULL_LENGTH_TEXT + " ";
        assertTrue(shortMessage.length() > MAX_SHORT_MESSAGE_LENGTH);
        StringValidator.validateString(shortMessage.getBytes(), StringParameter.SHORT_MESSAGE);
    }
}
