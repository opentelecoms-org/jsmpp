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
 */
public enum GSMSpecificFeature {
    DEFAULT((byte) 0x00),
    
    /**
     * User data header indicator.
     */
    UDHI((byte)0x40), 
    
    /**
     * Reply path.
     */
    REPLYPATH((byte)0x80),
    
    /**
     * User data header indicator and Reply path.
     */
    UDHI_REPLYPATH((byte) 0xc0);

    public static final byte CLEAR_BYTE = 0x3f;
    public static final byte MASK_BYTE = (byte) 0xc0;

    private final byte value;

    GSMSpecificFeature(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
    
    /**
     * Check the GSM Specific Feature is contained in ESM class value.
     * 
     * @param esmClass is the ESM class.
     * @return {@code true} if the GSM specific feature is contained in ESM class.
     */
    public boolean containedIn(ESMClass esmClass) {
        return containedIn(esmClass.value());
    }
    
    /**
     * Check the GSM Specific Feature is contained in ESM class value.
     * 
     * @param esmClass is the ESM class.
     * @return {@code true} if the GSM specific feature is contained in ESM class.
     */
    public boolean containedIn(byte esmClass) {
        return this.value == (byte)(esmClass & MASK_BYTE);
    }
    
    /**
     * Compose the existing ESM class with the GSM specific feature.
     * 
     * @param esmClass is the existing ESM class.
     * @param specificFeature the GSM specific feature.
     * @return the ESM class composed with specified GSM specific feature. 
     */
    public static byte compose(byte esmClass, GSMSpecificFeature specificFeature) {
        return (byte)(clear(esmClass) | specificFeature.value());
    }
    
    /**
     * Clear the bit 0 - 6.
     * 
     * @param esmClass is the ESM class value.
     * @return the masked value for ESM class.
     */
    public static byte clear(byte esmClass) {
        return (byte)(esmClass & CLEAR_BYTE);
    }
}
