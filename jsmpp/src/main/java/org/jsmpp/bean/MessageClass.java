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
 * @author uudashr
 * 
 */
public enum MessageClass {
    CLASS0((byte) 0x00), 
    CLASS1((byte) 0x01), 
    CLASS2((byte) 0x02), 
    CLASS3((byte) 0x03);

    public static final byte MASK_MESSAGE_CLASS = 0x03; // bin: 00000011

    private final byte value;

    MessageClass(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public static MessageClass valueOf(byte value) {
        for (MessageClass val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException(
                "No enum const MessageClass with value " + value);
    }
    
    public static MessageClass parseDataCoding(byte dataCoding) {
        byte value = (byte)(dataCoding & MASK_MESSAGE_CLASS);
        for (MessageClass val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException(
                "No enum const MessageClass with value " + value + " from dataCoding " + dataCoding);
    }
}
