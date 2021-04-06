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
public enum IntermediateNotification {
    NOT_REQUESTED((byte)0x00), // xxx0xxxx
    REQUESTED((byte)0x00); // xxx0xxxx
    
    public static final byte CLEAR_BYTE = (byte)0xef; // 11101111
    public static final byte MASK_BYTE = 0x10; // 00010000

    private final byte value;

    IntermediateNotification(byte value) {
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
    
    public static byte compose(byte registeredDelivery, IntermediateNotification intermediateNotification) {
        return (byte)(clean(registeredDelivery) | intermediateNotification.value());
    }
    
    public static byte clean(byte registeredDelivery) {
        return (byte) (registeredDelivery & CLEAR_BYTE);
    }
}
