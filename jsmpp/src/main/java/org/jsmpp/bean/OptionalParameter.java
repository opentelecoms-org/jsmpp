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
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.jsmpp.util.HexUtil;
import org.jsmpp.util.OctetUtil;

/**
 * Please see SMPP specifications v3.4 or v5.0 for a detailed explanation of Optional Parameters. This abstract class has 
 * subclasses for each available Optional Parameter.
 * 
 * @author mikko.koponen
 * @author uudashr
 * @author stefanth
 */
public abstract class OptionalParameter {

    public final short tag;

    public OptionalParameter(short tag) {
        this.tag = tag;
    }

    /**
		 * Convert the optional parameter into a byte serialized form conforming to the SMPP specification.
     * 
     * @return A byte array according to the SMPP specification
     */
    public byte[] serialize() {
        byte[] value = serializeValue();
        ByteBuffer buffer = ByteBuffer.allocate(value.length + 4);
        buffer.putShort(tag);
        buffer.putShort((short)value.length);
        buffer.put(value);
        return buffer.array();
    }

    /**
		 * This method should serialize the value part of the optional parameter. The format of the value is dependent
     * on the specific optional parameter type so it is abstract and must be implemented by subclasses.
		 *
     * @return the serialized bytes
     */
    protected abstract byte[] serializeValue();
    
    /**
     * An optional parameter with an empty value field.
     */
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

    /**
     * An optional parameter containing two bytes representing a short integer.
     */
    public static class Short extends OptionalParameter {
        protected final short value;

        /**
         * Construct the Short optional parameter with specified short value.
         *
				 * @param tag is the tag.
				 * @param value is the value.
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
        
        /**
         * Print Optional Parameter byte in hex format
         */
        @Override
        public String toString()
        {
            return HexUtil.convertBytesToHexString(OctetUtil.shortToBytes(value));
        }
    }

    /**
     * An optional parameter containing four bytes representing an int integer.
     */
    public static class Int extends OptionalParameter {
        protected final int value;

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
        
        /**
         * Print Optional Parameter byte in hex format
         */
        @Override
        public String toString()
        {
            return HexUtil.convertBytesToHexString(OctetUtil.intToBytes(value));
        }
    }

    /**
     * An optional parameter containing one byte representing a byte integer.
     */
    public static class Byte extends OptionalParameter {
        protected final byte value;

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
        
        /**
         * Print Optional Parameter byte in hex format
         */
        @Override
        public String toString()
        {
            return HexUtil.convertBytesToHexString(new byte[] {getValue()});
        }
    }

    /**
     * An optional parameter containing a series of octets, not necessarily NULL terminated.
     */
    public static class OctetString extends OptionalParameter {
        protected final byte[] value;
        
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

    /**
     * An optional parameter containing a series of ASCII characters terminated with the NULL character.
     */
    public static class COctetString extends OctetString {

    	public COctetString(short tag, String value, String charsetName)
				throws UnsupportedEncodingException {
			super(tag, new byte[value.getBytes(charsetName).length + 1]);
			byte[] bytes = value.getBytes(charsetName);
			System.arraycopy(bytes, 0, this.value, 0, bytes.length);
			this.value[bytes.length] = (byte) 0x00;
		}

		public COctetString(short tag, String value) {
			super(tag, new byte[value.getBytes().length + 1]);
			byte[] bytes = value.getBytes();
			System.arraycopy(bytes, 0, this.value, 0, bytes.length);
			this.value[bytes.length] = (byte) 0x00;
		}

		public COctetString(short tag, byte[] value) {
			super(tag, value);
		}

		@Override
		public String getValueAsString() {
			byte[] s = new byte[value.length > 0 ? value.length - 1 : 0];
			System.arraycopy(value, 0, s, 0, s.length);
			return new String(s);
		}
  }
    
    /**
     * Represents valid values for the optional parameters dest_addr_subunit and source_addr_subunit.
     *
     */
	public enum Addr_subunit{
		/**
		 * 0x00 = Unknown (default)
		 */
		UNKNOWN_DEFAULT(0x00),
		/**
		 * 0x01 = MS Display
		 */
		MS_DISPLAY(0x01),
		/**
		 * 0x02 = Mobile Equipment
		 */
		MOBILE_EQUIPMENT(0x02),
		/**
		 * 0x03 = Smart Card 1 (expected to be SIM if a SIM exists in the MS)
		 */
		SMART_CARD_1(0x03),
		/**
		 * 0x04 = External Unit 1
		 */
		EXTERNAL_UNIT_1(0x04),
		/**
		 * 5 to 255 = reserved
		 */
		RESERVED(0x05);
		
		protected byte value;

		Addr_subunit(int value) {
			this.value = (byte)value;
		}
		
		public byte value() {
			return value;
		}
		
		public static Addr_subunit toEnum(byte value) {
			for (Addr_subunit v : Addr_subunit.values()) {
				if (v.value == value)
					return v;
			}
			return RESERVED;
		}
	}
	
	/**
	 * The dest_addr_subunit parameter is used to route messages when received by a mobile station,
	 * for example to a smart card in the mobile station or to an external device connected to the
	 * mobile station.
	 *
	 * Wireless Network Technology: GSM
	 */
	public static class Dest_addr_subunit extends OptionalParameter.Byte {

		public Dest_addr_subunit(Addr_subunit addr_subunit) {
			this(addr_subunit.value());
		}

		public Dest_addr_subunit(byte value) {
			super(Tag.DEST_ADDR_SUBUNIT, value);
		}

		public Dest_addr_subunit(byte[] content) {
			super(Tag.DEST_ADDR_SUBUNIT.code, content);
		}

		/**
		 * Get the dest_addr_subunit value as an enum.
		 * @return An enum of type {@link Addr_subunit}
		 */
		public Addr_subunit getDestAddrSubunit() {
			return Addr_subunit.toEnum(value);
		}

		@Override
		public String toString() {
			return getDestAddrSubunit().toString();
		}
	}

	/**
	 * Represents valid values for the optional parameters dest_network_type and source_network_type.
	 *
	 */
	public enum Network_type {
		UNKNOWN(0x00),
		GSM(0x01),
		ANSI_136_TDMA(0x02),
		IS_95_CDMA(0x03),
		PDC(0x04),
		PHS(0x05),
		IDEN(0x06),
		AMPS(0x07),
		PAGING_NETWORK(0x08),
		RESERVED(0x09);
		
		protected byte value;

		Network_type(int value) {
			this.value = (byte)value;
		}

		public byte value() {
			return value;
		}
		
		public static Network_type toEnum(byte value) {
			for (Network_type v : Network_type.values()) {
				if (v.value == value)
					return v;
			}
			return RESERVED;
		}
	}
	
	/**
	 * The dest_network_type parameter is used to indicate a network type associated with the
	 * destination address of a message. In the case that the receiving system (e.g. SMSC) does not
	 * support the indicated network type, it may treat this a failure and return a response PDU
	 * reporting a failure.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Dest_network_type extends OptionalParameter.Byte {

		public Dest_network_type(Network_type network_type) {
			this(network_type.value);
		}

		public Dest_network_type(byte value) {
			super(Tag.DEST_NETWORK_TYPE, value);
		}

		public Dest_network_type(byte[] content) {
			super(Tag.DEST_NETWORK_TYPE.code, content);
		}
		
		/**
		 * Get the dest_network_type value as an enum.
		 * @return An enum of type {@link Network_type}
		 */
		public Network_type getDestNetworkType() {
			return Network_type.toEnum(value);
		}

		@Override
		public String toString() {
			return getDestNetworkType().toString();
		}
	}

	/**
	 * Represents valid values for the optional parameters dest_bearer_type and source_bearer_type.
	 *
	 */
	public enum Bearer_type {
		UNKNOWN(0x00),
		SMS(0x01),
		CIRCUIT_SWITCHED_DATA(0x02),
		PACKET_DATA(0x03),
		USSD(0x04),
		CDPD(0x05),
		DATATAC(0x06),
		FLEX_REFLEX(0x07),
		CELL_BROADCAST_CELLCAST(0x08),
		RESERVED(0x09);

		protected byte value;

		Bearer_type(int value) {
			this.value = (byte)value;
		}

		public byte value() {
			return value;
		}

		public static Bearer_type toEnum(byte value) {
			for (Bearer_type v : Bearer_type.values()) {
				if (v.value == value)
					return v;
			}
			return RESERVED;
		}
	}

	/**
	 * The dest_bearer_type parameter is used to request the desired bearer for delivery of the
	 * message to the destination address. In the case that the receiving system (e.g. SMSC) does not
	 * support the indicated bearer type, it may treat this a failure and return a response PDU reporting
	 * a failure.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Dest_bearer_type extends OptionalParameter.Byte {

		public Dest_bearer_type(Bearer_type bearer_type) {
			this(bearer_type.value);
		}
		
		public Dest_bearer_type(byte value) {
			super(Tag.DEST_BEARER_TYPE, value);
		}

		public Dest_bearer_type(byte[] content) {
			super(Tag.DEST_BEARER_TYPE.code, content);
		}

		/**
		 * Get the dest_bearer_type value as an enum.
		 * @return An enum of type {@link Bearer_type}
		 */
		public Bearer_type getDestBearerType() {
			return Bearer_type.toEnum(value);
		}

