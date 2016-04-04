package org.jsmpp.bean;

import org.jsmpp.util.OctetUtil;
import org.junit.Assert;
import org.testng.annotations.Test;

public class LongSMSTest {

  @Test
  public void testSplitMessage8Bit() throws Exception {

    final String message = "Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gatew";
    final byte[][] splittedMsg = LongSMS.splitMessage8Bit(message.getBytes("US-ASCII"));
    Assert.assertEquals(266, (message.getBytes().length));
    Assert.assertEquals(140, splittedMsg[0].length);
    Assert.assertEquals(140, splittedMsg[1].length);
    for (int i = 0; i < splittedMsg.length; i++) {
      Assert.assertEquals("UDH length", 0x06, splittedMsg[i][0]);
      Assert.assertEquals("IE identifier", 0x08, splittedMsg[i][1]);
      Assert.assertEquals("IE length", 0x04, splittedMsg[i][2]);
      Assert.assertEquals("The referencenumber differs",
          OctetUtil.bytesToShort(splittedMsg[0], 3),
          OctetUtil.bytesToShort(splittedMsg[i], 3));
      Assert.assertEquals("Total of segments differs", splittedMsg.length, splittedMsg[i][5]);
      Assert.assertEquals("The segmentnumber differs", i + 1, splittedMsg[i][6]);
    }
  }

}