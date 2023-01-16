package org.jsmpp.util;

import static java.nio.charset.StandardCharsets.US_ASCII;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.io.ByteArrayOutputStream;

import org.jsmpp.bean.DataSmResp;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitSm;
import org.junit.Test;

/**
 * @author pmoerenhout
 */
public class DefaultDecomposerTest {

  PDUDecomposer decomposer = new DefaultDecomposer();

  @Test
  public void decompose_submit_sm() throws Exception {
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    // command_length
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x46 });
    // command_id
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x04 });
    // command_status
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x00 });
    // sequence_number
    data.write(new byte[]{ 0x00, 0x00, 0x12, 0x34 });
    // service_type
    data.write("CMT\000".getBytes(US_ASCII));
    // source_addr_ton
    data.write(1);
    // source_addr_npi
    data.write(2);
    // source_addr
    data.write("31600000000\000".getBytes(US_ASCII));
    // dest_addr_ton
    data.write(3);
    // dest_addr_npi
    data.write(4);
    // destination_addr
    data.write("31612345678\000".getBytes(US_ASCII));
    // esm_class
    data.write(5);
    // protocol_id
    data.write(6);
    // priority_flag
    data.write(7);
    // schedule_delivery_time
    data.write(0);
    // validity_period
    data.write(0);
    // registered_delivery
    data.write(8);
    // replace_if_present_flag
    data.write(1);
    // data_coding
    data.write(9);
    // sm_default_msg_id
    data.write(10);
    // sm_length
    data.write(12);
    // short_message
    data.write("Hello World!".getBytes(US_ASCII));

    SubmitSm submitSm = decomposer.submitSm(data.toByteArray());
    assertEquals(submitSm.getCommandLength(), data.size());
    assertEquals(submitSm.getCommandLength(), 70);
    assertEquals(submitSm.getCommandId(), 4);
    assertEquals(submitSm.getCommandStatus(), 0);
    assertEquals(submitSm.getSequenceNumber(), 0x1234);
    assertEquals(submitSm.getServiceType(), "CMT");
    assertEquals(submitSm.getSourceAddrTon(), (byte) 0x01);
    assertEquals(submitSm.getSourceAddrNpi(), (byte) 0x02);
    assertEquals(submitSm.getSourceAddr(), "31600000000");
    assertEquals(submitSm.getDestAddrTon(), (byte) 0x03);
    assertEquals(submitSm.getDestAddrNpi(), (byte) 0x04);
    assertEquals(submitSm.getDestAddress(), "31612345678");
    assertEquals(submitSm.getEsmClass(), (byte) 0x05);
    assertEquals(submitSm.getProtocolId(), (byte) 0x06);
    assertEquals(submitSm.getPriorityFlag(), (byte) 0x07);
    assertNull(submitSm.getScheduleDeliveryTime());
    assertNull(submitSm.getValidityPeriod());
    assertEquals(submitSm.getRegisteredDelivery(), (byte) 0x08);
    assertEquals(submitSm.getReplaceIfPresent(), (byte) 0x01);
    assertEquals(submitSm.getDataCoding(), (byte) 0x09);
    assertEquals(submitSm.getSmDefaultMsgId(), (byte) 0x0a);
    assertEquals(new String(submitSm.getShortMessage(), US_ASCII), "Hello World!");
  }

  @Test
  public void decompose_data_sm_resp_with_error() throws Exception {
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    // command_length
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x19 });
    // command_id
    data.write(new byte[]{ (byte)0x80, 0x00, 0x01, 0x03 });
    // command_status
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x14 });
    // sequence_number
    data.write(new byte[]{ 0x00, 0x00, 0x12, 0x34 });
    // message_id
    data.write("01234567\000".getBytes(US_ASCII));

    DataSmResp dataSmResp = decomposer.dataSmResp(data.toByteArray());
    assertEquals(dataSmResp.getCommandLength(), data.size());
    assertEquals(dataSmResp.getCommandLength(), 25);
    assertEquals(dataSmResp.getCommandId(), 0x80000103);
    assertEquals(dataSmResp.getCommandStatus(), 20);
    assertEquals(dataSmResp.getSequenceNumber(), 0x1234);
    assertEquals(dataSmResp.getMessageId(), "01234567");
  }

  @Test
  public void decompose_replace_sm() throws Exception {
    ByteArrayOutputStream data = new ByteArrayOutputStream();
    // command_length
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x54 });
    // command_id
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x07 });
    // command_status
    data.write(new byte[]{ 0x00, 0x00, 0x00, 0x00 });
    // sequence_number
    data.write(new byte[]{ 0x00, 0x00, 0x12, 0x34 });
    // message_id
    data.write("32f53129-6a87-422c-8954-145f603582ed\000".getBytes(US_ASCII));
    // source_addr_ton
    data.write(1);
    // source_addr_npi
    data.write(2);
    // source_addr
    data.write("31612345678\000".getBytes(US_ASCII));
    // schedule_delivery_time
    data.write(0);
    // validity_period
    data.write(0);
    // registered_delivery
    data.write(4);
    // sm_default_msg_id
    data.write(5);
    // sm_length
    data.write(12);
    // short_message
    data.write("Hello World!".getBytes(US_ASCII));

    ReplaceSm replaceSm = decomposer.replaceSm(data.toByteArray());
    assertEquals(replaceSm.getCommandLength(), 84);
    assertEquals(replaceSm.getCommandId(), 7);
    assertEquals(replaceSm.getCommandStatus(), 0);
    assertEquals(replaceSm.getSequenceNumber(), 0x1234);
    assertEquals(replaceSm.getMessageId(), "32f53129-6a87-422c-8954-145f603582ed");
    assertEquals(replaceSm.getSourceAddrTon(), (byte) 0x01);
    assertEquals(replaceSm.getSourceAddrNpi(), (byte) 0x02);
    assertEquals(replaceSm.getSourceAddr(), "31612345678");
    assertNull(replaceSm.getScheduleDeliveryTime());
    assertNull(replaceSm.getValidityPeriod());
    assertEquals(replaceSm.getRegisteredDelivery(), (byte) 0x04);
    assertEquals(replaceSm.getSmDefaultMsgId(), (byte) 0x05);
    assertEquals(new String(replaceSm.getShortMessage(), US_ASCII), "Hello World!");
  }

}