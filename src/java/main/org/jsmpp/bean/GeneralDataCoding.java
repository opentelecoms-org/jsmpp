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
    
    private final boolean compressed;
    private final Alphabet alphabet;
    private final MessageClass messageClass;
    
    public GeneralDataCoding() {
        this(Alphabet.ALPHA_DEFAULT);
    }

    public GeneralDataCoding(Alphabet alphabet) {
        this(alphabet, false);
    }
    
    public GeneralDataCoding(Alphabet alphabet, boolean compressed) {
        this(alphabet, null, compressed);
    }
    
    public GeneralDataCoding(Alphabet alphabet,
            MessageClass messageClass, boolean compressed) throws IllegalArgumentException {
        if (alphabet == null) {
            throw new IllegalArgumentException("alphabet is mandatory, can't be null");
        }
        this.alphabet = alphabet;
        this.messageClass = messageClass;
        this.compressed = compressed;
    }

    public byte toByte() {
        byte value = compressed ? DataCodingFactory00xx.MASK_COMPRESSED : 0;
        value |= alphabet.value();
        if (messageClass != null) {
            value |= DataCodingFactory00xx.MASK_CONTAIN_MESSAGE_CLASS;
            value |= messageClass.value();
        }
        return value;
    }
}
