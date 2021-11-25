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
import static org.testng.Assert.assertThrows;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProtocolIdentifier00Test {

  @Test
  public void test_protocol_identifier_00_to_byte() {
    assertEquals(new ProtocolIdentifier00().toByte(), (byte) 0x00);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.IMPLICIT).toByte(), (byte) 0x20);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.TELEX).toByte(), (byte) 0x21);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.GRP_3_TELEFAX).toByte(), (byte) 0x22);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.GRP_4_TELEFAX).toByte(), (byte) 0x23);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_1).toByte(), (byte) 0x38);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_2).toByte(), (byte) 0x39);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_7).toByte(), (byte) 0x3e);
    assertEquals(new ProtocolIdentifier00(TelematicDevice.GSM_UMTS).toByte(), (byte) 0x3f);
  }

  @Test
  public void test_protocol_identifier_00_valueOf() {
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x00), new ProtocolIdentifier00());
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x20), new ProtocolIdentifier00(TelematicDevice.IMPLICIT));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x21), new ProtocolIdentifier00(TelematicDevice.TELEX));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x22), new ProtocolIdentifier00(TelematicDevice.GRP_3_TELEFAX));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x23), new ProtocolIdentifier00(TelematicDevice.GRP_4_TELEFAX));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x38), new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_1));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x39), new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_2));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x3e), new ProtocolIdentifier00(TelematicDevice.SPECIFIC_EACH_SC_7));
    assertEquals(ProtocolIdentifier00.valueOf((byte) 0x3f), new ProtocolIdentifier00(TelematicDevice.GSM_UMTS));
  }

  @Test
  public void test_protocol_identifier_invalid() {
    assertThrows(new Assert.ThrowingRunnable() {
      @Override
      public void run() throws IllegalArgumentException {
        ProtocolIdentifier00.valueOf((byte) 0x40);
      }
    });
  }
}