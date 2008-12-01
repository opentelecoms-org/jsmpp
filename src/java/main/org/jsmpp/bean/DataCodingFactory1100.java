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
 * Message Waiting Indication Group: Discard Message. Message may be discarded.
 * 
 * @author uudashr
 * 
 */
public class DataCodingFactory1100 extends AbstractDataCodingFactory {
    /**
     * bin: 1111xxxx
     */
    public static final byte MASK = (byte)0xf0; //
    
    /**
     * bin: 1100xxxx
     */
    public static final byte GROUP = (byte)0xc0; // 
    
    public DataCodingFactory1100() {
        super(MASK, GROUP);
    }
    
    public DataCoding newInstance(byte dataCoding) {
        /*
         * Mobile may discard the contents of the message, and only present the
         * indication to the user.
         */
        IndicationSense indicationSense = IndicationSense.parseDataCoding(dataCoding);
        IndicationType indicationType = IndicationType.parseDataCoding(dataCoding);
        return new MessageWaitingDataCoding(indicationSense, indicationType, null);
    }
}
