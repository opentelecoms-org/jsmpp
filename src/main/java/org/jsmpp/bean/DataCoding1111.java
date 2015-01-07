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
 * Data Coding with coding group bits 7..4 -> 1111
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class DataCoding1111 extends DataCoding {
    private static final byte MASK_CODING_GROUP = (byte)0xf0; // 11110000
    private static final byte MASK_MESSAGE_CLASS = MessageClass.MASK_MESSAGE_CLASS;
    private static final byte MASK_ALPHABET = Alphabet.MASK_ALPHABET;

    /**
     * Default constructor.
     */
    public DataCoding1111() {
        super();
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the value.
     */
    public DataCoding1111(byte value) {
        super(value);
    }
    
    public DataCoding1111(Alphabet alphabet, MessageClass messageClass) {
        super();
        setAlphabet(alphabet);
        setMessageClass(messageClass);
    }
    
    /**
     * Construct with specified value.
     * 
     * @param value is the value.
     */
    public DataCoding1111(int value) {
        super(value);
    }

    /**
     * Get the message class of current data coding.
     * 
     * @return the {@link MessageClass}.
     */
    public MessageClass getMessageClass() {
        return MessageClass.valueOf((byte)(value & MASK_MESSAGE_CLASS));
    }

    /**
     * Compose data coding with specified message class.
     * 
     * @param messageClass is the message class.
     */
    public void setMessageClass(MessageClass messageClass) {
        byte tmp = cleanMessageClass(value);
        value = (byte)(tmp | messageClass.value());
    }

    /**
     * Clean the message class bytes.
     * 
     * @param value is the source of message class to clean.
     * @return the value without message class bytes.
     */
    private static final byte cleanMessageClass(byte value) {
        return (byte)(value & (MASK_MESSAGE_CLASS ^ 0xff));
    }

    /**
     * Get the alphabet value.
     * 
     * @return the alphabet value.
     */
    public Alphabet getAlphabet() {
        return Alphabet.valueOf((byte)(value & MASK_ALPHABET));
    }

    /**
     * Compose data coding with specified alphabet.
     * 
     * @param alphabet is the {@link Alphabet}
     */
    public void setAlphabet(Alphabet alphabet) {
        byte tmp = cleanAlphabet(value);
        value = (byte)(tmp | alphabet.value());
    }

    /**
     * Clean the alphabet bytes.
     * 
     * @param value is the value of data coding to clean.
     * @return the clean data coding.
     */
    private static final byte cleanAlphabet(byte value) {
        return (byte)(value & (MASK_ALPHABET ^ 0xff));
    }

    public static boolean isCompatible(byte dataCodingValue) {
        return (dataCodingValue & MASK_CODING_GROUP) == MASK_CODING_GROUP;
    }
    
    @Override
    public String toString() {
        return "DataCoding111-" + value;
    }
    
}
