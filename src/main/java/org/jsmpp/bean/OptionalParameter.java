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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.jsmpp.util.OctetUtil;

/**
 * The Optional Parameter class.
 * 
 * @author mikko.koponen
 * @author uudashr
 * 
 */
public abstract class OptionalParameter {

    public final short tag;

    public OptionalParameter(short tag) {
        this.tag = tag;
    }

    public byte[] serialize() {
        byte[] value = serializeValue();
        ByteBuffer buffer = ByteBuffer.allocate(value.length + 4);
        buffer.putShort(tag);
        buffer.putShort((short)value.length);
        buffer.put(value);
        return buffer.array();
    }

    protected abstract byte[] serializeValue();
    
    public static class Null extends OptionalParameter {
        public Null(short tag) {
            super(tag);
        }
        
        public Null(Tag tag) {
            this(tag.code());
        }
        
        @Override
        protected byte[] serializeValue() {
            return new byte[0];
        }
    }

    public static class Short extends OptionalParameter {
        private final short value;

        /**
         * Construct the Short optional parameter with specified short value.
         * 
         * @param tag
         * @param value
         */
        public Short(short tag, short value) {
            super(tag);
            this.value = value;
        }
        
        public Short(Tag tag, short value) {
            this(tag.code(), value);
        }
        
        /**
         * Construct the Short optional parameters with specified bytes value.
         * Only 2 bytes will be use as value.
         * 
         * @param tag is the tag.
         * @param value is the value.
         */
        public Short(short tag, byte[] value) {
            this(tag, OctetUtil.bytesToShort(value));
        }
        
        public short getValue() {
            return value;
        }
        
        @Override
        protected byte[] serializeValue() {
            return OctetUtil.shortToBytes(value);
        }
    }

    public static class Int extends OptionalParameter {
        private final int value;

        public Int(short tag, int value) {
            super(tag);
            this.value = value;
        }
        
        public Int(Tag tag, int value) {
            this(tag.code(), value);
        }

        public Int(short tag, byte[] content) {
            this(tag, OctetUtil.bytesToInt(content));
        }
        
        public int getValue() {
            return value;
        }
        
        @Override
        protected byte[] serializeValue() {
            return OctetUtil.intToBytes(value);
        }
    }

    public static class Byte extends OptionalParameter {
        private final byte value;

        public Byte(short tag, byte value) {
            super(tag);
            this.value = value;
        }
        
        public Byte(Tag tag, byte value) {
            this(tag.code(), value);
        }
        
        public Byte(short tag, byte[] content) {
            this(tag, content[0]);
        }
        
        public byte getValue() {
            return value;
        }
        
        @Override
        protected byte[] serializeValue() {
            return new byte[] { value };
        }
    }

    public static class OctetString extends OptionalParameter {
        private final byte[] value;
        
        public OctetString(short tag, String value) {
            super(tag);
            this.value = value.getBytes();
        }
        
        public OctetString(Tag tag, String value) {
            this(tag.code(), value);
        }
        
        
        public OctetString(short tag, String value, String charsetName)
                throws UnsupportedEncodingException {
            super(tag);
            this.value = value.getBytes(charsetName);
        }
        
        public OctetString(short tag, byte[] value) {
            super(tag);
            this.value = value;
        }

        public OctetString(short tag, byte[] value, int offset, int length) {
            super(tag);
            this.value = new byte[length];
            System.arraycopy(value, offset, this.value, offset, length);
        }
        
        public byte[] getValue() {
            return value;
        }
        
        public String getValueAsString() {
            return new String(value);
        }
        
        @Override
        protected byte[] serializeValue() {
            return value;
        }
    }

    public static class COctetString extends OctetString {

        public COctetString(short tag, String value, String charsetName)
                throws UnsupportedEncodingException {
            super(tag, value, charsetName);
        }

        public COctetString(short tag, String value) {
            super(tag, value);
        }
        
        public COctetString(short tag, byte[] value) {
            super(tag, value);
        }
        
        @Override
        public String getValueAsString() {
            byte[] value = getValue();
            return new String(value, 0, value.length - 1);
        }
        
    }
    
