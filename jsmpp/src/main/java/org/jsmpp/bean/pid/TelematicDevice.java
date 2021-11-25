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

import org.jsmpp.util.HexUtil;

/**
 * Telematic Devices.
 * <p>
 * In the case of telematic interworking, the following five bit patterns in bits 4..0 are used to indicate different types of telematic devices:
 * <p>
 * 4.. .0 00000 implicit - device type is specific to this SC, or can be concluded on the basis of the address 00001 telex (or teletex reduced to telex format)
 * 00010 group 3 telefax 00011 group 4 telefax 00100 voice telephone (i.e. conversion to speech) 00101 ERMES (European Radio Messaging System) 00110 National
 * Paging system (known to the SC) 00111 Videotex (T.100 [20] /T.101 [21]) 01000 teletex, carrier unspecified 01001 teletex, in PSPDN 01010 teletex, in CSPDN
 * 01011 teletex, in analog PSTN 01100 teletex, in digital ISDN 01101 UCI (Universal Computer Interface, ETSI DE/PS 3 01-3) 01110..01111 (reserved, 2
 * combinations) 10000 a message handling facility (known to the SC) 10001 any public X.400-based message handling system 10010 Internet Electronic Mail
 * 10011..10111 (reserved, 5 combinations) 11000..11110 values specific to each SC, usage based on mutual agreement between the SME and the SC (7 combinations
 * available for each SC) 11111 A GSM/UMTS mobile station. The SC converts the SM from the received TP-Data-Coding-Scheme to any data coding scheme supported by
 * that MS (e.g. the default).
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme (kohme@gigsky.com)</a>
 */
public enum TelematicDevice {
  /**
   * implicit - device type is specific to this SC, or can be concluded on the basis of the address
   */
  IMPLICIT((byte) 0x00),
  /**
   * telex (or teletex reduced to telex format)
   */
  TELEX((byte) 0x01),
  /**
   * group 3 telefax
   */
  GRP_3_TELEFAX((byte) 0x02),
  /**
   * group 4 telefax
   */
  GRP_4_TELEFAX((byte) 0x03),
  /**
   * voice telephone (i.e. conversion to speech)
   */
  VOICE_TELEPHONE((byte) 0x04),
  /**
   * ERMES (European Radio Messaging System)
   */
  ERMES((byte) 0x05),
  /**
   * National Paging system (known to the SC)
   */
  NATIONAL_PAGING_SYSTEM((byte) 0x06),
  /**
   * Videotex (T.100 [20] /T.101 [21])
   */
  VIDEOTEX((byte) 0x07),
  /**
   * teletex, carrier unspecified
   */
  TELETEXCARRIER_UNSPECIFIED((byte) 0x08),
  /**
   * teletex, in PSPDN
   */
  TELETEX_IN_PSPDND((byte) 0x09),
  /**
   * teletex, in CSPDN
   */
  TELETEX_IN_CSPDN((byte) 0x0a),
  /**
   * teletex, in analog PSTN
   */
  TELETEX_ANALOG_PSTN((byte) 0x0b),
  /**
   * teletex, in digital ISDN
   */
  TELETEX_DIGITAL_ISDN((byte) 0x0c),
  /**
   * UCI (Universal Computer Interface, ETSI DE/PS 3 01-3)
   */
  UCI((byte) 0x0d),
  /**
   * (reserved, 2 combinations)
   */
  RESERVED_1((byte) 0x0e),
  /**
   * (reserved, 2 combinations)
   */
  RESERVED_2((byte) 0x0f),
  /**
   * a message handling facility (known to the SC)
   */
  MESSAGE_HANDLING_FACILITY((byte) 0x10),
  /**
   * any public X.400-based message handling system
   */
  X_400_BASED((byte) 0x11),
  /**
   * Internet Electronic Mail
   */
  INTERNET_ELECTRONIC_MAIL((byte) 0x12),
  /**
   * (reserved, 5 combinations)
   */
  RESERVED_3((byte) 0x13),
  /**
   * (reserved, 5 combinations)
   */
  RESERVED_4((byte) 0x14),
  /**
   * (reserved, 5 combinations)
   */
  RESERVED_5((byte) 0x15),
  /**
   * (reserved, 5 combinations)
   */
  RESERVED_6((byte) 0x16),
  /**
   * (reserved, 5 combinations)
   */
  RESERVED_7((byte) 0x17),
  /**
   * values specific to each SC, usage based on mutual agreement between the SME and the SC (7 combinations available for each SC)
   */
  SPECIFIC_EACH_SC_1((byte) 0x18),
  SPECIFIC_EACH_SC_2((byte) 0x19),
  SPECIFIC_EACH_SC_3((byte) 0x1a),
  SPECIFIC_EACH_SC_4((byte) 0x1b),
  SPECIFIC_EACH_SC_5((byte) 0x1c),
  SPECIFIC_EACH_SC_6((byte) 0x1d),
  SPECIFIC_EACH_SC_7((byte) 0x1e),
  /**
   * A GSM/UMTS mobile station. The SC converts the SM from the received TP-Data-Coding-Scheme
   * to any data coding scheme supported by that MS (e.g. the default).
   */
  GSM_UMTS((byte) 0x1f);

  /**
   * The byte encoding of the structure.
   */
  private byte encoding;

  TelematicDevice(byte encoding) {
    this.encoding = encoding;
  }

  /**
   * Get the telematic device from the byte encoding. You can pass the full first octet to this.
   *
   * @param encoding The encoding
   * @return Telematic device object
   */
  public static TelematicDevice valueOf(byte encoding) {
    for (TelematicDevice telematicDevice : TelematicDevice.values()) {
      if (telematicDevice.encoding == (encoding & 0x1f)) {
        return telematicDevice;
      }
    }
    throw new IllegalArgumentException("Could not recognize telematic device from encoding '" + HexUtil.convertByteToHexString(encoding) + "'.");
  }

  /**
   * Get the byte encoding of the structure.
   *
   * @return the byte encoding of the structure.
   */
  public byte value() {
    return encoding;
  }
}
