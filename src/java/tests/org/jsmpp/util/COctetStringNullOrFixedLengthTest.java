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

import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class COctetStringNullOrFixedLengthTest {
    
    @Test(groups="checkintest")
    public void testNull() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid((String)null, 10));
    }
    
    @Test(groups="checkintest")
    public void testEmpty() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid("", 10));
    }
    
    @Test(groups="checkintest")
    public void testSingleChar() throws Exception {
        assertFalse(StringValidator.isCOctetStringNullOrNValValid("1", 10));
    }
    
    @Test(groups="checkintest")
    public void testFixedLength() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid("123456789", 10));
    }
    
    @Test(groups="checkintest")
    public void testNonExceedLength() throws Exception {
         assertFalse(StringValidator.isCOctetStringNullOrNValValid("01234567", 10));
    }
    
    @Test(groups="checkintest")
    public void testExceedLength() throws Exception {
        assertFalse(StringValidator.isCOctetStringNullOrNValValid("01234567891", 10));
    }
}