    /**
     * Is all the defined SMPP Optional Parameters.
     * 
     * @author mikko.koponen
     * @author uudashr
     *
     */
    public enum Tag {
        
        DEST_ADDR_SUBUNIT(0x0005, Byte.class), 
        DEST_NETWORK_TYPE(0x0006, Byte.class),
        DEST_BEARER_TYPE(0x0007, Byte.class), 
        DEST_TELEMATICS_ID(0x0008, Short.class), 
        SOURCE_ADDR_SUBUNIT(0x000D, Byte.class), 
        SOURCE_NETWORK_TYPE(0x000E, Byte.class), 
        SOURCE_BEARER_TYPE(0x000F, Byte.class), 
        SOURCE_TELEMATICS_ID(0x0010, Byte.class), 
        QOS_TIME_TO_LIVE(0x0017, Int.class), 
        PAYLOAD_TYPE(0x0019, Byte.class), 
        ADDITIONAL_STATUS_INFO_TEXT(0x001D, COctetString.class), 
        RECEIPTED_MESSAGE_ID(0x001E, COctetString.class), 
        MS_MSG_WAIT_FACILITIES(0x0030, Byte.class), 
        PRIVACY_INDICATOR(0x0201, Byte.class), 
        SOURCE_SUBADDRESS(0x0202, OctetString.class), 
        DEST_SUBADDRESS(0x0203, OctetString.class), 
        USER_MESSAGE_REFERENCE(0x0204, Short.class), 
        USER_RESPONSE_CODE(0x0205, Byte.class), 
        SOURCE_PORT(0x020A, Short.class), 
        DESTINATION_PORT(0x020B, Short.class), 
        SAR_MSG_REF_NUM(0x020C, Short.class), 
        LANGUAGE_INDICATOR(0x020D, Byte.class), 
        SAR_TOTAL_SEGMENTS(0x020E, Byte.class), 
        SAR_SEGMENT_SEQNUM(0x020F, Byte.class), 
        SC_INTERFACE_VERSION(0x0210, Byte.class), 
        CALLBACK_NUM_PRES_IND(0x0302, Byte.class), 
        CALLBACK_NUM_ATAG(0x0303, OctetString.class), 
        NUMBER_OF_MESSAGES(0x0304, Byte.class), 
        CALLBACK_NUM(0x0381, OctetString.class), 
        DPF_RESULT(0x0420, Byte.class), 
        SET_DPF(0x0421, Byte.class), 
        MS_AVAILABILITY_STATUS(0x0422, Byte.class), 
        NETWORK_ERROR_CODE(0x0423, OctetString.class), 
        MESSAGE_PAYLOAD(0x0424, OctetString.class), 
        DELIVERY_FAILURE_REASON(0x0425, Byte.class), 
        MORE_MESSAGES_TO_SEND(0x0426, Byte.class), 
        MESSAGE_STATE(0x0427, Byte.class), 
        USSD_SERVICE_OP(0x0501, Byte.class), 
        DISPLAY_TIME(0x1201, Byte.class), 
        SMS_SIGNAL(0x1203, Short.class), 
        MS_VALIDITY(0x1204, Byte.class), 
        ALERT_ON_MESSAGE_DELIVERY(0x130C, Null.class), 
        ITS_REPLY_TYPE(0x1380, Byte.class), 
        ITS_SESSION_INFO(0x1383, Short.class);

        private final short code;
        final Class<? extends OptionalParameter> type;

        private Tag(int code, Class<? extends OptionalParameter> type) {
            this.code = (short)code;
            this.type = type;
        }
        
        /**
         * Get the tag code of the {@link Tag}.
         * 
         * @returns the tag code.
         * @deprecated use {@link #code()}
         */
        @Deprecated
        public short value() {
            return code;
        }
        
        /**
         * Get the tag code of the {@link Tag}.
         * 
         * @returns the tag code.
         */
        public short code() {
            return code;
        }
        
        public static Tag valueOf(short code) {
            for (Tag tag : Tag.values()) {
                if (tag.code == code)
                    return tag;
            }
            throw new IllegalArgumentException("No tag for: " + code);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + tag;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OptionalParameter other = (OptionalParameter)obj;
        if (tag != other.tag)
            return false;
        return true;
    }
}