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
 * Message Waiting Indication Group: Store Message. Text included in the user
 * data is coded in uncompressed UCS2 alphabet.
 * 
 * @author uudashr
 * @see Alphabet#ALPHA_UCS2
 * 
 */
public class DataCodingFactory1110 extends AbstractDataCodingFactory {
    
    /**
     * bin: 1111xxxx
     */
    public static final byte MASK = (byte)0xf0;
    
    /**
     * bin: 1110xxxx
     */
    public static final byte GROUP = (byte)0xe0;
    
    public DataCodingFactory1110() {
        super(MASK, GROUP);
    }
    
    public DataCoding newInstance(byte dataCoding) {
        // it's uncompressed UCS2
        IndicationSense indicationSense = IndicationSense.parseDataCoding(dataCoding);
        IndicationType indicationType = IndicationType.parseDataCoding(dataCoding);
        return new MessageWaitingDataCoding(indicationSense, indicationType, Alphabet.ALPHA_UCS2);
    }
}
