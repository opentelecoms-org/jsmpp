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
public enum MessageType {
    DEFAULT((byte) 0x00), 
    
    // ESME -> SMSC
    ESME_DEL_ACK((byte)0x08), 
    ESME_MAN_ACK((byte)0x10),
    
    // SMSC -> ESME
    SMSC_DEL_RECEIPT((byte) 0x04), 
    SME_DEL_ACK((byte) 0x08),
    SME_MAN_ACK((byte) 0x10),
    CONV_ABORT((byte) 0x18),
    INTER_DEL_NOTIF((byte) 0x20);

    public static final byte CLEAR_BYTE = (byte) 0xc3; // 11000011
    public static final byte MASK_BYTE = 0x3c; // 00111100

    private final byte value;

    MessageType(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
    
    public boolean containedIn(ESMClass esmClass) {
        return containedIn(esmClass.value());
    }
    
    public boolean containedIn(byte esmClass) {
        return this.value == (byte)(esmClass & MASK_BYTE);
    }
    
    public static byte compose(byte esmClass, MessageType messageType) {
        return (byte)(clean(esmClass) | messageType.value());
    }
    
    public static byte clean(byte esmClass) {
        return (byte) (esmClass & CLEAR_BYTE);
    }
    
}