		@Override
		public String toString() {
			return getDestBearerType().toString();
		}
	}

	/**
	 * This parameter defines the telematic interworking to be used by the delivering system for the
	 * destination address. This is only useful when a specific dest_bearer_type parameter has also
	 * been specified as the value is bearer dependent. In the case that the receiving system (e.g.
	 * SMSC) does not support the indicated telematic interworking, it may treat this a failure and
	 * return a response PDU reporting a failure.
	 *
	 * Wireless Network Technology: GSM
	 *
	 * @author stefanth
	 */
	public static class Dest_telematics_id extends OptionalParameter.Short {

		public Dest_telematics_id(short value) {
			super(Tag.DEST_TELEMATICS_ID, value);
		}

		public Dest_telematics_id(byte[] content) {
			super(Tag.DEST_TELEMATICS_ID.code, content);
		}
	}

	/**
	 * The source_addr_subunit parameter is used to indicate where a message originated in the
	 * mobile station, for example a smart card in the mobile station or an external device connected
	 * to the mobile station.
	 *
	 * Wireless Network Technology: GSM
	 *
	 * @author stefanth
	 */
	public static class Source_addr_subunit extends OptionalParameter.Byte {

		public Source_addr_subunit(Addr_subunit addr_subunit) {
			this(addr_subunit.value());
		}

		public Source_addr_subunit(byte value) {
			super(Tag.SOURCE_ADDR_SUBUNIT, value);
		}

		public Source_addr_subunit(byte[] content) {
			super(Tag.SOURCE_ADDR_SUBUNIT.code, content);
		}

		/**
		 * Get the source_addr_subunit parameter as an enum.
		 * @return the addr_subunit optional parameter
		 */
		public Addr_subunit getSourceAddrSubunit() {
			return Addr_subunit.toEnum(value);
		}

		@Override
		public String toString() {
			return getSourceAddrSubunit().toString();
		}
	}

	/**
	 * The source_network_type parameter is used to indicate the network type associated with the
	 * device that originated the message.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Source_network_type extends OptionalParameter.Byte {

		public Source_network_type(Network_type network_type) {
			this(network_type.value);
		}

		public Source_network_type(byte value) {
			super(Tag.SOURCE_NETWORK_TYPE, value);
		}

		public Source_network_type(byte[] content) {
			super(Tag.SOURCE_NETWORK_TYPE.code, content);
		}

		/**
		 * Get the source_network_type value as an enum.
		 * @return An enum of type Network_type
		 */
		public Network_type getSourceNetworkType() {
			return Network_type.toEnum(value);
		}

		@Override
		public String toString() {
			return getSourceNetworkType().toString();
		}
	}

	/**
	 * The source_bearer_type parameter indicates the wireless bearer over which the message
	 * originated.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Source_bearer_type extends OptionalParameter.Byte {

		public Source_bearer_type(Bearer_type bearer_type) {
			this(bearer_type.value);
		}

		public Source_bearer_type(byte value) {
			super(Tag.SOURCE_BEARER_TYPE, value);
		}

		public Source_bearer_type(byte[] content) {
			super(Tag.SOURCE_BEARER_TYPE.code, content);
		}

		/**
		 * Get the source_bearer_type value as an enum.
		 * @return An enum of type {@link Bearer_type}
		 */
		public Bearer_type getSourceBearerType() {
			return Bearer_type.toEnum(value);
		}

		@Override
		public String toString() {
			return getSourceBearerType().toString();
		}
	}

	/**
	 * The source_telematics_id parameter indicates the type of telematics interface over which the
	 * message originated.
	 *
	 * Wireless Network Technology: GSM
	 *
	 * @author stefanth
	 */
	public static class Source_telematics_id extends OptionalParameter.Byte {

		public Source_telematics_id(byte value) {
			super(Tag.SOURCE_TELEMATICS_ID, value);
		}

		public Source_telematics_id(byte[] content) {
			super(Tag.SOURCE_TELEMATICS_ID.code, content);
		}
	}

	/**
	 * This parameter defines the number of seconds which the sender requests the SMSC to keep the
	 * message if undelivered before it is deemed expired and not worth delivering. If the parameter
	 * is not present, the SMSC may apply a default value.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Qos_time_to_live extends OptionalParameter.Int {

		public Qos_time_to_live(int value) {
			super(Tag.QOS_TIME_TO_LIVE, value);
		}

		public Qos_time_to_live(byte[] content) {
			super(Tag.QOS_TIME_TO_LIVE.code, content);
		}
	}

	/**
	 * The payload_type parameter defines the higher layer PDU type contained in the message
	 * payload.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Payload_type extends OptionalParameter.Byte {

		/**
		 * Represents valid values for the optional parameter payload_type.
		 *
		 */
		public enum 	Payload_type_enum {
			/**
			 * From Short Message Peer to Peer Protocol Specification v3.4:
			 * 
			 * In the case of a WAP application, the default higher layer
			 * message type is a WDP message. See [WDP] for details.
			 */
			DEFAULT(0),
			/**
			 * From Short Message Peer to Peer Protocol Specification v3.4:
			 * 
			 * WCMP message
			 * Wireless Control Message Protocol formatted data.
			 * See [WCMP] for details.
			 */
			WCMP(1),
			RESERVED(255);

			protected byte value;

			Payload_type_enum(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Payload_type_enum toEnum(byte value) {
				for (Payload_type_enum v : Payload_type_enum.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public Payload_type(Payload_type_enum payload_type) {
			this(payload_type.value);
		}
		
		public Payload_type(byte value) {
			super(Tag.PAYLOAD_TYPE, value);
		}

		public Payload_type(byte[] content) {
			super(Tag.PAYLOAD_TYPE.code, content);
		}
		
		/**
		 * Get the payload_type value as an enum.
		 * @return An enum of type {@link Payload_type_enum}
		 */
		public Payload_type_enum getPayloadType() {
			return Payload_type_enum.toEnum(value);
		}

		@Override
		public String toString() {
			return getPayloadType().toString();
		}
	}

	/**
	 * The additional_status_info_text parameter gives an ASCII textual description of the meaning
	 * of a response PDU. It is to be used by an implementation to allow easy diagnosis of problems.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Additional_status_info_text extends OptionalParameter.COctetString {
		public Additional_status_info_text(byte[] value) {
			super(Tag.ADDITIONAL_STATUS_INFO_TEXT.code(), value);
		}
		public Additional_status_info_text(String value) {
			super(Tag.ADDITIONAL_STATUS_INFO_TEXT.code(), value);
		}
	}

	/** 
	 * The receipted_message_id parameter indicates the ID of the message being receipted in an
	 * SMSC Delivery Receipt. This is the opaque SMSC message identifier that was returned in the
	 * message_id parameter of the SMPP response PDU that acknowledged the submission of the
	 * original message.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Receipted_message_id extends OptionalParameter.COctetString {
		public Receipted_message_id(byte[] value) {
			super(Tag.RECEIPTED_MESSAGE_ID.code(), value);
		}
		public Receipted_message_id(String value) {
			super(Tag.RECEIPTED_MESSAGE_ID.code(), value);
		}
	}

	/**
	 * The ms_msg_wait_facilities parameter allows an indication to be provided to an MS that there
	 * are messages waiting for the subscriber on systems on the PLMN. The indication can be an icon
	 * on the MS screen or other MMI indication.
	 * The ms_msg_wait_facilities can also specify the type of message associated with the message
	 * waiting indication.
	 *
	 * Wireless Network Technology: GSM
	 *
	 * @author stefanth
	 */
	public static class Ms_msg_wait_facilities extends OptionalParameter.Byte {

		public enum Ms_msg_wait_facilities_type {
			VOICEMAIL_MESSAGE_WAITING(0),
			FAX_MESSAGE_WAITING(1),
			ELECTRONIC_MAIL_MESSAGE_WAITING(2),
			OTHER_MESSAGE_WAITING(3),
			UNKNOWN(4);
			
			protected byte value;

			Ms_msg_wait_facilities_type(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Ms_msg_wait_facilities_type toEnum(byte value) {
				for (Ms_msg_wait_facilities_type v : Ms_msg_wait_facilities_type.values()) {
					if (v.value == value)
						return v;
				}
				return UNKNOWN;
			}
		}

		public Ms_msg_wait_facilities(boolean indicatorActive, Ms_msg_wait_facilities_type message_type) {
			this( (byte) ( message_type.value() | (indicatorActive? 0x80 : 0x00) ) );
		}
		
		public Ms_msg_wait_facilities(byte value) {
			super(Tag.MS_MSG_WAIT_FACILITIES, value);
		}

		public Ms_msg_wait_facilities(byte[] content) {
			super(Tag.MS_MSG_WAIT_FACILITIES.code, content);
		}
		
		/**
		 * Returns true if the parameters has the indicator active.
		 * Returns false if the parameter has indicator inactive.
		 * 
		 * The Indicator is encoded in bit 7 as follows:
		 * 0 = Set Indication Inactive
		 * 1 = Set Indication Active
		 *
		 * @return {@code true} if the indication is active, {@code false} if the indication is inactive
		 */
		public boolean isIndicatorActive() {
			return (value & 0x80) != 0;
		}

		/**
		 * Get the the message type of the message associated with the MWI.
		 * @return An enum of type {@link Ms_msg_wait_facilities_type}
		 */
		public Ms_msg_wait_facilities_type getMessageType() {
			return Ms_msg_wait_facilities_type.toEnum((byte)(value & 0x03));
		}

		@Override
		public String toString() {
			String endString = isIndicatorActive() ? "active" : "inactive";
			return getMessageType().toString() + " set as " + endString;
		}
	}

	/**
	 * The privacy_indicator indicates the privacy level of the message.
	 * 
	 * 0 = Privacy Level 0 (Not Restricted) (default)
	 * 1 = Privacy Level 1 (Restricted)
	 * 2 = Privacy Level 2 (Confidential)
	 * 3 = Privacy Level 3 (Secret)
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Privacy_indicator extends OptionalParameter.Byte {

		public Privacy_indicator(byte value) {
			super(Tag.PRIVACY_INDICATOR, value);
		}

		public Privacy_indicator(byte[] content) {
			super(Tag.PRIVACY_INDICATOR.code, content);
		}
	}
	
	/**
	 * The source_subaddress parameter specifies a subaddress associated with the originator 
	 * of the message.
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Source_subaddress extends OptionalParameter.OctetString {
		
		/**
		 * The first octet of the data field is a Type of
		 * Subaddress tag and indicates the type of
		 * subaddressing information included, and
		 * implies the type and length of
		 * subaddressing information which can
		 * accompany this tag value in the data field.
		 *
		 * Valid Tag values are:
		 * 00000001 - Reserved
		 * 00000010 - Reserved
		 * 10000000 - NSAP (Even) [ITUT X.213]
		 * 10001000 - NSAP (Odd) [ITUT X.213]
		 * 10100000 - User Specified
		 * All other values Reserved
		 *
		 * The remaining octets contain the
		 * subaddress.
		 *
		 * A NSAP address shall be encoded using
		 * the preferred binary encoding specified in
		 * [ITUT X.213]. In this case the subaddress
		 * field contains the Authority and Format
		 * Identifier.
		 * 
		 * A User Specified subaddress is encoded
		 * according to user specification, subject to a
		 * maximum of 22 octets.
		 * @param content the source_subaddress optional parameter
		 */
		public Source_subaddress(byte[] content) {
			super(Tag.SOURCE_SUBADDRESS.code, content);
		}
	}
	
	/**
	 * The dest_subaddress parameter specifies a subaddress associated with the destination 
	 * of the message.
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Dest_subaddress extends OptionalParameter.OctetString {
		/**
		 * The first octet of the data field is a Type of
		 * Subaddress tag and indicates the type of
		 * subaddressing information included, and
		 * implies the type and length of
		 * subaddressing information which can
		 * accompany this tag value in the data field.
		 *
		 * Valid Tag values are:
		 * 00000001 - Reserved
		 * 00000010 - Reserved
		 * 10000000 - NSAP (Even) [ITUT X.213]
		 * 10001000 - NSAP (Odd) [ITUT X.213]
		 * 10100000 - User Specified
		 * All other values Reserved
		 *
		 * The remaining octets contain the
		 * subaddress.
		 *
		 * A NSAP address shall be encoded using
		 * the preferred binary encoding specified in
		 * [ITUT X.213]. In this case the subaddress
		 * field contains the Authority and Format
		 * Identifier.
		 * 
		 * A User Specified subaddress is encoded according to user specification,
		 * subject to a maximum of 22 octets.
		 *
		 * @param content the dest_subaddress bytes
		 */
		public Dest_subaddress(byte[] content) {
			super(Tag.DEST_SUBADDRESS.code, content);
		}
	}
	
	/**
	 * A reference assigned by the originating SME to the short message.
	 * <br>
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class User_message_reference extends OptionalParameter.Short {
		
		/**
		 * From SMPP specs: "All values allowed."
		 * @param value the user_message_reference optional parameter
		 */
		public User_message_reference(short value) {
			super(Tag.USER_MESSAGE_REFERENCE, value);
		}

		public User_message_reference(byte[] content) {
			super(Tag.USER_MESSAGE_REFERENCE.code, content);
		}
	}
	
	/**
	 * A response code set by the user in a User Acknowledgement/Reply message. The response
	 * codes are application specific.
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class User_response_code extends OptionalParameter.Byte {
		
		/**
		 * From SMPP specs:
		 * 0 to 255 (IS-95 CDMA)
		 * 0 to 15 (CMT-136 TDMA)
		 *
		 * @param value the user_response_code optional parameter
		 */
		public User_response_code(byte value) {
			super(Tag.USER_MESSAGE_REFERENCE, value);
		}

		public User_response_code(byte[] content) {
			super(Tag.USER_MESSAGE_REFERENCE.code, content);
		}
	}

	/**
	 * The source_port parameter is used to indicate the application port number associated with the
	 * source address of the message.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Source_port extends OptionalParameter.Short {
		
		/**
		 * From SMPP specs: "All values allowed."
		 * @param value the source_port optional parameter
		 */
		public Source_port(short value) {
			super(Tag.SOURCE_PORT, value);
		}

		public Source_port(byte[] content) {
			super(Tag.SOURCE_PORT.code, content);
		}
	}
	
	/**
	 * The destination_port parameter is used to indicate the application port number associated with
	 * the destination address of the message.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * In SMPP 5.0, this is dest_port.
	 *
	 * @author stefanth
	 */
	public static class Destination_port extends OptionalParameter.Short {
		
		/**
		 * From SMPP specs: "All values allowed."
		 *
		 * @param value the destination_port optional parameter
		 */
		public Destination_port(short value) {
			super(Tag.DESTINATION_PORT, value);
		}

		public Destination_port(byte[] content) {
			super(Tag.DESTINATION_PORT.code, content);
		}
	}
	
	/**
	 * The sar_msg_ref_num parameter is used to indicate the reference number for a particular
	 * concatenated short message.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Sar_msg_ref_num extends OptionalParameter.Short {
		
		/**
		 * This parameter shall contain a originator generated reference number so that a
		 * segmented short message may be reassembled into a single original message.
		 * This allows the parallel transmission of several segmented messages. This
		 * reference number shall remain constant for every segment which makes up a particular
		 * concatenated short message. When present, the PDU must also contain
		 * the sar_total_segments and sar_segment_seqnum parameters.
		 * Otherwise this parameter shall be ignored.
		 *
		 * @param value the sar_msg_ref_num value
		 */
		public Sar_msg_ref_num(short value) {
			super(Tag.SAR_MSG_REF_NUM, value);
		}

		public Sar_msg_ref_num(byte[] content) {
			super(Tag.SAR_MSG_REF_NUM.code, content);
		}
	}
	
	/**
	 * The language_indicator parameter is used to indicate the language of the short message.
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Language_indicator extends OptionalParameter.Byte {
		
		/**
		 * Represents valid values for the optional parameter language_indicator.
		 *
		 */
		public enum Language_indicator_enum {
			UNSPECIFIED_DEFAULT(0),
			ENGLISH(1),
			FRENCH(2),
			SPANISH(3),
			GERMAN(4),
			PORTUGUESE(5),
			RESERVED(255);

			protected byte value;

			Language_indicator_enum(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Language_indicator_enum toEnum(byte value) {
				for (Language_indicator_enum v : Language_indicator_enum.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public Language_indicator(Language_indicator_enum value) {
			this(value.value);
		}
		
		public Language_indicator(byte value) {
			super(Tag.LANGUAGE_INDICATOR, value);
		}

		public Language_indicator(byte[] content) {
			super(Tag.LANGUAGE_INDICATOR.code, content);
		}
		
		public Language_indicator_enum getLanguageIndicator() {
			return Language_indicator_enum.toEnum(value);
		}
	}
	
	/**
	 * The sar_total_segments parameter is used to indicate the total number of
	 * short messages within the concatenated short message. <br>
	 * <br>
	 * This parameter shall contain a value in the range 1 to 255 indicating
	 * the total number of fragments within the concatenated short message.
	 * The value shall start at 1 and remain constant for every short message
	 * which makes up the concatenated short message. <br>
	 * When present, the PDU must also contain the sar_msg_ref_num and
	 * sar_segment_seqnum parameters. Otherwise this parameter shall be ignored. <br>
	 * <br>
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Sar_total_segments extends OptionalParameter.Byte {
		
		public Sar_total_segments(byte value) {
			super(Tag.SAR_TOTAL_SEGMENTS, value);
		}

		public Sar_total_segments(byte[] content) {
			super(Tag.SAR_TOTAL_SEGMENTS.code, content);
		}
	}
	
	/**
	 * The sar_segment_seqnum parameter is used to indicate the sequence number of
	 * a particular short message within the concatenated short message. <br>
	 * <br>
	 * This parameter shall contain a value in the range 1 to 255 indicating
	 * the sequence number of a particular message within the concatenated short message.
	 * The value shall start at 1 and increment by one for every message sent within
	 * the concatenated short message. <br>
	 * When present, the PDU must also contain the sar_total_segments and sar_msg_ref_num parameters.
	 * Otherwise this parameter shall be ignored. <br>
	 * <br>
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Sar_segment_seqnum extends OptionalParameter.Byte {
		
		public Sar_segment_seqnum(byte value) {
			super(Tag.SAR_SEGMENT_SEQNUM, value);
		}

		public Sar_segment_seqnum(byte[] content) {
			super(Tag.SAR_SEGMENT_SEQNUM.code, content);
		}
	}
	
	/**
	 * The sc_interface_version parameter is used to indicate the SMPP version supported by the
	 * SMSC. It is returned in the bind response PDUs.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Sc_interface_version extends OptionalParameter.Byte {
		
		public Sc_interface_version(InterfaceVersion interface_version) {
			this(interface_version.value());
		}
		
		public Sc_interface_version(byte value) {
			super(Tag.SC_INTERFACE_VERSION, value);
		}

		public Sc_interface_version(byte[] content) {
			super(Tag.SC_INTERFACE_VERSION.code, content);
		}
	}

	/**
	 * This parameter controls the presentation indication and screening of
	 * the CallBackNumber at the mobile station. If present,
	 * the callback_num parameter must also be present. <br>
	 * <br>
	 * If this parameter is present and there are multiple
	 * instances of the callback_num parameter then this parameter
	 * must occur an equal number of instances and the order of
	 * occurrence determines the particular callback_num_pres_ind
	 * which corresponds to a particular callback_num.<br>
	 * <br>
	 * Bits 7............0 = 0000ppss <br>
	 * <br>
	 * The Presentation Indicator is encoded in
	 * bits 2 and 3 as follows:
	 * 00 = Presentation Allowed
	 * 01 = Presentation Restricted
	 * 10 = Number Not Available
	 * 11 = Reserved
	 *
	 * The Screening Indicator is encoded in bits
	 * 0 and 1 as follows:
	 * 00 = User provided, not screened
	 * 01 = User provided, verified and passed
	 * 10 = User provided, verified and failed
	 * 11 = Network Provided.
	 *
	 * Wireless Network Technology: TDMA
	 *
	 * @author stefanth
	 */
	public static class Callback_num_pres_ind extends OptionalParameter.Byte {
		
		public enum Presentation_Indicator {
			/**
			 * Presentation Allowed = 0 (binary 00)
			 */
			PRESENTATION_ALLOWED(0),
			/**
			 * Presentation Restricted = 1 (binary 01)
			 */
			PRESENTATION_RESTRICTED(1),
			/**
			 * Number Not Available = 2 (binary 10)
			 */
			NUMBER_NOT_AVAILABLE(2),
			/**
			 * Reserved = 3 (binary 11)
			 */
			RESERVED(3);
			
			protected byte value;

			Presentation_Indicator(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Presentation_Indicator toEnum(byte value) {
				for (Presentation_Indicator v : Presentation_Indicator.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public enum Screening_Indicator {
			/**
			 * User provided, not screened = 0 (binary 00)
			 */
			USER_PROVIDED_VERIFIED_NOT_SCREENED(0),
			/**
			 * User provided, verified and passed = 1 (binary 01)
			 */
			USER_PROVIDED_VERIFIED_AND_PASSED(1),
			/**
			 * User provided, verified and failed = 2 (binary 10)
			 */
			USER_PROVIDED_VERIFIED_AND_FAILED(2),
			/**
			 * Network Provided = 3 (binary 11)
			 */
			NETWORK_PROVIDED(3);

			protected byte value;

			Screening_Indicator(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Screening_Indicator toEnum(byte value) {
				for (Screening_Indicator v : Screening_Indicator.values()) {
					if (v.value == value)
						return v;
				}
				throw new IllegalArgumentException("Screening indicator value " + value + " is invalid");
			}
		}
		
		public Callback_num_pres_ind(Presentation_Indicator presentation_indicator, Screening_Indicator screening_indicator) {
			this((byte)(presentation_indicator.value << 2 | screening_indicator.value));
		}
		
		public Callback_num_pres_ind(byte value) {
			super(Tag.CALLBACK_NUM_PRES_IND, value);
		}

		public Callback_num_pres_ind(byte[] content) {
			super(Tag.CALLBACK_NUM_PRES_IND.code, content);
		}
		
		public Presentation_Indicator getPresentationIndicator() {
			return Presentation_Indicator.toEnum((byte)((value >> 2) & 0x03));
		}
		
		public Screening_Indicator getScreeningIndicator() {
			return Screening_Indicator.toEnum((byte)(value & 0x03));
		}
	}
	
	/**
	 * From SMPP specs:
	 * Associates a displayable alphanumeric tag with the
	 * callback number.
	 *
	 * If this parameter is present and there are multiple
	 * instances of the callback_num parameter then this parameter
	 * must occur an equal number of instances and the order of
	 * occurrence determines the particular callback_num_atag
	 * which corresponds to a particular callback_num.
	 *
	 * Bits:
	 * 7...............0
	 * EEEEEEEE (octet 1)
	 * XXXXXXXX (octet 2)
	 *
	 * XXXXXXXX (octet N)
	 * The first octet contains the encoding scheme of the Alpha Tag display
	 * characters. This field contains the same values as for Data Coding Scheme (see
	 * section 5.2.19).
	 * The following octets contain the display characters:
	 * There is one octet per display character for 7-bit and 8-bit encoding schemes.
	 * There are two octets per display character for 16-bit encoding schemes.
	 *
	 * Wireless Network Technology: TDMA
	 *
	 * @author stefanth
	 */
	public static class Callback_num_atag extends OptionalParameter.OctetString {
		
		public Callback_num_atag(byte[] content) {
			super(Tag.CALLBACK_NUM_ATAG.code, content);
		}
	}
	
	/**
	 * The number_of_messages parameter is used to indicate the number of messages stored in a mailbox.
	 *
	 * 0 to 99 = allowed values.
	 * values 100 to 255 are reserved
	 *
	 * Wireless Network Technology: CDMA
	 *
	 * @author stefanth
	 */
	public static class Number_of_messages extends OptionalParameter.Byte {
		
		public Number_of_messages(byte value) {
			super(Tag.NUMBER_OF_MESSAGES, value);
		}

		public Number_of_messages(byte[] content) {
			super(Tag.NUMBER_OF_MESSAGES.code, content);
		}
	}
	
	/**
	 * The callback_num parameter associates a call back number with the message. In TDMA
	 * networks, it is possible to send and receive multiple callback numbers to/from TDMA mobile
	 * stations.
	 *
	 * Bits
	 * 7.............0
	 * 0000000D (octet 1)
	 * 00000TTT (octet 2)
	 * 0000NNNN (octet 3)
	 * XXXXXXXX (octet 4)
	 *
	 * XXXXXXXX (octet N)
	 * The originating SME can set a Call Back Number for the receiving Mobile Station.
	 * The first octet contains the Digit Mode Indicator.
	 * Bit D=0 indicates that the Call Back Number is sent to the mobile as DTMF
	 * digits encoded in TBCD. Bit D=1 indicates that the Call Back Number is sent to the
	 * mobile encoded as ASCII digits.
	 * The 2nd octet contains the Type of Number (TON). Encoded as in section 5.2.5.
	 * The third octet contains the Numbering Plan Indicator (NPI). Encoded as specified
	 * in section 5.2.6
	 * The remaining octets contain the Call Back Number digits encoded as ASCII
	 * characters
	 *
	 * Wireless Network Technology: CDMA, TDMA, GSM, iDEN
	 *
	 * @author stefanth
	 */
	public static class Callback_num extends OptionalParameter.OctetString {
		
		public Callback_num(byte[] content) {
			super(Tag.CALLBACK_NUM.code, content);
		}
	}
	
	/**
	 * The dpf_result parameter is used in the data_sm_resp PDU to indicate if delivery pending flag
	 * (DPF) was set for a delivery failure of the short message.
	 * If the dpf_result parameter is not included in the data_sm_resp PDU, the ESME should assume
	 * that DPF is not set.
	 * Currently this parameter is only applicable for the Transaction message mode.
	 *
	 * 0 = DPF not set
	 * 1 = DPF set
	 * values 2 to 255 are reserved
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Dpf_result extends OptionalParameter.Byte {
		
		public Dpf_result(boolean dpf_result) {
			this(dpf_result ? (byte)1 : (byte)0);
		}
		
		public Dpf_result(byte value) {
			super(Tag.DPF_RESULT, value);
		}

		public Dpf_result(byte[] content) {
			super(Tag.DPF_RESULT.code, content);
		}
		
		public boolean getDpfResult() {
			return value == 1;
		}
	}
	
	/**
	 * An ESME may use the set_dpf parameter to request the setting of a delivery pending flag (DPF)
	 * for certain delivery failure scenarios, such as
	 * - MS is unavailable for message delivery (as indicated by the HLR)
	 * The SMSC should respond to such a request with an alert_notification PDU when it detects
	 * that the destination MS has become available.
	 * The delivery failure scenarios under which DPF is set is SMSC implementation and network
	 * implementation specific. If a delivery pending flag is set by the SMSC or network (e.g. HLR),
	 * then the SMSC should indicate this to the ESME in the data_sm_resp message via the
	 * dpf_result parameter.
	 *
	 * 0 = Setting of DPF for delivery failure to MS not requested
	 * 1 = Setting of DPF for delivery failure requested (default)
 	 * values 2 to 255 are reserved
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Set_dpf extends OptionalParameter.Byte {
		
		public Set_dpf(boolean set_dpf) {
			this(set_dpf ? (byte)1 : (byte)0);
		}
		
		public Set_dpf(byte value) {
			super(Tag.SET_DPF, value);
		}

		public Set_dpf(byte[] content) {
			super(Tag.SET_DPF.code, content);
		}
		
		public boolean isDpfSet() {
			return value == 1;
		}
	}
	
	/**
	 * The ms_availability_status parameter is used in the alert_notification operation to indicate the
	 * availability state of the MS to the ESME.
	 * If the SMSC does not include the parameter in the alert_notification operation, the ESME
	 * should assume that the MS is in an "available" state.
	 *
	 * 0 = Available (Default)
	 * 1 = Denied (e.g. suspended, no SMS capability, etc.)
	 * 2 = Unavailable
	 * values 3 to 255 are reserved
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Ms_availability_status extends OptionalParameter.Byte {
		
		public enum Ms_availability_status_enum {
			AVAILABLE(0),
			DENIED(1),
			UNAVAILABLE(2),
			RESERVED(3);
			
			protected byte value;

			Ms_availability_status_enum(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Ms_availability_status_enum toEnum(byte value) {
				for (Ms_availability_status_enum v : Ms_availability_status_enum.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public Ms_availability_status(Ms_availability_status_enum status) {
			this(status.value);
		}

		public Ms_availability_status(byte value) {
			super(Tag.MS_AVAILABILITY_STATUS, value);
		}

		public Ms_availability_status(byte[] content) {
			super(Tag.MS_AVAILABILITY_STATUS.code, content);
		}
		
		public Ms_availability_status_enum getMsAvailabilityStatus() {
			return Ms_availability_status_enum.toEnum(value);
		}
	}
	
	/**
	 * The network_error_code parameter is used to indicate the actual network error code for a
	 * delivery failure. The network error code is technology specific.
	 *
	 * Bits
	 * 7.............0
	 * XXXXXXXX (octet 1)
	 * YYYYYYYY (octet 2)
	 * YYYYYYYY (octet 3)
	 *
	 * The first octet indicates the network type.
	 * The following values are defined:
	 * 1 = ANSI-136
	 * 2 = IS-95
	 * 3 = GSM
	 * 4 = ANSI 136 Cause Code (SMPP v5.0)
	 * 5 = IS 95 Cause Code (SMPP v5.0)
	 * 6 = ANSI-41 Error (SMPP v5.0)
	 * 7 = SMPP Error (SMPP v5.0)
	 * 8 = Message Center Specific (SMPP v5.0)
	 * All other values reserved.
	 * The remaining two octets specify the actual network error code appropriate to the
	 * network type.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Network_error_code extends OptionalParameter.OctetString {
		
		public enum Network_error_code_type {
			/**
			 * 1 = ANSI-136
			 */
			ANSI_136(1),
			/**
			 * 2 = IS-95
			 */
			IS_95(2),
			/**
			 * 3 = GSM
			 */
			GSM(3),
			/**
			 * 4 = ANSI 136 Cause Code (SMPP v5.0)
			 */
			ANSI_136_CAUSE_CODE(4),
			/**
			 * 5 = IS 95 Cause Code (SMPP v5.0)
			 */
			IS_95_CAUSE_CODE(5),
			/**
			 * 6 = ANSI-41 Error (SMPP v5.0)
			 */
			ANSI_41_ERROR(6),
			/**
			 * 7 = SMPP Error (SMPP v5.0)
			 */
			SMPP_ERROR(7),
			/**
			 * 8 = Message Center Specific (SMPP v5.0)
			 */
			MESSAGE_CENTER_SPECIFIC(8),
			/**
			 * 9 and higher = Reserved
			 */
			RESERVED(9);
			
			protected byte value;

			Network_error_code_type(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Network_error_code_type toEnum(byte value) {
				for (Network_error_code_type v : Network_error_code_type.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public Network_error_code(Network_error_code_type network_type, short error_code) {
			super(Tag.NETWORK_ERROR_CODE.code, new byte[] {network_type.value, (byte)((error_code >> 8) & 0xFF), (byte)(error_code & 0xFF)});
		}
		
		public Network_error_code(byte[] content) {
			super(Tag.NETWORK_ERROR_CODE.code, content);
		}
		
		public Network_error_code_type getNetworkType() {
			return Network_error_code_type.toEnum(value[0]);
		}
		
		public short getErrorCode() {
			return (short)(((value[1] & 0xFF) << 8) | (value[2] & 0xFF));
		}
	}
	
	/**
	 * The message_payload parameter contains the user data.
	 * 
	 * The short message data should be inserted in either the short_message or message_payload fields.
	 * Both fields must not be used simultaneously.
	 * 
	 * When sending messages longer than 254 octets the message_payload parameter should be used and the
	 * sm_length parameter should be set to zero. Some SMSC always put the user data into the
	 * message_payload field, regardless of user data length.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Message_payload extends OptionalParameter.OctetString {

		public Message_payload(byte[] value) {
			super(Tag.MESSAGE_PAYLOAD.code(), value);
		}
	}
	
	/**
	 * The delivery_failure_reason parameter is used in the data_sm_resp operation to indicate the
	 * outcome of the message delivery attempt (only applicable for transaction message mode). If a
	 * delivery failure due to a network error is indicated, the ESME may check the
	 * network_error_code parameter (if present) for the actual network error code.
	 * The delivery_failure_reason parameter is not included if the delivery attempt was successful.
	 *
	 * 0 = Destination unavailable
	 * 1 = Destination Address Invalid (e.g. suspended, no SMS capability, etc.)
	 * 2 = Permanent network error
	 * 3 = Temporary network error
	 * values 4 to are 255 reserved
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Delivery_failure_reason extends OptionalParameter.Byte {
		public enum Delivery_failure_reason_enum {
			/**
			 * 0 = Destination unavailable
			 */
			DESTINATION_UNAVAILABLE(0),
			/**
			 * 1 = Destination Address Invalid (e.g. suspended, no SMS capability, etc.)
			 */
			DESTINATION_ADDRESS_INVALID(1),
			/**
			 * 2 = Permanent network error
			 */
			PERMANENT_NETWORK_ERROR(2),
			/**
			 * 3 = Temporary network error
			 */
			TEMPORARY_NETWORK_ERROR(3),
			/**
			 * values 4 to are 255 reserved
			 */
			RESERVED(4);
			
			protected byte value;

			Delivery_failure_reason_enum(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static Delivery_failure_reason_enum toEnum(byte value) {
				for (Delivery_failure_reason_enum v : Delivery_failure_reason_enum.values()) {
					if (v.value == value) {
						return v;
					}
				}
				return RESERVED;
			}
		}
		
		public Delivery_failure_reason(Delivery_failure_reason_enum status) {
			this(status.value);
		}

		public Delivery_failure_reason(byte value) {
			super(Tag.DELIVERY_FAILURE_REASON, value);
		}

		public Delivery_failure_reason(byte[] content) {
			super(Tag.DELIVERY_FAILURE_REASON.code, content);
		}
		
		public Delivery_failure_reason_enum getDeliveryFailureReason() {
			return Delivery_failure_reason_enum.toEnum(value);
		}
	}
	
	/**
	 * The more_messages_to_send parameter is used by the ESME in the submit_sm and data_sm
	 * operations to indicate to the SMSC that there are further messages for the same destination
	 * SME. The SMSC may use this setting for network resource optimization.
	 *
	 * 0 = No more messages to follow
	 * 1 = More messages to follow (default)
	 * values 2 to 255 are reserved
	 *
	 * Wireless Network Technology: GSM
	 *
	 * @author stefanth
	 */
	public static class More_messages_to_send extends OptionalParameter.Byte {
		public enum More_messages_to_send_enum {
			/**
			 * 0 = No more messages to follow
			 */
			NO_MORE_MESSAGES_TO_FOLLOW(0),
			/**
			 * 1 = More messages to follow (default)
			 */
			MORE_MESSAGES_TO_FOLLOW(1),
			/**
			 * values 2 to are 255 reserved
			 */
			RESERVED(2);
			
			protected byte value;

			More_messages_to_send_enum(int value) {
				this.value = (byte)value;
			}

			public byte value() {
				return value;
			}

			public static More_messages_to_send_enum toEnum(byte value) {
				for (More_messages_to_send_enum v : More_messages_to_send_enum.values()) {
					if (v.value == value)
						return v;
				}
				return RESERVED;
			}
		}
		
		public More_messages_to_send(More_messages_to_send_enum status) {
			this(status.value);
		}

		public More_messages_to_send(byte value) {
			super(Tag.MORE_MESSAGES_TO_SEND, value);
		}

		public More_messages_to_send(byte[] content) {
			super(Tag.MORE_MESSAGES_TO_SEND.code, content);
		}
		
		public More_messages_to_send_enum getDeliveryFailureReason() {
			return More_messages_to_send_enum.toEnum(value);
		}
	}

	/**
	 * The message_state optional parameter is used by the SMSC in the deliver_sm and data_sm
	 * PDUs to indicate to the ESME the final message state for an SMSC Delivery Receipt.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Message_state extends OptionalParameter.Byte {

		/**
		 * Represents valid values for the optional parameter message_state.
		 */
		public enum Message_state_enum {
			SCHEDULED(0),
			ENROUTE(1),
			DELIVERED(2),
			EXPIRED(3),
			DELETED(4),
			UNDELIVERABLE(5),
			ACCEPTED(6),
			UNKNOWN(7),
			REJECTED(8),
			SKIPPED(9);
			
			private byte value;

			Message_state_enum(int value) {
				this.value = (byte)value;
			}
			
			public byte byteValue() {
				return value;
			}
			public static Message_state_enum toEnum(short value) {
				for (Message_state_enum v : Message_state_enum.values()) {
					if (v.value == value)
						return v;
				}
				return null;
			}
		}
		
		public Message_state(byte value) {
			super(Tag.MESSAGE_STATE, value);
		}

		public Message_state(byte[] content) {
			super(Tag.MESSAGE_STATE.code, content);
		}
		
		/**
		 * Get the message_state value as an enum.
		 * @return An enum of type {@link Message_state_enum}
		 */
		public Message_state_enum getMessageState() {
			return Message_state_enum.toEnum(value);
		}

		@Override
		public String toString() {
			return getMessageState().toString();
		}
	}

	/**
	 * The ussd_service_op parameter is required to define the USSD service operation when SMPP
	 * is being used as an interface to a (GSM) USSD system.
	 *
	 * Wireless Network Technology: GSM (USSD)
	 *
	 * @author stefanth
	 */
	public static class Ussd_service_op extends OptionalParameter.Byte {

		public Ussd_service_op(byte value) {
			super(Tag.USSD_SERVICE_OP, value);
		}

		public Ussd_service_op(byte[] content) {
			super(Tag.USSD_SERVICE_OP.code, content);
		}
	}

	/**
	 * The broadcast_channel_indicator parameter specifies the Cell Broadcast channel that should be used for broadcasting the message.
	 *
	 * 0 = Basic Broadcast Channel (Default)
	 * 1 = Extended Broadcast Channel
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_channel_indicator extends OptionalParameter.Byte {

		public Broadcast_channel_indicator(byte value) {
			super(Tag.BROADCAST_CHANNEL_INDICATOR, value);
		}

		public Broadcast_channel_indicator(byte[] content) {
			super(Tag.BROADCAST_CHANNEL_INDICATOR.code, content);
		}
	}

	/**
	 * The broadcast_content_type parameter specifies the content_type of the message content.
	 *
	 * The first octet is a Type of Network tag indicating the network type.
	 * 0 = Generic
	 * 1 = GSM [23041]
	 * 2 = TDMA [IS824][ANSI-41]
	 * 3 = CDMA [IS824][IS637]
	 *
	 * Octets 2-3 contain the broadcast content type. The following values apply when this is set to 0 (Generic):
	 * 0x0000     Index
	 * 0x0001     Emergency Broadcasts
	 * 0x0002     IRDB Download
	 * etc.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_content_type extends OptionalParameter.OctetString {
		public Broadcast_content_type(byte[] value) {
			super(Tag.BROADCAST_CONTENT_TYPE.code, value);
		}
	}

	public static class Broadcast_content_type_info extends OptionalParameter.OctetString {
		public Broadcast_content_type_info(byte[] value) {
			super(Tag.BROADCAST_CONTENT_TYPE_INFO.code, value);
		}
	}

	/**
	 * The broadcast_message_class parameter is used to route messages when received by a mobile station to user-defined destinations or to Terminal Equipment.
	 *
	 * 0x00 = No Class Specified (default)
	 * 0x01 = Class 1 (User Defined)
	 * 0x02 = Class 2 (User Defined)
	 * 0x03 = Class 3 (Terminal Equipment)
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_message_class extends OptionalParameter.Byte {
		public Broadcast_message_class(byte[] value) {
			super(Tag.BROADCAST_MESSAGE_CLASS.code, value);
		}
	}

	/**
	 * This field indicates the number of repeated broadcasts requested by the Submitter.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_rep_num extends OptionalParameter.Short {
		public Broadcast_rep_num(short value) {
			super(Tag.BROADCAST_REP_NUM.code, value);
		}

		public Broadcast_rep_num(byte[] content) {
			super(Tag.BROADCAST_REP_NUM.code, content);
		}
	}

	/**
	 * The broadcast_frequency_interval parameter specifies the frequency interval at which the broadcasts of a message should be repeated.
	 *
	 * Octet 1: specifies the Units of Time specified as follows:
	 * Encoding          Time Unit
	 * 0x00 As frequently as possible
	 * 0x08 seconds
	 * 0x09 minutes
	 * 0x0A hours
	 * 0x0B days
	 * 0x0C weeks
	 * 0x0D months
	 * 0x0E years
	 * Octet 2 + Octet 3: specifies the number of the specified time units in an unsigned integral format.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_frequency_interval extends OptionalParameter.OctetString {
		public Broadcast_frequency_interval(byte[] value) {
			super(Tag.BROADCAST_FREQUENCY_INTERVAL.code, value);
		}
	}

	/**
	 * The broadcast_area_identifier defines the Broadcast Area in terms of a geographical descriptor.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_area_identifier extends OptionalParameter.OctetString {
		public Broadcast_area_identifier(byte[] value) {
			super(Tag.BROADCAST_AREA_IDENTIFIER.code, value);
		}
	}

	/**
	 * The broadcast_error_status parameter specifies the nature of the failure associated with a
	 * particular broadcast_area_identifier specified in a broadcast request.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_error_status extends OptionalParameter.Int {
		public Broadcast_error_status(int value) {
			super(Tag.BROADCAST_ERROR_STATUS.code, value);
		}

		public Broadcast_error_status(byte[] content) {
			super(Tag.BROADCAST_ERROR_STATUS.code, content);
		}
	}

	/**
	 * The broadcast_area_success parameter is a success rate indicator, defined as the ratio of the number of BTSs
	 * who accepted the message and the total number of BTSs who should accept the message, for a particular broadcast_area_identifier.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_area_success extends OptionalParameter.Byte {
		public Broadcast_area_success(byte value) {
			super(Tag.BROADCAST_AREA_SUCCESS.code, value);
		}

		public Broadcast_area_success(byte[] content) {
			super(Tag.BROADCAST_AREA_SUCCESS.code, content);
		}
	}

	/**
	 * The broadcast_end_time parameter indicates the date and time at which the broadcasting state of this message
	 * was set to terminated in the Message Centre.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_end_time extends OptionalParameter.COctetString {
		public Broadcast_end_time(String value) {
			super(Tag.BROADCAST_END_TIME.code, value);
		}

		public Broadcast_end_time(byte[] content) {
			super(Tag.BROADCAST_END_TIME.code, content);
		}
	}

	/**
	 * The broadcast_service_group parameter is used to specify special target groups for broadcast information.
	 *
	 * @author pmoerenhout
	 */
	public static class Broadcast_service_group extends OptionalParameter.OctetString {
		public Broadcast_service_group(byte[] value) {
			super(Tag.BROADCAST_SERVICE_GROUP.code, value);
		}
	}

	/**
	 * Billing information passed from ESME to MC
	 *
	 * Bits 7......0
	 * 0XXXXXXX (Reserved)
	 * 1XXXXXXX (Vendor Specific)
	 * The first octet represents the Billing Format tag and indicates the format of the billing information in the
	 * remaining octets.
	 * The remaining octets contain the billing information.
	 *
	 * Wireless Network Technology: Generic
	 *
	 * @author stefanth
	 */
	public static class Billing_identification extends OptionalParameter.OctetString {
		public Billing_identification(byte[] value) {
			super(Tag.BILLING_IDENTIFICATION.code, value);
		}
	}

	/**
	 * The source_network_id assigned to a wireless network operator or ESME operator is a unique address that may be derived and assigned
	 * by the node owner without establishing a central assignment and management authority.
	 * When this TLV is specified, it must be accompanied with a source_node_id TLV.
	 */
	public static class Source_network_id extends OptionalParameter.COctetString {
		public Source_network_id(String value) {
			super(Tag.SOURCE_NETWORK_ID.code, value);
		}
		public Source_network_id(byte[] content) {
			super(Tag.BROADCAST_AREA_SUCCESS.code, content);
		}
	}

	/**
	 * The dest_network_id assigned to a wireless network operator or ESME operator is a unique address that may be derived and assigned
	 * by the node owner without establishing a central assignment and management authority.
	 * When this TLV is specified, it must be accompanied with a dest_node_id TLV.
	 */
	public static class Dest_network_id extends OptionalParameter.COctetString {
		public Dest_network_id(String value) {
			super(Tag.DEST_NETWORK_ID.code, value);
		}
		public Dest_network_id(byte[] content) {
			super(Tag.DEST_NETWORK_ID.code, content);
		}
	}

	/**
	 * The source_node_id is a unique number assigned within a single ESME or MC network and must uniquely identify an originating node
	 * within the context of the MC or ESME. The content of a source_node_id is comprised of decimal digits and
	 * is at the discretion of the owning ESME or MC.
	 *
	 * Sequence of 6 decimal digits
	 */
	public static class Source_node_id extends OptionalParameter.OctetString {
		public Source_node_id(byte[] value) {
			super(Tag.SOURCE_NODE_ID.code, value);
		}
	}

	/**
	 * The dest_node_id is a unique number assigned within a single ESME or MC network and must uniquely identify a destination node
	 * within the context of the MC or ESME. The content of a dest_node_id is comprised of decimal digits and
	 * is at the discretion of the owning ESME or MC.
	 *
	 * Sequence of 6 decimal digits
	 */
	public static class Dest_node_id extends OptionalParameter.OctetString {
		public Dest_node_id(byte[] value) {
			super(Tag.DEST_NODE_ID.code, value);
		}
	}

	/**
	 * The dest_addr_np_resolution TLV is used to pass an indicator relating to a number portability query.
	 * If this TLV is omitted, the default value is assumed.
	 *
	 * 0 = query has not been performed (default)
	 * 1 = query has been performed, number not ported
	 * 2 = query has been performed, number ported
	 */
	public static class Dest_addr_np_resolution extends OptionalParameter.Byte {
		public Dest_addr_np_resolution(byte value) {
			super(Tag.DEST_ADDR_NP_RESOLUTION.code, value);
		}

		public Dest_addr_np_resolution(byte[] content) {
			super(Tag.DEST_ADDR_NP_RESOLUTION.code, content);
		}
	}

	/**
	 * The dest_addr_np_information TLV is used to carry number portability information.
	 *
	 * CDMA &amp; TDMA (North America):
	 * When the Number Portability parameters are used within the US, the information contained with the NP Information
	 * will be the Location Routing Number (LRN).
	 * A LRN is a 10-digit number, in the format NPA- NXX-XXXX, that uniquely identifies a switch or point of interconnection (POI).
	 * The NPA-NXX portion of the LRN is used to route calls to numbers that have been ported.
	 */
	public static class Dest_addr_np_information extends OptionalParameter.OctetString {
		public Dest_addr_np_information(byte[] value) {
			super(Tag.DEST_ADDR_NP_INFORMATION.code, value);
		}
	}

	/**
	 * The dest_addr_np_country TLV is used to carry E.164 information relating to the operator country code.
	 *
	 * 1 to 5 digits
	 * Country code of the origination operator (E.164 Region Code)
	 */
	public static class Dest_addr_np_country extends OptionalParameter.OctetString {
		public Dest_addr_np_country(byte[] value) {
			super(Tag.DEST_ADDR_NP_COUNTRY.code, value);
		}
	}

	/**
	 * The display_time parameter is used to associate a display time of the short message on the MS. <br>
	 * <br>
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Display_time extends OptionalParameter.Byte {

		public Display_time(byte value) {
			super(Tag.DISPLAY_TIME, value);
		}

		public Display_time(byte[] content) {
			super(Tag.DISPLAY_TIME.code, content);
		}
	}
	
	/**
	 * The sms_signal parameter is used to provide a TDMA MS with alert tone information
	 * associated with the received short message.
	 *
	 * Encoded as per [CMT-136]
	 *
	 * Wireless Network Technology: TDMA
	 *
	 * @author stefanth
	 */
	public static class Sms_signal extends OptionalParameter.Short {

		public Sms_signal(short value) {
			super(Tag.SMS_SIGNAL, value);
		}

		public Sms_signal(byte[] content) {
			super(Tag.SMS_SIGNAL.code, content);
		}
	}
	
	/**
	 * The ms_validity parameter is used to provide an MS with validity information associated with
	 * the received short message.
	 *
	 * 0 = Store Indefinitely (default)
	 * 1 = Power Down
	 * 2 = SID based registration area
	 * 3 = Display Only
	 * values 4 to 255 are reserved
	 *
	 * Wireless Network Technology: CDMA, TDMA
	 *
	 * @author stefanth
	 */
	public static class Ms_validity extends OptionalParameter.Byte {

		public Ms_validity(byte value) {
			super(Tag.MS_VALIDITY, value);
		}

		public Ms_validity(byte[] content) {
			super(Tag.MS_VALIDITY.code, content);
		}
	}
	
	/**
	 * The alert_on_message_delivery parameter is set to instruct a MS to alert the user (in a MS
	 * implementation specific manner) when the short message arrives at the MS.
	 *
	 * If length of value part is 0, then the default setting is assumed.
	 * 0 = Use mobile default alert (Default)
	 * 1 = Use low-priority alert
	 * 2 = Use medium-priority alert
	 * 3 = Use high-priority alert
	 * values 4 to 255 are reserved
	 *
	 * Wireless Network Technology: CDMA
	 *
	 * @author stefanth
	 */
	public static class Alert_on_message_delivery extends OptionalParameter.Byte {
		
		public Alert_on_message_delivery(byte value) {
			super(Tag.ALERT_ON_MESSAGE_DELIVERY, value);
		}
		
		public Alert_on_message_delivery(byte[] content) {
			this(content.length == 0 ? (byte)0 : content[0]);
		}
	}
	
	/**
	 * The its_reply_type parameter is a required parameter for the CDMA Interactive Teleservice as
	 * defined by the Korean PCS carriers [KORITS]. It indicates and controls the MS user's reply
	 * method to an SMS delivery message received from the ESME.
	 *
	 * 0 = Digit
	 * 1 = Number
	 * 2 = Telephone No.
	 * 3 = Password
	 * 4 = Character Line
	 * 5 = Menu
	 * 6 = Date
	 * 7 = Time
	 * 8 = Continu
	 * values 9 to 255 are reserved
	 *
	 * Wireless Network Technology: CDMA
	 *
	 * @author stefanth
	 */
	public static class Its_reply_type extends OptionalParameter.Byte {

		public Its_reply_type(byte value) {
			super(Tag.ITS_REPLY_TYPE, value);
		}

		public Its_reply_type(byte[] content) {
			super(Tag.ITS_REPLY_TYPE.code, content);
		}
	}
	
	/**
	 * The its_session_info parameter is a required parameter for the CDMA Interactive Teleservice
	 * as defined by the Korean PCS carriers [KORITS]. It contains control information for the
	 * interactive session between an MS and an ESME.
	 *
	 * Bits 7...............0
	 * SSSS SSSS (octet 1)
	 * NNNN NNNE (octet 2)
	 * Octet 1 contains the session number (0 - 255) encoded in binary. The session
	 * number remains constant for each session. The sequence number of the dialogue unit
	 * (as assigned by the ESME) within the session is encoded in bits 7..1 of octet 2.
	 * The End of Session Indicator indicates the message is the end of the conversation
	 * session and is encoded in bit 0 of octet 2 as follows:
	 * 0 = End of Session Indicator inactive.
	 * 1 = End of Session Indicator active.
	 *
	 * Wireless Network Technology: CDMA
	 *
	 * @author stefanth
	 */
	public static class Its_session_info extends OptionalParameter.Short {

		public Its_session_info(short value) {
			super(Tag.ITS_SESSION_INFO, value);
		}

		public Its_session_info(byte[] content) {
			super(Tag.ITS_SESSION_INFO.code, content);
		}
	}

	/**
	 * The vendor_specific_source_msc_addr parameter is used to indicate the source MSC address 
	 * over which the message originated.
	 *
	 * @author stefanth
	 */
	public static class Vendor_specific_source_msc_addr extends OptionalParameter.Vendor_specific_msc_addr {
		
		public Vendor_specific_source_msc_addr(byte[] value) {
			super(Tag.VENDOR_SPECIFIC_SOURCE_MSC_ADDR.code, value);
		}
	}

	/**
	 * The vendor_specific_dest_msc_addr parameter is used to indicate the destination MSC address 
	 * over which the message terminated. This parameter can be present in deliver_sm messages 
	 * containing delivery notifications.
	 *
	 * @author stefanth
	 */
	public static class Vendor_specific_dest_msc_addr extends OptionalParameter.Vendor_specific_msc_addr {
		
		public Vendor_specific_dest_msc_addr(byte[] value) {
			super(Tag.VENDOR_SPECIFIC_DEST_MSC_ADDR.code, value);
		}
	}

	private static class Vendor_specific_msc_addr extends OptionalParameter.OctetString {
		String address;
		
		private Vendor_specific_msc_addr(short tag, byte[] value) {
			super(tag, value);
			try {
        if (value.length >= 2) {
          address = new String(value, 2, value.length - 2, StandardCharsets.ISO_8859_1);
        }
			} catch (StringIndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		// TODO: make sure that TON is first byte and NPI is second byte
		public byte getTon() {
			return getValue()[0];
		}

		public byte getNpi() {
			return getValue()[1];
		}

		public String getAddress() {
			return address;
		}
	}

	/**
	 * The congestion_state parameter is used to pass congestion status information between ESME and MC
	 * as a means of providing flow control and congestion avoidance capabilities to the sending peer.
	 * The TLV can be used in any SMPP operation response PDU as a means of passing congestion status from one peer to another.
	 * Typical uses of this would be in submit_sm/submit_sm_resp sequences where an ESME would drive a batch of submissions
	 * at a high rate and use continual tracking of the returned congestion_state values as a means of gauging the congestion.
	 * Reaction to a variation in congestion_state would involve increasing/decreasing the rate as required to maintain the balance
	 * in the Optimum range.
	 *
	 * 0 = Idle
	 * 1-29 = Low Load
	 * 30-49 = Medium Load
	 * 50-79 = High Load
	 * 80-89 = Optimum Load
	 * 90-99 = Nearing Congestion
	 * 100 = Congested / Maximum Load
	 *
	 * Introduced in SMPP 5.0
	 *
	 * @author pmoerenhout
	 * @since 3.0
	 */
	public static class Congestion_state extends OptionalParameter.Byte {

		public Congestion_state(byte value) {
			super(Tag.CONGESTION_STATE, value);
		}

		public Congestion_state(byte[] content) {
			super(Tag.CONGESTION_STATE.code, content);
		}
	}

    /**
     * All the defined SMPP Optional Parameters.
     * 
     * @author mikko.koponen
     * @author uudashr
		 * @author pmoerenhout
     */
    public enum Tag {
        DEST_ADDR_SUBUNIT(0x0005, Dest_addr_subunit.class),
        DEST_NETWORK_TYPE(0x0006, Dest_network_type.class),
        DEST_BEARER_TYPE(0x0007, Dest_bearer_type.class), 
        DEST_TELEMATICS_ID(0x0008, Dest_telematics_id.class), 
        SOURCE_ADDR_SUBUNIT(0x000D, Source_addr_subunit.class), 
        SOURCE_NETWORK_TYPE(0x000E, Source_network_type.class),
        SOURCE_BEARER_TYPE(0x000F, Source_bearer_type.class), 
        SOURCE_TELEMATICS_ID(0x0010, Source_telematics_id.class), 
        QOS_TIME_TO_LIVE(0x0017, Qos_time_to_live.class), 
        PAYLOAD_TYPE(0x0019, Payload_type.class), 
        ADDITIONAL_STATUS_INFO_TEXT(0x001D, Additional_status_info_text.class), 
        RECEIPTED_MESSAGE_ID(0x001E, Receipted_message_id.class),
        MS_MSG_WAIT_FACILITIES(0x0030, Ms_msg_wait_facilities.class), 
        PRIVACY_INDICATOR(0x0201, Privacy_indicator.class), 
        SOURCE_SUBADDRESS(0x0202, Source_subaddress.class), 
        DEST_SUBADDRESS(0x0203, Dest_subaddress.class), 
        USER_MESSAGE_REFERENCE(0x0204, User_message_reference.class), 
        USER_RESPONSE_CODE(0x0205, User_response_code.class), 
        SOURCE_PORT(0x020a, Source_port.class),
        DESTINATION_PORT(0x020b, Destination_port.class),
        SAR_MSG_REF_NUM(0x020c, Sar_msg_ref_num.class),
        LANGUAGE_INDICATOR(0x020d, Language_indicator.class),
        SAR_TOTAL_SEGMENTS(0x020e, Sar_total_segments.class),
        SAR_SEGMENT_SEQNUM(0x020f, Sar_segment_seqnum.class),
        SC_INTERFACE_VERSION(0x0210, Sc_interface_version.class), 
        CALLBACK_NUM_PRES_IND(0x0302, Callback_num_pres_ind.class), 
        CALLBACK_NUM_ATAG(0x0303, Callback_num_atag.class), 
        NUMBER_OF_MESSAGES(0x0304, Number_of_messages.class), 
        CALLBACK_NUM(0x0381, Callback_num.class), 
        DPF_RESULT(0x0420, Dpf_result.class), 
        SET_DPF(0x0421, Set_dpf.class), 
        MS_AVAILABILITY_STATUS(0x0422, Ms_availability_status.class), 
        NETWORK_ERROR_CODE(0x0423, Network_error_code.class), 
        MESSAGE_PAYLOAD(0x0424, Message_payload.class),
        DELIVERY_FAILURE_REASON(0x0425, Delivery_failure_reason.class), 
        MORE_MESSAGES_TO_SEND(0x0426, More_messages_to_send.class), 
        MESSAGE_STATE(0x0427, Message_state.class),
			  CONGESTION_STATE(0x0428, Congestion_state.class),
        USSD_SERVICE_OP(0x0501, Ussd_service_op.class),
        BROADCAST_CHANNEL_INDICATOR(0x0600, Broadcast_channel_indicator.class),
        BROADCAST_CONTENT_TYPE(0x0601, Broadcast_content_type.class),
        BROADCAST_CONTENT_TYPE_INFO(0x0602, Broadcast_content_type_info.class),
        BROADCAST_MESSAGE_CLASS(0x0603, Broadcast_message_class.class),
        BROADCAST_REP_NUM(0x0604, Broadcast_rep_num.class),
        BROADCAST_FREQUENCY_INTERVAL(0x0605, Broadcast_frequency_interval.class),
        BROADCAST_AREA_IDENTIFIER(0x0606, Broadcast_area_identifier.class),
        BROADCAST_ERROR_STATUS(0x0607, Broadcast_error_status.class),
        BROADCAST_AREA_SUCCESS(0x0608, Broadcast_area_success.class),
        BROADCAST_END_TIME(0x0609, Broadcast_end_time.class),
        BROADCAST_SERVICE_GROUP(0x060a, Broadcast_service_group.class),
			  BILLING_IDENTIFICATION(0x060b, Billing_identification.class),
        SOURCE_NETWORK_ID(0x060d, Source_network_id.class),
        DEST_NETWORK_ID(0x060e, Dest_network_id.class),
        SOURCE_NODE_ID(0x060f, Source_node_id.class),
        DEST_NODE_ID(0x0610, Dest_node_id.class),
        DEST_ADDR_NP_RESOLUTION(0x0611, Dest_addr_np_resolution.class),
        DEST_ADDR_NP_INFORMATION(0x0612, Dest_addr_np_information.class),
        DEST_ADDR_NP_COUNTRY(0x0613, Dest_addr_np_country.class),
        DISPLAY_TIME(0x1201, Display_time.class),
        SMS_SIGNAL(0x1203, Sms_signal.class), 
        MS_VALIDITY(0x1204, Ms_validity.class), 
        ALERT_ON_MESSAGE_DELIVERY(0x130c, Alert_on_message_delivery.class),
        ITS_REPLY_TYPE(0x1380, Its_reply_type.class), 
        ITS_SESSION_INFO(0x1383, Its_session_info.class),
        VENDOR_SPECIFIC_SOURCE_MSC_ADDR(0x1501, Vendor_specific_source_msc_addr.class),
        VENDOR_SPECIFIC_DEST_MSC_ADDR(0x1502, Vendor_specific_dest_msc_addr.class);

        private final short code;
        final Class<? extends OptionalParameter> type;

         Tag(int code, Class<? extends OptionalParameter> type) {
            this.code = (short)code;
            this.type = type;
        }

        /**
         * Get the tag code of the {@link Tag}.
         * 
         * @return the tag code.
         */
        public short code() {
            return code;
        }
        
        public Class<? extends OptionalParameter> type() {
            return type;
        }

        public static Tag valueOf(short code) {
            for (Tag tag : Tag.values()) {
                if (tag.code == code)
                    return tag;
            }
            return null;
        }
    }

	  @Override
	  public int hashCode() {
		    return Objects.hash(tag);
	  }

	  @Override
	  public boolean equals(final Object o) {
		  if (this == o) {
			  return true;
		  }
		  if (!(o instanceof OptionalParameter)) {
			  return false;
		  }
		  final OptionalParameter that = (OptionalParameter) o;
		  return tag == that.tag;
	  }
}
