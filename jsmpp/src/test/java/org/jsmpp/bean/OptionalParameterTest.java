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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import static org.testng.AssertJUnit.assertFalse;

import java.io.UnsupportedEncodingException;

import org.jsmpp.bean.OptionalParameter.COctetString;
import org.jsmpp.bean.OptionalParameter.Callback_num_pres_ind.Presentation_Indicator;
import org.jsmpp.bean.OptionalParameter.Callback_num_pres_ind.Screening_Indicator;
import org.jsmpp.bean.OptionalParameter.Ms_msg_wait_facilities;
import org.jsmpp.bean.OptionalParameter.Ms_msg_wait_facilities.Ms_msg_wait_facilities_type;
import org.jsmpp.bean.OptionalParameter.Network_error_code.Network_error_code_type;
import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.util.OctetUtil;
import org.testng.annotations.Test;

/**
 * Test case for Optional Parameter.
 * 
 * @author mikko.koponen
 * @author uudashr
 * @author pmoerenhout
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
        assertNull(Tag.valueOf(tagCode));
    }
    
    @Test
    public void anotherUndefinedTag() {
        short tagCode = (short)(-127 & 0xffff);
        assertNull(Tag.valueOf(tagCode));
    }
    
    @Test
    public void parsingMessagePayload() {
        OptionalParameter optParam = OptionalParameters.deserialize(Tag.MESSAGE_PAYLOAD.code(), "hello".getBytes());
        if(! (optParam instanceof OctetString))
        {
            fail("message_payload should be of type OctetString");
        }
    }
    
    @Test
    public void ms_msg_wait_facilities() {
    	Ms_msg_wait_facilities o = new OptionalParameter.Ms_msg_wait_facilities((byte)0x81);
    	
    	assertTrue(o.isIndicatorActive());
    	assertEquals(o.getMessageType(), Ms_msg_wait_facilities_type.FAX_MESSAGE_WAITING);
    	
    	o = new OptionalParameter.Ms_msg_wait_facilities((byte)0x00);
    	assertEquals(o.getMessageType(), Ms_msg_wait_facilities_type.VOICEMAIL_MESSAGE_WAITING);
    	assertFalse(o.isIndicatorActive());
    	
    	o = new OptionalParameter.Ms_msg_wait_facilities(true, Ms_msg_wait_facilities_type.FAX_MESSAGE_WAITING);
    	assertEquals(o.getMessageType(), Ms_msg_wait_facilities_type.FAX_MESSAGE_WAITING);
    	assertTrue(o.isIndicatorActive());
    	assertEquals(o.getValue(), (byte)0x81);
    }
    
    @Test
    public void Source_subaddress() {
    	OptionalParameter.Source_subaddress o = new OptionalParameter.Source_subaddress(null);
    	assertEquals(Tag.valueOf(o.tag), Tag.SOURCE_SUBADDRESS);
    }
    
    @Test
    public void Sc_interface_version() {
    	OptionalParameter.Sc_interface_version o = new OptionalParameter.Sc_interface_version(InterfaceVersion.IF_34);
    	assertEquals(Tag.valueOf(o.tag), Tag.SC_INTERFACE_VERSION);
    	assertEquals(o.value, InterfaceVersion.IF_34.value());
    }
    
	@Test
	public void Callback_num_pres_ind() {
		OptionalParameter.Callback_num_pres_ind o = new OptionalParameter.Callback_num_pres_ind((byte)0x06);
		
		assertEquals(o.getPresentationIndicator(), Presentation_Indicator.PRESENTATION_RESTRICTED);
		assertEquals(o.getScreeningIndicator(), Screening_Indicator.USER_PROVIDED_VERIFIED_AND_FAILED);
		
		o = new OptionalParameter.Callback_num_pres_ind(
					Presentation_Indicator.NUMBER_NOT_AVAILABLE, 
					Screening_Indicator.USER_PROVIDED_VERIFIED_AND_PASSED);
		assertEquals(o.getValue(), 0x09);
    }
	
	@Test
	public void Network_error_code() {
		OptionalParameter.Network_error_code o = new OptionalParameter.Network_error_code(new byte[] {0x01, (byte)0x01, (byte)0x01});
		assertEquals(Network_error_code_type.ANSI_136, o.getNetworkType());
		assertEquals(o.getErrorCode(), 257);
		
		o = new OptionalParameter.Network_error_code(new byte[] {0x03, (byte)0xFF, (byte)0xFF});
		assertEquals(o.getNetworkType(), Network_error_code_type.GSM);
		assertEquals(o.getErrorCode(), -1);
		
		o = new OptionalParameter.Network_error_code(Network_error_code_type.IS_95, (short)100);
		assertEquals(o.getNetworkType(), Network_error_code_type.IS_95);
		assertEquals(o.getErrorCode(), 100);
		
		o = new OptionalParameter.Network_error_code(Network_error_code_type.IS_95, (short)1000);
		assertEquals(o.getNetworkType(), Network_error_code_type.IS_95);
		assertEquals(o.getErrorCode(), 1000);
		
		o = new OptionalParameter.Network_error_code(Network_error_code_type.IS_95, (short)60000);
		assertEquals(o.getNetworkType(), Network_error_code_type.IS_95);
		assertEquals(o.getErrorCode(), (short)60000);
		
	}

  @Test(groups="checkintest")
  public void cOctetStringGetValueAsString() throws UnsupportedEncodingException {
    COctetString string = new OptionalParameter.COctetString(Tag.ADDITIONAL_STATUS_INFO_TEXT.code(), "urgent");

    assertEquals(string.getValueAsString(), "urgent");

		assertEquals((byte)0x75, string.getValue()[0]); // u
		assertEquals((byte)0x72, string.getValue()[1]); // r
		assertEquals((byte)0x67, string.getValue()[2]); // g
		assertEquals((byte)0x65, string.getValue()[3]); // e
		assertEquals((byte)0x6e, string.getValue()[4]); // n
		assertEquals((byte)0x74, string.getValue()[5]); // t
		assertEquals((byte)0x00, string.getValue()[6]); // NULL
  }

  @Test
  public void testAdditionalStatusInfoTextStringAsString() {
    COctetString op = new OptionalParameter.Additional_status_info_text("additional text");
    assertEquals("additional text", op.getValueAsString());
  }

  @Test
  public void testAdditionalStatusInfoTextBytesAsString() {
    byte[] content = "more additional text\0".getBytes();
    COctetString op = new OptionalParameter.Additional_status_info_text(content);
    assertEquals("more additional text", op.getValueAsString());
  }

  @Test
  public void testReceiptedMessageId() {
    byte[] content = "123456\0".getBytes();
    OptionalParameter.Receipted_message_id op = new OptionalParameter.Receipted_message_id(content);
    assertEquals("123456", op.getValueAsString());
  }

  @Test
  public void testCongestionState() {
    OptionalParameter.Congestion_state op = new OptionalParameter.Congestion_state((byte)80);
    assertEquals(80, op.getValue() & 0xff);
  }
}