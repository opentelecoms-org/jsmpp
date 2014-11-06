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
 * This General Data Coding. 
 * 
 * @author uudashr
 *
 */
public class GeneralDataCoding implements DataCoding {
    public static final GeneralDataCoding DEFAULT = new GeneralDataCoding();
    
    private final boolean compressed;
    private final Alphabet alphabet;
    private final MessageClass messageClass;
    
    public GeneralDataCoding() {
        this(Alphabet.ALPHA_DEFAULT);
    }

    public GeneralDataCoding(Alphabet alphabet) {
        this(alphabet, null);
    }
    
    public GeneralDataCoding(Alphabet alphabet, ESMClass esmClass) {
        this(alphabet, null, false);
    }

    /**
     * Construct the GeneralDataCoding with specified alphabet, messageClass and
     * compression flag.
     * 
     * @param alphabet is the alphabet.
     * @param messageClass is the message class. This is nullable. If
     *        <code>null</code> means the DataCoding doesn't has meaning
     *        MessageClass.
     * @param compressed is the compression flag. Value is
     *        <code>true</tt> if the user message is compressed, otherwise set to <code>false</code>.
     * 
     * @throws IllegalArgumentException if the alphabet is <code>null</code>,
     *         since Alphabet is mandatory.
     */
    public GeneralDataCoding(Alphabet alphabet, MessageClass messageClass,
            boolean compressed) throws IllegalArgumentException {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet is mandatory, can't be null");
        }
        this.alphabet = alphabet;
        this.messageClass = messageClass;
        this.compressed = compressed;
    }
    
    public Alphabet getAlphabet() {
        return alphabet;
    }
    
    public MessageClass getMessageClass() {
        return messageClass;
    }
    
    public boolean isCompressed() {
        return compressed;
    }
    
    public byte toByte() {
        byte value = compressed ? DataCodingFactory00xx.MASK_COMPRESSED : 0;
        value |= alphabet.value();
        if (messageClass != null) {
            value |= DataCodings.MASK_CONTAIN_MESSAGE_CLASS;
            value |= messageClass.value();
        }
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((alphabet == null) ? 0 : alphabet.hashCode());
        result = prime * result + (compressed ? 1231 : 1237);
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
        GeneralDataCoding other = (GeneralDataCoding)obj;
        if (alphabet == null) {
            if (other.alphabet != null)
                return false;
        } else if (!alphabet.equals(other.alphabet))
            return false;
        if (compressed != other.compressed)
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
