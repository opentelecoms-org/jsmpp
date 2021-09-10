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
 * This is enum of the alphabet type.
 *
 * Alphabet represents the lower 4 bits of the data_coding field in the PDU,
 * as specified in section 5.2.19 of the SMPP v3.4 specification and
 * in section 4.7.7 of the SMPP 5.0 specification.
 *
 * @author uudashr
 * @version 1.0
 */
public enum Alphabet {

    /**
     * SMSC alphabet default
     */
    ALPHA_DEFAULT((byte)0x00, true, false),

    /**
     * IA5 (CCITT T.50)/ASCII (ANSI X3.4)
     */
    ALPHA_IA5((byte)0x01, true, false),

    /**
     * 8-bit binary octet unspecified coding.
     */
    ALPHA_UNSPECIFIED_2((byte)0x02, true, true),

    /**
     * Latin 1 (ISO-8859-1)
     */
    ALPHA_LATIN1((byte)0x03, true, false),

    /**
     * 8-bit binary octet unspecified coding.
     */
    ALPHA_8_BIT((byte)0x04, true, true),

    /**
     * JIS (X 0208-1990)
     */
    ALPHA_JIS((byte)0x05, true, false),

    /**
     * Cyrillic (ISO-8859-5)
     */
    ALPHA_CYRILLIC((byte)0x06, true, false),

    /**
     * Latin/Hebrew (ISO-8859-8)
     */
    ALPHA_LATIN_HEBREW((byte)0x07, true, false),

    /**
     * UCS2 alphabet coding (16-bit)
     */
    ALPHA_UCS2((byte)0x08, true, false),

    /**
     * Pictogram Encoding
     */
    ALPHA_PICTOGRAM_ENCODING((byte)0x09, true, false),

    /**
     * ISO-2022-JP (Music Codes)
     */
    ALPHA_ISO_2022_JP_MUSIC_CODES((byte)0x0a, true, false),

    /**
     * Unused.
     */
    ALPHA_RESERVED_11((byte)0x0b, false, false),

    /**
     * Unused.
     */
    ALPHA_RESERVED_12((byte)0x0c, false, false),

    /**
     * Extended Kanji JIS(X 0212-1990)
     */
    ALPHA_JIS_X_0212_1990((byte)0x0d, true, false),

    /**
     * KS C 5601 (now known as KS X 1001 but referred to
     * by the old name in the SMPP v3.4 spec)
     */
    ALPHA_KS_C_5601((byte)0x0e, true, false),

    /**
     * Unused
     */
    ALPHA_RESERVED_15((byte)0x0f, false, false);


    /**
     * Is the MASK of alphabet (00001111)
     */
    public static final byte MASK_ALPHABET = 0x0f; // bin: 00001111

    /**
     * Is the MASK of alphabet when message class is present (00001100)
     */
    public static final byte MASK_ALPHABET_MESSAGE_CLASS = 0x0c; // bin: 00001100

    private final byte value;
    private final boolean valid;
    private final boolean unspecified;

    /**
     * Default constructor.
     *
     * @param value is the alphabet value
     * @param valid is the alphabet validity
     * @param unspecified is the unspecified value
     */
    Alphabet(byte value, boolean valid, boolean unspecified) {
        this.value = value;
        this.valid = valid;
        this.unspecified = unspecified;
    }

    /**
     * Get the alphabet value.
     *
     * @return the alphabet value.
     */
    public byte value() {
        return value;
    }

    /**
     * Check if this is a valid alphabet or a reserved alphabet index.
     *
     * @return true if valid, false if reserved
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Check if this is a valid alphabet or a reserved alphabet index.
     *
     * @return true if reserved, false if known
     */
    public boolean isReserved() {
        return !isValid();
    }

    /**
     * Check if this is a genuine alphabet or if it signifies binary
     * data with unspecified meaning.
     *
     * @return true if this alphabet code does not correspond to a specific alphabet
     */
    public boolean isUnspecified() {
        return unspecified;
    }

    /**
     * Get the enum constant associated with specified value.
     *
     * @param value is the value associated with the {@code Alphabet} enum constant.
     * @return the associated enum constant.
     * @throws IllegalArgumentException if there is no associated enum constant
     *         for given value.
     */
    public static Alphabet valueOf(byte value) throws IllegalArgumentException {
        for (Alphabet val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const Alphabet with value "
                + value);
    }

    public static Alphabet parseDataCoding(byte dataCoding) throws IllegalArgumentException {
        byte mask = DataCodings.containsMessageClass(dataCoding) ? MASK_ALPHABET_MESSAGE_CLASS : MASK_ALPHABET;
        byte value = (byte)(dataCoding & mask);
        for (Alphabet val : values()) {
            if (val.value == value) {
                return val;
            }
        }
        throw new IllegalArgumentException("No enum const Alphabet with value "
                + value + " for dataCoding " + dataCoding);
    }

}
