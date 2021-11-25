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
package org.jsmpp.bean.pid;

import java.util.Objects;

/**
 * TP-Protocol Identifier
 * <p>
 * The Protocol-Identifier is the information element by which the SM-TL either refers to the higher layer protocol being used, or indicates interworking with a
 * certain type of telematic device. The Protocol-Identifier information element makes use of a particular field in the message types SMS-SUBMIT,
 * SMS-SUBMIT-REPORT for RP-ACK, SMS-DELIVER DELIVER, SMS-DELIVER-REPORT for RP-ACK, SMS_STATUS_REPORT and SMS-COMMAND TP-Protocol-Identifier (TP-PID).
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme (kohme@gigsky.com)</a>
 */
public class ProtocolIdentifier00 implements ProtocolIdentifier {

  /**
   * The telematic working indicator.
   */
  private TelematicInterworkingIndicator telematicInterworkingIndicator;

  /**
   * The telematic device.
   */
  private TelematicDevice telematicDevice;

  /**
   * Constructor for {@link ProtocolIdentifier00}.
   */
  public ProtocolIdentifier00() {
    this.telematicInterworkingIndicator = TelematicInterworkingIndicator.NO_INTERWORKING;
  }

  /**
   * Constructor for {@link ProtocolIdentifier00(TelematicDevice)}.
   *
   * @param telematicDevice The telematic device.
   */
  public ProtocolIdentifier00(TelematicDevice telematicDevice) {
    this.telematicInterworkingIndicator = TelematicInterworkingIndicator.INTERWORKING;
    this.telematicDevice = telematicDevice;
  }

  /**
   * Get the protocol identifier from the byte encoding.
   *
   * @param encoding The encoding.
   * @return the protocol identifier.
   */
  public static ProtocolIdentifier00 valueOf(byte encoding) {
    if (encoding < 0 || encoding >= 0x40) {
      throw new IllegalArgumentException("encoding invalid");
    }
    ProtocolIdentifier00 protocolIdentifier = null;
    switch (TelematicInterworkingIndicator.valueOf(encoding)) {
      case NO_INTERWORKING:
        protocolIdentifier = new ProtocolIdentifier00();
        break;
      case INTERWORKING:
        protocolIdentifier = new ProtocolIdentifier00(TelematicDevice.valueOf(encoding));
    }
    return protocolIdentifier;
  }

  /**
   * Get the byte encoding.
   *
   * @return the byte encoding.
   */
  public byte toByte() {
    switch (telematicInterworkingIndicator) {
      case NO_INTERWORKING:
        return telematicInterworkingIndicator.value();
      case INTERWORKING:
        return (byte) (telematicInterworkingIndicator.value() | telematicDevice.value());
      default:
        throw new IllegalStateException("Telematic interworking indicator is not set");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ProtocolIdentifier00 that = (ProtocolIdentifier00) o;
    if (telematicDevice != that.telematicDevice) {
      return false;
    }
    if (telematicInterworkingIndicator != that.telematicInterworkingIndicator) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hash(telematicInterworkingIndicator, telematicDevice);
  }
}
