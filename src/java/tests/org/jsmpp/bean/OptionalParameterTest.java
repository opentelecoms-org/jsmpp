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
package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.util.OctetUtil;
import org.testng.annotations.Test;

/**
 * Test case for Optional Parameter.
 * 
 * @author mikko.koponen
 * @author uudashr
 *
 */
public class OptionalParameterTest {

    @Test(groups="checkintest")
    public void stringParameterSerialization() {
        OptionalParameter param = new OptionalParameter.OctetString(Tag.DEST_SUBADDRESS, "jeah");
        byte[] serialised = param.serialize();
        assertEquals(serialised.length, 2 + 2 + 4);
        assertEquals(OctetUtil.bytesToShort(serialised, 0), Tag.DEST_SUBADDRESS.code());
        assertEquals(OctetUtil.bytesToShort(serialised, 2), "jeah".getBytes().length);
        assertEquals(new String(serialised, 4, serialised.length - 4), "jeah");
    }
    
    @Test(groups="checkintest")
    public void stringParameterDeserialization() {
        OptionalParameter param = OptionalParameters.deserialize(Tag.DEST_SUBADDRESS.code(), "jeah".getBytes());
        assertTrue(param instanceof OptionalParameter.OctetString);
        OptionalParameter.OctetString stringParam = (OctetString) param;
        assertEquals(stringParam.getValueAsString(), "jeah");
    }
    
    @Test
    public void undefinedTag() {
        final short tagCode = 0x0000;
        try {
            Tag.valueOf(tagCode);
            fail("Tag code 0x0000 should be not found");
        } catch (IllegalArgumentException e) {
        }
        OptionalParameters.deserialize(tagCode, "Undefined tag".getBytes());
    }
    
    @Test
    public void anotherUndefinedTag() {
        short tagCode = (short)(-127 & 0xffff);
        try {
            Tag.valueOf(tagCode);
            fail("Tag code " + tagCode + " should be not found");
        } catch (IllegalArgumentException e) {
        }
        OptionalParameters.deserialize(tagCode, "Undefined tag".getBytes());
    }
}