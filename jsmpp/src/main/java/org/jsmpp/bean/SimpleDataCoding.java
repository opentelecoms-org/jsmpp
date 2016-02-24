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
 * This is simple DataCoding. Only contains Alphabet (DEFAULT and 8-bit) and
 * Message Class.
 * 
 * @author uudashr
 * 
 */
public class SimpleDataCoding implements DataCoding {
    
    private final Alphabet alphabet;
    private final MessageClass messageClass;

    /**
     * Construct Data Coding using default Alphabet and
     * {@link MessageClass#CLASS1} Message Class.
     */
    public SimpleDataCoding() {
        this(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1);
    }

    /**
     * Construct Data Coding using specified Alphabet and Message Class.
     *  
     * @param alphabet is the alphabet. Only support
     *        {@link Alphabet#ALPHA_DEFAULT} and {@link Alphabet#ALPHA_8_BIT}.
     * @param messageClass
     * @throws IllegalArgumentException if alphabet is <tt>null</tt> or using
     *         non {@link Alphabet#ALPHA_DEFAULT} and
     *         {@link Alphabet#ALPHA_8_BIT} alphabet or
     *         <code>messageClass</code> is null.
     */
    public SimpleDataCoding(Alphabet alphabet, MessageClass messageClass) throws IllegalArgumentException {
        if (alphabet == null) {
            throw new IllegalArgumentException(
                    "Alphabet is mandatory, can't be null");
        }
        if (alphabet.equals(Alphabet.ALPHA_UCS2)
                || alphabet.isReserved()) {
            throw new IllegalArgumentException(
                    "Supported alphabet for SimpleDataCoding does not include "
                            + Alphabet.ALPHA_UCS2 + " or "
                            + "reserved alphabet codes. Current alphabet is " + alphabet);
        }
        if (messageClass == null) {
            throw new IllegalArgumentException(
                    "MessageClass is mandatory, can't be null");
        }
        this.alphabet = alphabet;
        this.messageClass = messageClass;
    }
    
    public Alphabet getAlphabet() {
        return alphabet;
    }
    
    public MessageClass getMessageClass() {
        return messageClass;
    }
    
    public byte toByte() {
        // base byte is 11110xxx or 0xf0, others injected
        byte value = (byte)0xf0;
        value |= alphabet.value();
        value |= messageClass.value();
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((alphabet == null) ? 0 : alphabet.hashCode());
        result = prime * result
                + ((messageClass == null) ? 0 : messageClass.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SimpleDataCoding other = (SimpleDataCoding)obj;
        if (alphabet == null) {
            if (other.alphabet != null)
                return false;
        } else if (!alphabet.equals(other.alphabet))
            return false;
        if (messageClass == null) {
            if (other.messageClass != null)
                return false;
        } else if (!messageClass.equals(other.messageClass))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "DataCoding:" + (0xff & toByte());
    }
}
