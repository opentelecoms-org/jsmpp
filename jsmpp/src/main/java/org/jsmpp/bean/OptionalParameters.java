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

import org.jsmpp.bean.OptionalParameter.Byte;
import org.jsmpp.bean.OptionalParameter.COctetString;
import org.jsmpp.bean.OptionalParameter.Int;
import org.jsmpp.bean.OptionalParameter.Null;
import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Short;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * @author pmoerenhout
 *
 */
public class OptionalParameters {

    public static final OptionalParameter[] EMPTY_OPTIONAL_PARAMETERS = new OptionalParameter[]{};

    private static final Logger log = LoggerFactory.getLogger(OptionalParameters.class);

    private OptionalParameters() {
        throw new InstantiationError("This class must not be instantiated");
    }

    /**
     * Create SAR_MESSAGE_REF_NUM TLV instance.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Short newSarMsgRefNum(short value) {
        return new OptionalParameter.Short(Tag.SAR_MSG_REF_NUM, value);
    }

    /**
     * Create SAR_MESSAGE_REF_NUM TLV instance.
     * The value will cast automatically into short type.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Short newSarMsgRefNum(int value) {
        return newSarMsgRefNum((byte)value);
    }

    /**
     * Create SAR_SEGMENT_SEQNUM TLV instance.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarSegmentSeqnum(byte value) {
        return new OptionalParameter.Byte(Tag.SAR_SEGMENT_SEQNUM, value);
    }

    /**
     * Create SAR_SEGMENT_SEQNUM TLV instance.
     * The value will cast automatically into byte type.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarSegmentSeqnum(int value) {
        return newSarSegmentSeqnum((byte)value);
    }

    /**
     * Create SAR_TOTAL_SEGMENTS TLV instance.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarTotalSegments(byte value) {
        return new OptionalParameter.Byte(Tag.SAR_TOTAL_SEGMENTS, value);
    }

    /**
     * Create SAR_TOTAL_SEGMENTS TLV instance.
     * The value will cast automatically into byte type.
     *
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarTotalSegments(int value) {
        return newSarTotalSegments((byte)value);
    }

    /**
     * Deserialize all recognized tag code to {@link OptionalParameter} object.
     * Unrecognized will be classified as {@link COctetString}.
     *
     * @param tagCode is the tag code.
     * @param content is the content.
     * @return the OptionalParameter object.
     */
    public static OptionalParameter deserialize(short tagCode, byte[] content) {
        Tag tag = Tag.valueOf(tagCode);
        if (tag == null) {
            log.debug("Optional Parameter Tag not recognized for deserialization: {}", tagCode);
            return new OctetString(tagCode, content);
        }

        switch(tag)
        {
            case DEST_ADDR_SUBUNIT:
                return new OptionalParameter.Dest_addr_subunit(content);
            case DEST_NETWORK_TYPE:
                return new OptionalParameter.Dest_network_type(content);
            case DEST_BEARER_TYPE:
                return new OptionalParameter.Dest_bearer_type(content);
            case DEST_TELEMATICS_ID:
                return new OptionalParameter.Dest_telematics_id(content);
            case SOURCE_ADDR_SUBUNIT:
                return new OptionalParameter.Source_addr_subunit(content);
            case SOURCE_NETWORK_TYPE:
                return new OptionalParameter.Source_network_type(content);
            case SOURCE_BEARER_TYPE:
                return new OptionalParameter.Source_bearer_type(content);
            case SOURCE_TELEMATICS_ID:
                return new OptionalParameter.Source_telematics_id(content);
            case QOS_TIME_TO_LIVE:
                return new OptionalParameter.Qos_time_to_live(content);
            case PAYLOAD_TYPE:
                return new OptionalParameter.Payload_type(content);
            case ADDITIONAL_STATUS_INFO_TEXT:
            	return new OptionalParameter.Additional_status_info_text(content);
            case RECEIPTED_MESSAGE_ID:
                return new OptionalParameter.Receipted_message_id(content);
            case MS_MSG_WAIT_FACILITIES:
            	return new OptionalParameter.Ms_msg_wait_facilities(content);
            case PRIVACY_INDICATOR:
            	return new OptionalParameter.Privacy_indicator(content);
            case SOURCE_SUBADDRESS:
            	return new OptionalParameter.Source_subaddress(content);
            case DEST_SUBADDRESS:
            	return new OptionalParameter.Dest_subaddress(content);
            case USER_MESSAGE_REFERENCE:
            	return new OptionalParameter.User_message_reference(content);
            case USER_RESPONSE_CODE:
            	return new OptionalParameter.User_response_code(content);
            case SOURCE_PORT:
            	return new OptionalParameter.Source_port(content);
            case DESTINATION_PORT:
            	return new OptionalParameter.Destination_port(content);
            case SAR_MSG_REF_NUM:
            	return new OptionalParameter.Sar_msg_ref_num(content);
            case LANGUAGE_INDICATOR:
            	return new OptionalParameter.Language_indicator(content);
            case SAR_TOTAL_SEGMENTS:
            	return new OptionalParameter.Sar_total_segments(content);
            case SAR_SEGMENT_SEQNUM:
            	return new OptionalParameter.Sar_segment_seqnum(content);
            case SC_INTERFACE_VERSION:
                return new OptionalParameter.Sc_interface_version(content);
            case CALLBACK_NUM_PRES_IND:
            	return new OptionalParameter.Callback_num_pres_ind(content);
            case CALLBACK_NUM_ATAG:
            	return new OptionalParameter.Callback_num_atag(content);
            case NUMBER_OF_MESSAGES:
            	return new OptionalParameter.Number_of_messages(content);
            case CALLBACK_NUM:
            	return new OptionalParameter.Callback_num(content);
            case DPF_RESULT:
            	return new OptionalParameter.Dpf_result(content);
            case SET_DPF:
            	return new OptionalParameter.Set_dpf(content);
            case MS_AVAILABILITY_STATUS:
            	return new OptionalParameter.Ms_availability_status(content);
            case NETWORK_ERROR_CODE:
            	return new OptionalParameter.Network_error_code(content);
            case MESSAGE_PAYLOAD:
                return new OptionalParameter.Message_payload(content);
            case DELIVERY_FAILURE_REASON:
            	return new OptionalParameter.Delivery_failure_reason(content);
            case MORE_MESSAGES_TO_SEND:
            	return new OptionalParameter.More_messages_to_send(content);
            case MESSAGE_STATE:
                return new OptionalParameter.Message_state(content);
            case CONGESTION_STATE:
                return new OptionalParameter.Congestion_state(content);
            case USSD_SERVICE_OP:
            	return new OptionalParameter.Ussd_service_op(content);
            case BROADCAST_CHANNEL_INDICATOR:
                return new OptionalParameter.Broadcast_channel_indicator(content);
            case BROADCAST_CONTENT_TYPE:
                return new OptionalParameter.Broadcast_content_type(content);
            case BROADCAST_CONTENT_TYPE_INFO:
                return new OptionalParameter.Broadcast_content_type_info(content);
            case BROADCAST_MESSAGE_CLASS:
                return new OptionalParameter.Broadcast_message_class(content);
            case BROADCAST_REP_NUM:
                return new OptionalParameter.Broadcast_rep_num(content);
            case BROADCAST_FREQUENCY_INTERVAL:
                return new OptionalParameter.Broadcast_frequency_interval(content);
            case BROADCAST_AREA_IDENTIFIER:
                return new OptionalParameter.Broadcast_area_identifier(content);
            case BROADCAST_ERROR_STATUS:
                return new OptionalParameter.Broadcast_error_status(content);
            case BROADCAST_AREA_SUCCESS:
                return new OptionalParameter.Broadcast_area_success(content);
            case BROADCAST_END_TIME:
                return new OptionalParameter.Broadcast_end_time(content);
            case BROADCAST_SERVICE_GROUP:
                return new OptionalParameter.Broadcast_service_group(content);
            case BILLING_IDENTIFICATION:
            	return new OptionalParameter.Billing_identification(content);
            case SOURCE_NETWORK_ID:
                return new OptionalParameter.Source_network_id(content);
            case DEST_NETWORK_ID:
                return new OptionalParameter.Dest_network_id(content);
            case SOURCE_NODE_ID:
                return new OptionalParameter.Source_node_id(content);
            case DEST_NODE_ID:
                return new OptionalParameter.Dest_node_id(content);
            case DEST_ADDR_NP_RESOLUTION:
                return new OptionalParameter.Dest_addr_np_resolution(content);
            case DEST_ADDR_NP_INFORMATION:
                return new OptionalParameter.Dest_addr_np_information(content);
            case DEST_ADDR_NP_COUNTRY:
                return new OptionalParameter.Dest_addr_np_country(content);
            case DISPLAY_TIME:
            	return new OptionalParameter.Display_time(content);
            case SMS_SIGNAL:
            	return new OptionalParameter.Sms_signal(content);
            case MS_VALIDITY:
            	return new OptionalParameter.Ms_validity(content);
            case ALERT_ON_MESSAGE_DELIVERY:
            	return new OptionalParameter.Alert_on_message_delivery(content);
            case ITS_REPLY_TYPE:
            	return new OptionalParameter.Its_reply_type(content);
            case ITS_SESSION_INFO:
            	return new OptionalParameter.Its_session_info(content);
            case VENDOR_SPECIFIC_SOURCE_MSC_ADDR:
                return new OptionalParameter.Vendor_specific_source_msc_addr(content);
            case VENDOR_SPECIFIC_DEST_MSC_ADDR:
                return new OptionalParameter.Vendor_specific_dest_msc_addr(content);
            default:
                log.warn("Missing code in deserialize to handle Optional Parameter Tag: {}", tag);
        }

        // fallback
        log.warn("Falling back to basic OptionalParameter types for {}", tag);
        if (Null.class.isAssignableFrom(tag.type)) {
            return new Null(tagCode);
        }
        if (Byte.class.isAssignableFrom(tag.type)) {
            return new Byte(tagCode, content);
        }
        if (Short.class.isAssignableFrom(tag.type)) {
            return new Short(tagCode, content);
        }
        if (Int.class.isAssignableFrom(tag.type)) {
            return new Int(tagCode, content);
        }
        if (OctetString.class.isAssignableFrom(tag.type)) {
            return new OctetString(tagCode, content);
        }
        if (COctetString.class.isAssignableFrom(tag.type)) {
            return new COctetString(tagCode, content);
        }
        throw new IllegalArgumentException("Unsupported tag: " + tagCode);
    }

    @SuppressWarnings("unchecked")
    public static <U extends OptionalParameter> U get(Class<U> tagClass, OptionalParameter[] parameters)
    {
        if (parameters != null) {
          for (OptionalParameter i : parameters) {
            if (i.getClass() == tagClass) {
              return (U) i;
            }
          }
        }
        return null;
    }

    public static OptionalParameter get(short tag, OptionalParameter[] parameters)
    {
        if (parameters != null) {
          for (OptionalParameter i : parameters) {
            if (i.tag == tag) {
              return i;
            }
          }
        }
        return null;
    }
}
