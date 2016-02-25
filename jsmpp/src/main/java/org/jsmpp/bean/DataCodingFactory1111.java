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
public class DataCodingFactory1111 extends AbstractDataCodingFactory {
    /**
     * bin: 1111xxxx
     */
    public static final byte MASK = (byte)0xf0;
    
    /**
     * bin: 1111xxxx
     */
    public static final byte GROUP = (byte)0xf0;
    
    /**
     * bin: 11110111
     */
    private static final byte MASK_BIT3_REMOVAL = (byte)0xf7;
    
    public DataCodingFactory1111() {
        super(MASK, GROUP);
    }
    
    public DataCoding newInstance(byte dataCoding) {
        // bit 3 is reserved, and set to 0
        
        byte fixedDataCoding = (byte)(MASK_BIT3_REMOVAL & dataCoding);
        Alphabet alphabet = Alphabet.parseDataCoding(fixedDataCoding);
        MessageClass messageClass = MessageClass.parseDataCoding(fixedDataCoding);
        return new SimpleDataCoding(alphabet, messageClass);
    }
}
