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

import java.nio.charset.StandardCharsets;

import org.jsmpp.util.OctetUtil;
import org.junit.Assert;
import org.testng.annotations.Test;

public class LongSMSTest {

  @Test
  public void testSplitMessage8Bit() throws Exception {

    final String message = "Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gatew";
    final byte[][] splittedMsg = LongSMS.splitMessage8Bit(message.getBytes(StandardCharsets.US_ASCII));
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