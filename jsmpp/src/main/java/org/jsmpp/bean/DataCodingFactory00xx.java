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
 * General Data Coding indication parser.
 * 
 * @author uudashr
 *
 */
public class DataCodingFactory00xx extends AbstractDataCodingFactory {
    
    /**
     * bin: 00000000
     */
    public static final byte GROUP = 0x00;
    
    /**
     * bin: 11xxxxxx
     */
    public static final byte MASK = (byte)0xc0;
    
    /**
     * bin: 00000011
     */
    public static final byte MASK_MESSAGE_CLASS = 0x03; 
    
    /**
     * bin: 00100000
     */
    public static final byte MASK_COMPRESSED = 0x20;
    
    public DataCodingFactory00xx() {
        super(MASK, GROUP);
    }
    
    public DataCoding newInstance(byte dataCoding) {
        boolean compressed = isCompressed(dataCoding);
        boolean containMessageClass = DataCodings.containsMessageClass(dataCoding);
        MessageClass messageClass = null;
        if (containMessageClass) {
            // ignore Message Class if the PDU has tell us no Message Class
            messageClass = MessageClass.parseDataCoding(dataCoding);
        }
        Alphabet alphabet = Alphabet.parseDataCoding(dataCoding);
        
        return new GeneralDataCoding(alphabet, messageClass, compressed);
    }
    
    public static boolean isCompressed(byte dataCoding) {
        return (dataCoding & MASK_COMPRESSED) == MASK_COMPRESSED;
    }

}
