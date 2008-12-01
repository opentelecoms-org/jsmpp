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
    
    public SimpleDataCoding() {
        this(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0);
    }
    
    public SimpleDataCoding(Alphabet alphabet, MessageClass messageClass) throws IllegalArgumentException {
        if (alphabet == null) {
            throw new IllegalArgumentException(
                    "alphabet is mandatory, can't be null");
        }
        if (!alphabet.equals(Alphabet.ALPHA_DEFAULT)
                || !alphabet.equals(Alphabet.ALPHA_8_BIT)) {
            throw new IllegalArgumentException(
                    "Supported alphabet for SimpleDataCoding is "
                            + Alphabet.ALPHA_DEFAULT + " or "
                            + Alphabet.ALPHA_8_BIT + " only");
        }
        if (messageClass == null) {
            throw new IllegalArgumentException(
                    "messageClass is mandatory, can't be null");
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
}
