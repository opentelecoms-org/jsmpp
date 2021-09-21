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
 * General Data Coding implements {@link DataCoding}
 *
 * SMPP 3.3 uses the GSM 03.38 SMS DCS. For SMPP 3.4 and later,
 * the data_coding is preferable only used for character code settings. These are the values 0x00 till 0x0f,
 * whereby some overlap exists with the GSM, TDMA and CDMA defined values.
 *
 * @author uudashr
 */
public class GeneralDataCoding implements DataCoding {
    public static final GeneralDataCoding DEFAULT = new GeneralDataCoding();

    private final Alphabet alphabet;
    private final MessageClass messageClass;
    private final boolean compressed;

    public GeneralDataCoding() {
        this(Alphabet.ALPHA_DEFAULT);
    }

    /**
     * Used when using the SMPP 3.4/5.0 DCS for character set only.
     *
     * @param alphabet is the alphabet
     *
     */
    public GeneralDataCoding(Alphabet alphabet) {
        this(alphabet, null);
    }

    public GeneralDataCoding(Alphabet alphabet, MessageClass messageClass) {
        this(alphabet, messageClass, false);
    }

    /**
     * Construct the GeneralDataCoding with specified alphabet, messageClass and
     * compression flag.
     *
     * @param alphabet is the alphabet
     * @param messageClass is the message class. This is nullable. If
     *        <code>null</code> means the DataCoding doesn't have meaning
     *        MessageClass.
     * @param compressed is the compression flag. Value is {@code true} if the user message is compressed, otherwise set to {@code false}.
     * @throws IllegalArgumentException if the alphabet is {@code null}, since alphabet is mandatory.
     */
    public GeneralDataCoding(Alphabet alphabet, MessageClass messageClass,
            boolean compressed) throws IllegalArgumentException {
        if (alphabet == null) {
            throw new IllegalArgumentException("Alphabet is mandatory, can't be null");
        }
        if (messageClass != null && (alphabet != Alphabet.ALPHA_DEFAULT && alphabet != Alphabet.ALPHA_8_BIT && alphabet != Alphabet.ALPHA_UCS2 && alphabet != Alphabet.ALPHA_RESERVED_12)) {
            throw new IllegalArgumentException("Alphabet is not supported, only default, 8-bit, UCS-2 are allowed with message class specified");
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
        return "DataCoding:" + (toByte() & 0xff);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GeneralDataCoding)) {
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
