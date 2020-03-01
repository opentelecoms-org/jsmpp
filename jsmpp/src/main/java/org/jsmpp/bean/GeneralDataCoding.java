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


import java.util.Objects;

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
    public String toString() {
        return "DataCoding:" + (0xff & toByte());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GeneralDataCoding that = (GeneralDataCoding) o;
        return compressed == that.compressed &&
            alphabet == that.alphabet &&
            messageClass == that.messageClass;
    }

    @Override
    public int hashCode() {
        return Objects.hash(compressed, alphabet, messageClass);
    }
}
