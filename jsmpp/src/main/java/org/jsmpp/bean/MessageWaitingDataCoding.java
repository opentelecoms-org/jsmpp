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
 * Message Waiting DataCoding is a data coding for message waiting.
 * 
 * @author uudashr
 * 
 */
public class MessageWaitingDataCoding implements DataCoding {
    
    private final IndicationSense indicationSense;
    private final IndicationType indicationType;
    private final Alphabet alphabet;

    /**
     * Construct with specified indication sense and indication type. No
     * alphabet, means the user message may discard.
     * 
     * @param indicationSense is the indication sense.
     * @param indicationType is the indication type.
     */
    public MessageWaitingDataCoding(IndicationSense indicationSense,
            IndicationType indicationType) {
        this(indicationSense, indicationType, null);
    }

    /**
     * Construct with specified indication sense, indication type and the
     * alphabet.
     * <p>
     * Possible alphabet is {@link Alphabet#ALPHA_DEFAULT},
     * {@link Alphabet#ALPHA_8_BIT} &amp; <code>null</code>, others will cause
     * construction thrown an {@link IllegalArgumentException}. If the alphabet
     * is set to <code>null</code> it means that the user message may discard.
     * </p>
     * 
     * @param indicationSense is the indication sense.
     * @param indicationType is the indication type.
     * @param alphabet
     * @throws IllegalArgumentException if alphabet is null or alphabet non one
     *         of {@link Alphabet#ALPHA_DEFAULT} and
     *         {@link Alphabet#ALPHA_8_BIT}.
     */
    public MessageWaitingDataCoding(IndicationSense indicationSense,
            IndicationType indicationType, Alphabet alphabet)
            throws IllegalArgumentException {
        if (alphabet != null && (alphabet.isUnspecified()
                || alphabet.isReserved())) {
            throw new IllegalArgumentException(
                    "Supported alphabet for SimpleDataCoding is one of "
                            + Alphabet.ALPHA_DEFAULT + ", "
                            + Alphabet.ALPHA_UNSPECIFIED_2 + " or " + Alphabet.ALPHA_8_BIT
                            + " only. Current alphabet is " + alphabet);
        }
        this.indicationSense = indicationSense;
        this.indicationType = indicationType;
        this.alphabet = alphabet;
    }

    /**
     * Get the store message flag. If it return <code>false</code> means the
     * user message may discard.
     * 
     * @return the store message flag.
     */
    public boolean isStoreMessage() {
        return alphabet != null;
    }

    /**
     * Get the alphabet. Alphabet may <code>null</code>, that means
     * {@link #isStoreMessage()} value is
     * <code>false<code> and user message may discard.
     * 
     * @return the alphabet. This value is nullable.
     */
    public Alphabet getAlphabet() {
        return alphabet;
    }
    
    /**
     * Get the indication sense.
     * 
     * @return the indication sense.
     */
    public IndicationSense getIndicationSense() {
        return indicationSense;
    }
    
    /**
     * Get the indication type.
     * 
     * @return the indication type.
     */
    public IndicationType getIndicationType() {
        return indicationType;
    }
    
    public byte toByte() {
        byte value = 0;
        if (!isStoreMessage()) {
            value = DataCodingFactory1100.GROUP;
        } else if (alphabet.equals(Alphabet.ALPHA_DEFAULT)) {
            value = DataCodingFactory1101.GROUP;
        } else if (alphabet.equals(Alphabet.ALPHA_UCS2)) {
            value = DataCodingFactory1110.GROUP;
        }
        value |= indicationSense.value();
        value |= indicationType.value();
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((alphabet == null) ? 0 : alphabet.hashCode());
        result = prime * result
                + ((indicationSense == null) ? 0 : indicationSense.hashCode());
        result = prime * result
                + ((indicationType == null) ? 0 : indicationType.hashCode());
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
        MessageWaitingDataCoding other = (MessageWaitingDataCoding)obj;
        if (alphabet == null) {
            if (other.alphabet != null)
                return false;
        } else if (!alphabet.equals(other.alphabet))
            return false;
        if (indicationSense == null) {
            if (other.indicationSense != null)
                return false;
        } else if (!indicationSense.equals(other.indicationSense))
            return false;
        if (indicationType == null) {
            if (other.indicationType != null)
                return false;
        } else if (!indicationType.equals(other.indicationType))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "DataCoding:" + (0xff & toByte());
    }
}
