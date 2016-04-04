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
 * The enumerated state for short message.
 * 
 * @author uudashr
 * 
 */
public enum MessageState {
    /**
     * The message is in scheduled state.
     */
    SCHEDULED((byte) 0x00),
    /**
     * The message is in enroute state.
     */
    ENROUTE((byte) 0x01),
    /**
     * Message is delivered to destination.
     */
    DELIVERED((byte) 0x02),
    /**
     * Message validity period has expired.
     */
    EXPIRED((byte) 0x03),
    /**
     * Message has been deleted.
     */
    DELETED((byte) 0x04),
    /**
     * Message is undeliverable.
     */
    UNDELIVERABLE((byte) 0x05),
    /**
     * Message is in accepted state.
     */
    ACCEPTED((byte) 0x06),
    /**
     * Message is in invalid state.
     */
    UNKNOWN((byte) 0x07),
    /**
     * Message is in rejected state.
     */
    REJECTED((byte) 0x08),
    /**
     * Message is in skipped state.
     */
    SKIPPED((byte) 0x09);

    private final byte value;

    MessageState(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public static MessageState valueOf(byte value) {
        for (MessageState val : values()) {
            if (val.value() == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const MessageState with value " + value);
    }
}
