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
 */
package org.jsmpp.bean.pid;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ProtocolIdentifier01Test {

  @Test
  public void test_protocol_identifier_01_value() {
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.SM_TYPE_0).toByte(), (byte) 0x40);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_1).toByte(), (byte) 0x41);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_2).toByte(), (byte) 0x42);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_3).toByte(), (byte) 0x43);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_4).toByte(), (byte) 0x44);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_5).toByte(), (byte) 0x45);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_6).toByte(), (byte) 0x46);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.REPL_SM_TYPE_7).toByte(), (byte) 0x47);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.DEVICE_TRIGGERING_SM).toByte(), (byte) 0x48);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.ENHANCED_MESSAGE_SERVICE).toByte(), (byte) 0x5e);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.RETURN_CALL_MESSAGE).toByte(), (byte) 0x5f);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.ANSI_136_R_DATA).toByte(), (byte) 0x7c);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.ME_DATA_DOWNLOAD).toByte(), (byte) 0x7d);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.ME_DE_PERSONALIZATION_SM).toByte(), (byte) 0x7e);
    assertEquals(new ProtocolIdentifier01(ProtocolMessageType.USIM_DATA_DOWNLOAD).toByte(), (byte) 0x7f);
  }

  @Test
  public void test_protocol_identifier_01_value_of() {
    assertEquals(ProtocolMessageType.valueOf((byte) 0x40), ProtocolMessageType.SM_TYPE_0);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x41), ProtocolMessageType.REPL_SM_TYPE_1);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x42), ProtocolMessageType.REPL_SM_TYPE_2);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x43), ProtocolMessageType.REPL_SM_TYPE_3);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x44), ProtocolMessageType.REPL_SM_TYPE_4);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x45), ProtocolMessageType.REPL_SM_TYPE_5);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x46), ProtocolMessageType.REPL_SM_TYPE_6);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x47), ProtocolMessageType.REPL_SM_TYPE_7);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x48), ProtocolMessageType.DEVICE_TRIGGERING_SM);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x5e), ProtocolMessageType.ENHANCED_MESSAGE_SERVICE);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x5f), ProtocolMessageType.RETURN_CALL_MESSAGE);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x7c), ProtocolMessageType.ANSI_136_R_DATA);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x7d), ProtocolMessageType.ME_DATA_DOWNLOAD);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x7e), ProtocolMessageType.ME_DE_PERSONALIZATION_SM);
    assertEquals(ProtocolMessageType.valueOf((byte) 0x7f), ProtocolMessageType.USIM_DATA_DOWNLOAD);
  }
}