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

/**
 * Protocol Identifier Group.
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme
 *         (kohme@gigsky.com)</a>
 */
public enum ProtocolIdentifierGroup {
    /**
     * 00
     */
    _00((byte) 0),
    /**
     *01
     */
    _01((byte) 0b01000000),
    /**
     * Reserved
     */
    RESERVED((byte) 0b10000000),
    /**
     * Assigns bits 0-5 for SC specific use
     */
    SC_SPECIFIC((byte) 0b10),
    /**
     * SMS-SUBMIT (in the direction MS to SC)
     */
    SMS_SUBMIT((byte) 0b01);

    /**
     * The byte encoding of the structure.
     */
    private byte encoding;

    private ProtocolIdentifierGroup(byte encoding) {
        this.encoding = encoding;
    }

    /**
     * Get the byte encoding.
     *
     * @return the byte encoding.
     */
    public byte value() { return encoding; }

    /**
     * Get the protocol identifier group from the byte encoding.
     * You can pass the full first octet to this.
     *
     * @param encoding The encoding.
     * @return the protocol identifier group.
     */
    public static ProtocolIdentifierGroup valueOf(byte encoding) {
        for (ProtocolIdentifierGroup protocolIdentifierGroup : ProtocolIdentifierGroup.values()) {
            if (protocolIdentifierGroup.encoding == (encoding & 0b11000000)) {
                return protocolIdentifierGroup;
            }
        }
        throw new IllegalArgumentException("Could not recognize protocol identifier group from encoding '" + encoding + "'.");
    }
}
