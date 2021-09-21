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
 * 
 * @author uudashr
 * 
 */
public enum SMEOriginatedAcknowledgement {
    DEFAULT((byte)0x00), // xxxx00xx
    DELIVERY((byte)0x04), // xxxx01xx
    MANUAL((byte)0x08),  // xxxx10xx
    DELIVERY_MANUAL((byte)0x0c); // xxxx11xx
    
    public static final byte CLEAR_BYTE = (byte)0xf3; // 11110011
    public static final byte MASK_BYTE = 0x0c; // 00001100

    private final byte value;

    SMEOriginatedAcknowledgement(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public boolean containedIn(RegisteredDelivery registeredDelivery) {
        return containedIn(registeredDelivery.value());
    }
    
    public boolean containedIn(byte registeredDelivery) {
        return this.value == (byte)(registeredDelivery & MASK_BYTE);
    }
    
    public static byte compose(byte registeredDelivery, SMEOriginatedAcknowledgement smeOriginatedAcknowledgement) {
        return (byte)(clean(registeredDelivery) | smeOriginatedAcknowledgement.value());
    }
    
    public static byte clean(byte registeredDelivery) {
        return (byte) (registeredDelivery & CLEAR_BYTE);
    }
}
