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

import java.util.Objects;

/**
 * TP-Protocol Identifier
 * <p/>
 * The Protocol-Identifier is the information element by which the SM-TL either refers to the higher layer protocol being
 * used, or indicates interworking with a certain type of telematic device.
 * The Protocol-Identifier information element makes use of a particular field in the message types SMS-SUBMIT,
 * SMS-SUBMIT-REPORT for RP-ACK, SMS-DELIVER DELIVER, SMS-DELIVER-REPORT for RP-ACK,
 * SMS_STATUS_REPORT and SMS-COMMAND TP-Protocol-Identifier (TP-PID).
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme
 *         (kohme@gigsky.com)</a>
 */
public class ProtocolIdentifier {

    /**
     * The protocol identifier group.
     */
    private ProtocolIdentifierGroup protocolIdentifierGroup;

    /**
     * The telematic working indicator.
     */
    private TelematicInterworkingIndicator telematicInterworkingIndicator;

    /**
     * The telematic device.
     */
    private TelematicDevice telematicDevice;

    /**
     * The protocol message type.
     */
    private ProtocolMessageType protocolMessageType;

    /**
     * The byte encoding of the structure.
     */
    private byte encoding;

    private ProtocolIdentifier() {

    }

    /**
     * Constructor for {@link ProtocolIdentifierGroup#_00}.
     *
     * @param protocolIdentifierGroup        The protocol identifier group.
     * @param telematicInterworkingIndicator The telematic working indicator.
     * @param telematicDevice                The telematic device.
     */
    public ProtocolIdentifier(ProtocolIdentifierGroup protocolIdentifierGroup, TelematicInterworkingIndicator telematicInterworkingIndicator, TelematicDevice telematicDevice) {
        this.protocolIdentifierGroup = protocolIdentifierGroup;
        this.telematicInterworkingIndicator = telematicInterworkingIndicator;
        this.telematicDevice = telematicDevice;
        encoding = (byte) (protocolIdentifierGroup.value() | telematicInterworkingIndicator.value() | telematicDevice.value());
    }

    /**
     * Constructor for {@link ProtocolIdentifierGroup#_01}.
     *
     * @param protocolIdentifierGroup The protocol identifier group.
     * @param protocolMessageType     The protocol message type.
     */
    public ProtocolIdentifier(ProtocolIdentifierGroup protocolIdentifierGroup, ProtocolMessageType protocolMessageType) {
        this.protocolIdentifierGroup = protocolIdentifierGroup;
        this.protocolMessageType = protocolMessageType;
        encoding = (byte) (protocolIdentifierGroup.value() | protocolMessageType.value());
    }

    /**
     * Get the byte encoding.
     *
     * @return the byte encoding.
     */
    public byte value() { return encoding; }

    /**
     * Get the protocol identifier from the byte encoding.
     *
     * @param encoding The encoding.
     * @return the protocol identifier.
     */
    public static ProtocolIdentifier valueOf(byte encoding) {
        ProtocolIdentifier protocolIdentifier = new ProtocolIdentifier();
        protocolIdentifier.encoding = encoding;
        ProtocolIdentifierGroup protocolIdentifierGroup = ProtocolIdentifierGroup.valueOf(encoding);
        protocolIdentifier.protocolIdentifierGroup = protocolIdentifierGroup;
        TelematicInterworkingIndicator telematicInterworkingIndicator;
        TelematicDevice telematicDevice;
        ProtocolMessageType protocolMessageType;
        switch (protocolIdentifierGroup) {
            case _00:
                telematicInterworkingIndicator = TelematicInterworkingIndicator.valueOf(encoding);
                protocolIdentifier.telematicInterworkingIndicator = telematicInterworkingIndicator;
                switch (telematicInterworkingIndicator) {
                    case INTERWORKING:
                        telematicDevice = TelematicDevice.valueof(encoding);
                        protocolIdentifier.telematicDevice = telematicDevice;
                        break;
                }
                break;
            case _01:
                protocolMessageType = ProtocolMessageType.valueOf(encoding);
                protocolIdentifier.protocolMessageType = protocolMessageType;
                break;
            default:
                break;
        }
        return protocolIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtocolIdentifier that = (ProtocolIdentifier) o;

        if (protocolIdentifierGroup != that.protocolIdentifierGroup) return false;
        if (protocolMessageType != that.protocolMessageType) return false;
        if (telematicDevice != that.telematicDevice) return false;
        if (telematicInterworkingIndicator != that.telematicInterworkingIndicator) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(protocolIdentifierGroup, telematicInterworkingIndicator,
                telematicDevice, protocolMessageType);
    }
}
