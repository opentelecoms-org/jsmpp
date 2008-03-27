package org.jsmpp.bean;

import java.nio.ByteBuffer;

import org.jsmpp.util.IntUtil;
import org.jsmpp.util.OctetUtil;

public abstract class OptionalParameter {

    public final Tag tag;

    public OptionalParameter(Tag tag) {
        this.tag = tag;
    }

    public byte[] serialize() {
        byte[] value = serializeValue();
        ByteBuffer buffer = ByteBuffer.allocate(value.length + 4);
        buffer.putShort(tag.value());
        buffer.putShort((short) value.length);
        buffer.put(value);
        return buffer.array();
    }

    protected abstract byte[] serializeValue();

    public static OptionalParameter deserialize(short tagCode, byte[] content) {
        Tag tag = Tag.valueOf(tagCode);
        if (Null.class.equals(tag.type)) {
            return new Null(tag);
        }
        if (Byte.class.equals(tag.type)) {
            return new Byte(tag, content);
        }
        if (Short.class.equals(tag.type)) {
            return new Short(tag, content);
        }
        if (Int.class.equals(tag.type)) {
            return new Int(tag, content);
        }
        if (OctetString.class.equals(tag.type)) {
            return new OctetString(tag, content);
        }
        if (COctetString.class.equals(tag.type)) {
            return new COctetString(tag, content);
        }
        throw new IllegalArgumentException("Unsupported tag: " + tagCode);
    }

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
                result.put((byte) 0);
                return result.array();
            }
            return bytes;
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
        dest_addr_subunit(0x0005, Byte.class), 
        dest_network_type(0x0006, Byte.class), 
        dest_bearer_type(0x0007, Byte.class),
        dest_telematics_id(0x0008, Short.class)
        , source_addr_subunit(0x000D, Byte.class), 
        source_network_type(0x000E, Byte.class),
        source_bearer_type(0x000F, Byte.class), 
        source_telematics_id(0x0010, Byte.class), 
        qos_time_to_live(0x0017, Int.class),
        payload_type(0x0019, Byte.class), 
        additional_status_info_text(0x001D, COctetString.class),
        receipted_message_id(0x001E, COctetString.class),
        ms_msg_wait_facilities(0x0030, Byte.class),
        privacy_indicator(0x0201, Byte.class), 
        source_subaddress(0x0202, OctetString.class),
        dest_subaddress(0x0203, OctetString.class),
        user_message_reference(0x0204, Short.class), 
        user_response_code(0x0205, Byte.class), 
        source_port(0x020A, Short.class),
        destination_port(0x020B, Short.class), 
        sar_msg_ref_num(0x020C, Short.class), 
        language_indicator(0x020D, Byte.class),
        sar_total_segments(0x020E, Byte.class), 
        sar_segment_seqnum(0x020F, Byte.class), 
        SC_interface_version(0x0210, Byte.class),
        callback_num_pres_ind(0x0302, Byte.class),
        callback_num_atag(0x0303, OctetString.class),
        number_of_messages(0x0304, Byte.class), 
        callback_num(0x0381, OctetString.class), 
        dpf_result(0x0420, Byte.class), 
        set_dpf(0x0421, Byte.class), 
        ms_availability_status(0x0422, Byte.class), 
        network_error_code(0x0423, OctetString.class),
        message_payload(0x0424, OctetString.class), 
        delivery_failure_reason(0x0425, Byte.class), 
        more_messages_to_send(0x0426, Byte.class),
        message_state(0x0427, Byte.class),
        ussd_service_op(0x0501, Byte.class), 
        display_time(0x1201, Byte.class),
        sms_signal(0x1203, Short.class), 
        ms_validity(0x1204, Byte.class),
        lert_on_message_delivery(0x130C, Null.class), 
        its_reply_type(0x1380, Byte.class),
        its_session_info(0x1383, Short.class);

        private final short value;
        private final Class<? extends OptionalParameter> type;

        private Tag(int value, Class<? extends OptionalParameter> type) {
            this.value = (short) value;
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
            throw new IllegalArgumentException("No tag for: " + IntUtil.toHexString(code));
        }
    }
}