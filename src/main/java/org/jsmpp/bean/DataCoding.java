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
 * This is class of <tt>DataCoding</tt>
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public abstract class DataCoding {

    /**
     * Create new instance of data coding with specified value.
     * 
     * @param value is the value.
     * @return the <tt>DataCoding</tt> based on specified value. 
     */
    public static final DataCoding newInstance(int value) {
        byte byteValue = (byte) value;
        if (GeneralDataCoding.isCompatible(byteValue)) {
            return new GeneralDataCoding(byteValue);
        } else if (DataCoding1111.isCompatible(byteValue)) {
            return new DataCoding1111(byteValue);
        } else {
            return null;
        }
    }

    /**
     * Create new instance of data coding with specified value.
     * 
     * @param value is the value.
     * @return <tt>DataCoding</tt> based on specified value.
     */
    public static final DataCoding newInstance(byte value) {
        if (GeneralDataCoding.isCompatible(value)) {
            return new GeneralDataCoding(value);
        } else if (DataCoding1111.isCompatible(value)) {
            return new DataCoding1111(value);
        } else {
            return null;
        }
    }

    protected byte value;

    /**
     * Default constructor.
     */
    public DataCoding() {
        value = 0;
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the data coding value.
     */
    public DataCoding(int value) {
        this.value = (byte) value;
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the data coding value.
     */
    public DataCoding(byte value) {
        this.value = value;
    }

    /**
     * Get the value of data coding.
     * 
     * @return the data coding value.
     */
    public byte value() {
        return value;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final DataCoding other = (DataCoding)obj;
        if (value != other.value)
            return false;
        return true;
    }
}
