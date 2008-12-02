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
 * data is coded in the Default Alphabet.
 * 
 * @author uudashr
 * @see Alphabet#ALPHA_DEFAULT
 * 
 */
public class DataCodingFactory1101 extends AbstractDataCodingFactory {
    /**
     * bin: 1111xxxx
     */
    public static final byte MASK = (byte)0xf0;
    
    /**
     * bin: 1101xxxx
     */
    public static final byte GROUP = (byte)0xd0; 
    
    public DataCodingFactory1101() {
        super(MASK, GROUP);
    }
    
    public DataCoding newInstance(byte dataCoding) {
        /*
         * Mobile shall store the text message of the SMS in addition to setting
         * indication
         */
        IndicationSense indicationSense = IndicationSense.parseDataCoding(dataCoding);
        IndicationType indicationType = IndicationType.parseDataCoding(dataCoding);
        return new MessageWaitingDataCoding(indicationSense, indicationType, Alphabet.ALPHA_DEFAULT);
    }
}
