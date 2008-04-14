package org.jsmpp.bean;

import java.nio.ByteBuffer;

import org.jsmpp.util.OctetUtil;

/**
 * The Optional Parameter class.
 * 
 * @author mikko.koponen
 * 
 */
public abstract class OptionalParameter {

    public final Tag tag;

    public OptionalParameter(Tag tag) {
        this.tag = tag;
    }

    public byte[] serialize() {
        byte[] value = serializeValue();
        ByteBuffer buffer = ByteBuffer.allocate(value.length + 4);
        buffer.putShort(tag.value());
        buffer.putShort((short)value.length);
        buffer.put(value);
        return buffer.array();
    }

    protected abstract byte[] serializeValue();

    public static class Null extends OptionalParameter {
        public Null(Tag tag) {
            super(tag);
        }

        @Override
        protected byte[] serializeValue() {
            return new byte[0];
        }
    }

    public static class Short extends OptionalParameter {
        public final short value;

        public Short(Tag tag, short value) {
            super(tag);
            this.value = value;
        }

        public Short(Tag tag, byte[] content) {
            this(tag, OctetUtil.bytesToShort(content));
        }

        @Override
        protected byte[] serializeValue() {
            return OctetUtil.shortToBytes(value);
        }
    }

    public static class Int extends OptionalParameter {
        public final int value;

        public Int(Tag tag, int value) {
            super(tag);
            this.value = value;
        }

        public Int(Tag tag, byte[] content) {
            this(tag, OctetUtil.bytesToInt(content));
        }

        @Override
        protected byte[] serializeValue() {
            return OctetUtil.intToBytes(value);
        }
    }

    public static class Byte extends OptionalParameter {
        public final byte value;

        public Byte(Tag tag, byte value) {
            super(tag);
            this.value = value;
        }

        public Byte(Tag tag, byte[] content) {
            this(tag, content[0]);
        }

        @Override
        protected byte[] serializeValue() {
            return new byte[] { value };
        }
    }

    public static class OctetString extends OptionalParameter {
        public final String value;
        private final boolean writeNull;

        public OctetString(Tag tag, String value) {
            this(tag, value, false);
        }

        public OctetString(Tag tag, String value, boolean withNull) {
            super(tag);
            this.value = value;
            this.writeNull = withNull;
        }

        public OctetString(Tag tag, byte[] content, int offset, int length) {
            this(tag, new String(content, offset, length));
        }

        public OctetString(Tag tag, byte[] content) {
            this(tag, content, 0, content.length);
        }

        @Override
        protected byte[] serializeValue() {
            byte[] bytes = value.getBytes();
            if (writeNull) {
                ByteBuffer result = ByteBuffer.allocate(bytes.length + 1);
                result.put(bytes);
                result.put((byte)0);
                return result.array();
            } else {
                return bytes;
            }
        }
    }

    public static class COctetString extends OctetString {
        public COctetString(Tag tag, String value) {
            super(tag, value, true);
        }

        public COctetString(Tag tag, byte[] content) {
            super(tag, content, 0, content.length - 1);
        }
    }

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
        ALERT_ON_MESSAGE_DELIVERY(0x130C,Null.class), 
        ITS_REPLY_TYPE(0x1380, Byte.class), 
        ITS_SESSION_INFO(0x1383, Short.class);

        private final short value;
        final Class<? extends OptionalParameter> type;

        private Tag(int value, Class<? extends OptionalParameter> type) {
            this.value = (short)value;
            this.type = type;
        }

        public short value() {
            return value;
        }

        public static Tag valueOf(short code) {
            for (Tag tag : Tag.values()) {
                if (tag.value() == code)
                    return tag;
            }
            throw new IllegalArgumentException("No tag for: " + code);
        }

    }

}