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

import java.util.ArrayList;
import java.util.List;


/**
 * @author uudashr
 */
public final class DataCodings {

    /**
     * bin: 00010000
     */
    public static final byte MASK_CONTAIN_MESSAGE_CLASS = 0x10;

    private static final List<DataCodingFactory> factories = new ArrayList<>();
    static {
        factories.add(new DataCodingFactory00xx());
        factories.add(new DataCodingFactory1100());
        factories.add(new DataCodingFactory1101());
        factories.add(new DataCodingFactory1110());
        factories.add(new DataCodingFactory1111());
    }
    
    /**
     * DataCoding with binary value 0000000.
     */
    public static final DataCoding ZERO = new GeneralDataCoding();

    private DataCodings() {
        throw new InstantiationError("This class must not be instantiated");
    }

    /**
     * Create new instance of {@link DataCoding}.
     * 
     * @param dataCoding the data coding as byte
     * @return the DataCoding object
     */
    public static DataCoding newInstance(byte dataCoding) {
        for (DataCodingFactory factory : factories) {
            if (factory.isRecognized(dataCoding)) {
                return factory.newInstance(dataCoding);
            }
        }
        return new RawDataCoding(dataCoding);
    }

    /**
     * Check if the Data Coding has the message class bit set
     *
     * @param dataCoding the data coding scheme
     * @return {@code true} if the data coding contains a message class
     */
    public static boolean containsMessageClass(byte dataCoding) {
        return (dataCoding & MASK_CONTAIN_MESSAGE_CLASS) == MASK_CONTAIN_MESSAGE_CLASS;
    }
}
