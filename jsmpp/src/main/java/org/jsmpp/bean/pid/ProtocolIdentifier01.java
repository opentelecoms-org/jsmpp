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

import java.util.Objects;

/**
 * TP-Protocol Identifier
 * <p>
 * The Protocol-Identifier is the information element by which the SM-TL either refers to the higher layer protocol being used, or indicates interworking with a
 * certain type of telematic device. The Protocol-Identifier information element makes use of a particular field in the message types SMS-SUBMIT,
 * SMS-SUBMIT-REPORT for RP-ACK, SMS-DELIVER DELIVER, SMS-DELIVER-REPORT for RP-ACK, SMS_STATUS_REPORT and SMS-COMMAND TP-Protocol-Identifier (TP-PID).
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme
 * (kohme@gigsky.com)</a>
 */
public class ProtocolIdentifier01 implements ProtocolIdentifier {

  /**
   * The protocol message type.
   */
  private ProtocolMessageType protocolMessageType;

  private ProtocolIdentifier01() {
  }

  /**
   * Constructor for {@link ProtocolIdentifier01}.
   *
   * @param protocolMessageType The protocol message type
   */
  public ProtocolIdentifier01(ProtocolMessageType protocolMessageType) {
    this.protocolMessageType = protocolMessageType;
  }

  /**
   * Get the protocol identifier from the byte encoding.
   *
   * @param encoding The encoding
   * @return the protocol identifier
   */
  public static ProtocolIdentifier01 valueOf(byte encoding) {
    return new ProtocolIdentifier01(ProtocolMessageType.valueOf(encoding));
  }

  /**
   * Get the byte encoding.
   *
   * @return the byte encoding.
   */
  public byte toByte() {
    return (byte) (0x40 | protocolMessageType.value());
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ProtocolIdentifier01)) {
      return false;
    }
    final ProtocolIdentifier01 that = (ProtocolIdentifier01) o;
    return protocolMessageType == that.protocolMessageType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(protocolMessageType);
  }
}
