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
 * Protocol message types in case {@link ProtocolIdentifier01}.
 * <p>
 * In the case where bit 7 = 0, bit 6 = 1, bits 5..0 are used as defined below 5 .. . .0 000000 Short Message Type 0 000001 Replace Short Message Type 1 000010
 * Replace Short Message Type 2 000011 Replace Short Message Type 3 000100 Replace Short Message Type 4 000101 Replace Short Message Type 5 000110 Replace Short
 * Message Type 6 000111 Replace Short Message Type 7 001000 Device Triggering Short Message 001001..011101 Reserved 011110 Enhanced Message Service (Obsolete)
 * 011111 Return Call Message 100000..111011 Reserved 111100 ANSI-136 R-DATA 111101 ME Data download 111110 ME De-personalization Short Message 111111 (U)SIM
 * Data download
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme (kohme@gigsky.com)</a>
 */
public enum ProtocolMessageType {
  /**
   * Short Message Type 0
   */
  SM_TYPE_0((byte) 0x00),
  /**
   * Replace Short Message Type 1
   */
  REPL_SM_TYPE_1((byte) 0x01),
  /**
   * Replace Short Message Type 2
   */
  REPL_SM_TYPE_2((byte) 0x02),
  /**
   * Replace Short Message Type 3
   */
  REPL_SM_TYPE_3((byte) 0x03),
  /**
   * Replace Short Message Type 41
   */
  REPL_SM_TYPE_4((byte) 0x04),
  /**
   * Replace Short Message Type 5
   */
  REPL_SM_TYPE_5((byte) 0x05),
  /**
   * Replace Short Message Type 6
   */
  REPL_SM_TYPE_6((byte) 0x06),
  /**
   * Replace Short Message Type 7
   */
  REPL_SM_TYPE_7((byte) 0x07),
  /**
   * Device Triggering Short Message
   */
  DEVICE_TRIGGERING_SM((byte) 0x08),
  /**
   * Enhanced Message Service (Obsolete)
   */
  ENHANCED_MESSAGE_SERVICE((byte) 0x1e),
  /**
   * Return Call Message
   */
  RETURN_CALL_MESSAGE((byte) 0x1f),
  /**
   * ANSI-136 R-DATA
   */
  ANSI_136_R_DATA((byte) 0x3c),
  /**
   * ME Data download
   */
  ME_DATA_DOWNLOAD((byte) 0x3d),
  /**
   * ME De-personalization Short Message
   */
  ME_DE_PERSONALIZATION_SM((byte) 0x3e),
  /**
   * (U)SIM Data download
   */
  USIM_DATA_DOWNLOAD((byte) 0x3f);

  /**
   * The byte encoding of the structure.
   */
  private byte value;

  ProtocolMessageType(byte value) {
    this.value = value;
  }

  /**
   * Get the protocol message type from the byte encoding. You can pass the full first octet to this.
   *
   * @param encoding The encoding.
   * @return the protocol message type.
   */
  public static ProtocolMessageType valueOf(byte encoding) {
    for (ProtocolMessageType protocolMessageType : ProtocolMessageType.values()) {
      if (protocolMessageType.value == (encoding & 0x3f)) {
        return protocolMessageType;
      }
    }
    throw new IllegalArgumentException("Could not recognize protocol messaging type from encoding '" + HexUtil.convertByteToHexString(encoding) + "'.");
  }

  /**
   * Get the byte encoding of the structure.
   *
   * @return the byte encoding of the structure.
   */
  public byte value() {
    return value;
  }
}
