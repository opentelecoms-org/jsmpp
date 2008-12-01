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
 *
 */
public class MessageWaitingDataCoding implements DataCoding {
    
    private final IndicationSense indicationSense;
    private final IndicationType indicationType;
    private final Alphabet alphabet;
    
    public MessageWaitingDataCoding(IndicationSense indicationSense,
            IndicationType indicationType, Alphabet alphabet) {
        if (alphabet != null && (!alphabet.equals(Alphabet.ALPHA_DEFAULT)
                || !alphabet.equals(Alphabet.ALPHA_8_BIT))) {
            throw new IllegalArgumentException(
                    "Supported alphabet for SimpleDataCoding is "
                            + Alphabet.ALPHA_DEFAULT + " or "
                            + Alphabet.ALPHA_8_BIT + " only");
        }
        this.indicationSense = indicationSense;
        this.indicationType = indicationType;
        this.alphabet = alphabet;
    }
    
    public boolean isStoreMessage() {
        return alphabet != null;
    }
    
    public Alphabet getAlphabet() {
        return alphabet;
    }
    
    public IndicationSense getIndicationSense() {
        return indicationSense;
    }
    
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
}
