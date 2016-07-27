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

import org.jsmpp.SMPPConstant;

/**
 * Enum represent the interface version of SMPP.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public enum InterfaceVersion {
    /**
     * Interface Version for SMPP version 3.3.
     */
    IF_33(SMPPConstant.IF_VERSION_33),

    /**
     * Interface Version for SMPP version 3.4.
     */
    IF_34(SMPPConstant.IF_VERSION_34),

    /**
     * Interface Version for SMPP version 5.0
     */
    IF_50(SMPPConstant.IF_VERSION_50);

    private byte value;

    InterfaceVersion(byte value) {
        this.value = value;
    }

    /**
     * Get the value of interface version as defined on SMPP spesification.
     * 
     * @return the value of interface version.
     */
    public byte value() {
        return value;
    }

    /**
     * Get the <tt>InterfaceVersion</tt> by specified value.
     * 
     * @param value is the value associated by the enum constant.
     * @return the enum const assiciated with specified <tt>value</tt>.
     * @throws IllegalArgumentException if there is <tt>InterfaceVersion</tt>
     *         associated with specified <tt>value</tt>.
     */
    public static InterfaceVersion valueOf(byte value)
            throws IllegalArgumentException {
        for (InterfaceVersion val : values()) {
            if (val.value == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const InterfaceVersion with value " + value);
    }
}
