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
package org.jsmpp.util;

import org.jsmpp.InvalidNumberOfDestinationsException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DestinationAddress;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.UnsuccessDelivery;

/**
 * This is utility to compose the PDU from parameter values to byte.
 *
 * @author uudashr
 *
 */
public interface PDUComposer {

    /**
     * Compose the header.
     *
     * @param commandId the command identifier
     * @param commandStatus the command status
     * @param sequenceNumber the sequence number
     * @return the composed header byte values.
     */
    byte[] composeHeader(int commandId, int commandStatus, int sequenceNumber);

    /**
     * Compose bind (bind).
     *
     * @param commandId the bind transmitter, receiver of transceiver command id
     * @param sequenceNumber An unique sequence number.
     *                       The associated bind_transmitter_resp PDU will echo the same sequence number.
     * @param systemId Identifies the ESME system requesting to bind as a transmitter with the MC.
     * @param password The password may be used by the MC to authenticate the ESME requesting to bind.
     * @param systemType Identifies the type of ESME system requesting to bind with the MC.
     * @param interfaceVersion Identifies the version of the SMPP protocol supported by the ESME.
     * @param addrTon Type of Number (TON) for ESME address(es) served via this SMPP session.
     *                Set to 0 (Unknown) if not known.
     * @param addrNpi Numbering Plan Indicator (NPI) for ESME address(es) served via this SMPP session.
     *                Set to 0 (Unknown) if not known.
     * @param addressRange A single ESME address or a range of ESME addresses served via this SMPP session.
     *                     Set to 0 if not known.
     * @return the composed bind PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] bind(int commandId, int sequenceNumber, String systemId,
            String password, String systemType, byte interfaceVersion,
            byte addrTon, byte addrNpi, String addressRange)
            throws PDUStringException;

    /**
     * Compose bind response (bind_transmitter_resp, bind_receiver_resp, bind_transceiver_resp).
     *
     * @param commandId the bind_transmitter_resp, bind_receiver_resp of bind_transceiver_resp command id
     * @param sequenceNumber The sequence number of original bind request.
     * @param systemId MC identifier, which identifies the MC to the ESME.
     * @param optionalParameters Optional parameters, including sc_interface_version to hold the SMPP version supported by MC.
     * @return the composed bind_response PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] bindResp(int commandId, int sequenceNumber, String systemId,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose unbind (unbind).
     *
     * @param sequenceNumber An unique sequence number. The associated unbind_resp PDU will echo the same sequence number.
     * @return the composed unbind PDU byte values.
     */
    byte[] unbind(int sequenceNumber);

    /**
     * Compose unbind response (unbind_resp).
     *
     * @param commandStatus Not used. Set to 0.
     * @param sequenceNumber An unique sequence number. The associated unbind_resp PDU will echo the same sequence number.
     * @return the composed unbind_resp PDU.
     */
    byte[] unbindResp(int commandStatus, int sequenceNumber);

    /**
     * Compose outbind (outbind).
     *
     * @param sequenceNumber An unique sequence number. The associated unbind_resp PDU will echo the same sequence number.
     * @param systemId Identifies the MC to the ESME.
     * @param password The password (max 8 characters) may be used by the ESME for security reasons to authenticate the MC originating the outbind.
     * @return the composed outbind PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] outbind(int sequenceNumber, String systemId, String password)
            throws PDUStringException;

    /**
     * Compose enquire link (enquire_link).
     *
     * @param sequenceNumber An unique sequence number. The associated enquire_link_resp PDU should echo the same sequence number.
     * @return the composed enquire_link PDU bytes.
     */
    byte[] enquireLink(int sequenceNumber);

    /**
     * Compose enquire link response (enquire_link_resp).
     *
     * @param sequenceNumber The same sequence number of original enquire_link PDU.
     * @return the composed enquire_link_resp PDU byte values.
     */
    byte[] enquireLinkResp(int sequenceNumber);

    /**
     * Compose generic nack (generic_nack).
     *
     * @param commandStatus Error code corresponding to reason for sending the generic_nack.
     * @param sequenceNumber Sequence number of original PDU or to 0 if the original PDU cannot be decoded.
     * @return the composed generic_nack PDU.
     */
    byte[] genericNack(int commandStatus, int sequenceNumber);

    /**
     * Compose submit short message (submit_sm).
     * 
     * @param sequenceNumber An unique sequence number. The associated submit_sm_resp PDU will echo this sequence number.
     * @param serviceType The service_type parameter can be used to indicate the SMS Application service associated
     *                    with the message.
     *                    Specifying the service_type allows the ESME to avail of enhanced messaging services
     *                    such as “replace by service_type” or to control the teleservice used on the air interface.
     *                    Set to null for default MC settings
     * @param sourceAddrTon Type of Number for source address. If not known, set to 0 (Unknown).
     * @param sourceAddrNpi Numbering Plan Indicator for source address. If not known, set to 0 (Unknown).
     * @param sourceAddr Address of SME which originated this message. If not known, set to null (Unknown).
     * @param destAddrTon Type of Number for destination.
     * @param destAddrNpi Numbering Plan Indicator for destination.
     * @param destinationAddr Destination address of this short message. For mobile terminated messages, this is the directory number of the recipient MS.
     * @param esmClass Indicates Message Mode and Message Type.
     * @param protocolId Protocol Identifier. Network specific field.
     * @param priorityFlag Designates the priority level of the message.
     * @param scheduleDeliveryTime The short message is to be scheduled by the MC for delivery. Set to null for immediate message delivery.
     * @param validityPeriod The validity period of this message. Set to null to request the MC default validity period.
     *                       Note: this is superseded by the qos_time_to_live TLV if specified.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt, manual ACK, delivery ACK or an intermediate notification is required.
     * @param replaceIfPresentFlag Flag indicating if the submitted message should replace an existing message.
     * @param dataCoding Encoding scheme of the short message user data.
     * @param smDefaultMsgId Indicates the short message to send from a list of pre-defined (‘canned’) short messages stored on the MC.
     *                       If not using a MC canned message, set to 0.
     * @param shortMessage Up to 255 octets of short message user data.
     *                     The exact physical limit for short_message size may vary according to the underlying network.
     *                     Note: this field is superceded by the message_payload TLV if specified.
     * @param optionalParameters Message submission TLV's
     * @return the composed submit_sm PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] submitSm(int sequenceNumber, String serviceType, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, byte destAddrTon,
            byte destAddrNpi, String destinationAddr, byte esmClass,
            byte protocolId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, byte registeredDelivery,
            byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUStringException;

    /**
     * Compose submit short message response (submit_sm_resp).
     * 
     * @param sequenceNumber The sequence number of original submit_sm PDU.
     * @param messageId The MC message ID of the submitted message.
     *                  It may be used at a later stage to query the status of a message, cancel or replace the message.
     * @param optionalParameters Optional parameter TLV's for SMPP 5.0
     * @return the composed submit_sm_resp PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] submitSmResp(int sequenceNumber, String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException;

    /**
     * Compose query short message (query_sm).
     *
     * @param sequenceNumber Unique sequence number. The associated query_sm_resp PDU should echo the same sequence number.
     * @param messageId Message ID of the message whose state is to be queried.
     *                  This must be the MC assigned Message ID allocated to the original short message when submitted to the MC
     *                  by the submit_sm, data_sm or submit_multi command, and returned in the response PDU by the MC.
     * @param sourceAddrTon Type of Number of message originator.
     *                      This is used for verification purposes, and must match that supplied in the original request PDU (e.g. submit_sm).
     *                      If not known, set to 0.
     * @param sourceAddrNpi Numbering Plan Identity of message originator.
     *                      This is used for verification purposes, and must match that supplied in the original message submission request PDU.
     *                      If not known, set to 0.
     * @param sourceAddr Address of message originator.
     *                   This is used for verification purposes, and must match that supplied in the original request PDU (e.g. submit_sm).
     *                   If not known, set to null.
     * @return the composed query_sm PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] querySm(int sequenceNumber, String messageId, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr) throws PDUStringException;

    /**
     * Compose query short message response (query_sm_resp).
     *
     * @param sequenceNumber The sequence number of original query_sm PDU.
     * @param messageId MC Message ID of the message whose state is being queried.
     * @param finalDate Date and time when the queried message reached a final state.
     *                  For messages, which have not yet reached a final state, this field will contain null.
     * @param messageState Specifies the status of the queried short message.
     * @param errorCode Where appropriate this holds a network error code defining the reason for failure of message delivery.
     *                  The range of values returned depends on the underlying telecommunications network.
     * @return the composed query_sm_resp PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] querySmResp(int sequenceNumber, String messageId, String finalDate,
            byte messageState, byte errorCode) throws PDUStringException;

    /**
     * Compose deliver short message (deliver_sm) PDU.
     *
     * @param sequenceNumber An unique sequence number. The associated deliver_sm_resp PDU will echo this sequence number.
     * @param serviceType The service_type parameter can be used to indicate the SMS Application service associated with the message.
     *                    Specifying the service_type allows the ESME to avail of enhanced messaging services such as “replace by service_type”
     *                    or control the teleservice used on the air interface.
     *                    Set to null if not known by MC
     * @param sourceAddrTon Type of Number for source address.
     * @param sourceAddrNpi Numbering Plan Indicator for source address.
     * @param sourceAddr Address of SME which originated this message.
     * @param destAddrTon Type of Number for destination
     * @param destAddrNpi Numbering Plan Indicator for destination
     * @param destinationAddr Destination address of this short message For mobile terminated messages, this is the directory number of the recipient MS.
     * @param esmClass Indicates Message Mode and Message Type
     * @param protocolId Protocol Identifier (Network specific)
     * @param priorityFlag Designates the priority level of the message.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt or an SME acknowledgement is required.
     * @param dataCoding Defines the encoding scheme of the short message user data.
     * @param shortMessage Up to 254 (SMPP 3) or 255 (SMPP 5) octets of short message user data.
     *                     The exact physical limit for short_message size may vary according to the underlying network
     *                     Note: this field is superceded by the message_payload TLV if specified.
     *                     Applications which need to send messages longer than 255 octets should use the message_payload TLV.
     * @param optionalParameters Message submission TLV's
     * @return the composed deliver_sm PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] deliverSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            byte registeredDelivery, byte dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose deliver short message response (deliver_sm_resp).
     *
     * @param commandStatus Indicates outcome of deliver_sm request.
     * @param sequenceNumber The sequence number of original deliver_sm PDU.
     * @param messageId This field is unused and should set to null.
     * @return the composed deliver_sm_resp PDU byte values.
     */
    byte[] deliverSmResp(int commandStatus, int sequenceNumber, String messageId);

    /**
     * Compose data short message (data_sm) PDU.
     *
     * @param sequenceNumber An unique sequence number. The associated data_sm_resp PDU will echo this sequence number.
     * @param serviceType The service_type parameter can be used to indicate the SMS Application service associated with the message.
     *                    Specifying the service_type allows the ESME to avail of enhanced messaging services such as “replace by service_type”
     *                    or control the teleservice used on the air interface.
     *                    Set to null for default MC settings.
     * @param sourceAddrTon Type of Number for source address. If not known, set to 0 (Unknown).
     * @param sourceAddrNpi Numbering Plan Indicator for source address. If not known, set to 0 (Unknown).
     * @param sourceAddr Address of SME which originated this message. If not known, set to null (Unknown).
     * @param destAddrTon Type of Number for destination.
     * @param destAddrNpi Numbering Plan Indicator for destination.
     * @param destinationAddr Destination address of this short message. For mobile terminated messages, this is the directory number of the recipient MS.
     * @param esmClass Indicates Message Mode and Message Type.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt or an SME acknowledgement is required.
     * @param dataCoding Defines the encoding scheme of the short message user data.
     * @param optionalParameters Message Submission TLV's.
     * @return the composed data_sm PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] dataSm(int sequenceNumber, String serviceType, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, byte destAddrTon,
            byte destAddrNpi, String destinationAddr, byte esmClass,
            byte registeredDelivery, byte dataCoding,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose data short message response (data_sm_resp) PDU.
     *
     * @param sequenceNumber is the sequence number.
     * @param messageId is the the message identifier.
     * @param optionalParameters is the optional parameter(s).
     * @return the composed data_sm_resp PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] dataSmResp(int sequenceNumber, String messageId,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose cancel short message (cancel_sm) PDU.
     * 
     * @param sequenceNumber Unique sequence number. The associated cancel_sm_resp PDU should echo the same sequence number.
     * @param serviceType The SMS Application service, if cancellation of a group of application service messages is desired.
     *                    Otherwise set to null.
     * @param messageId Message ID of the message to be cancelled.
     *                  This must be the MC assigned Message ID of the original message.
     *                  Set to null if cancelling a group of messages.
     * @param sourceAddrTon The Type of Number of message originator.
     *                      This is used for verification purposes, and must match that supplied in the original message submission request PDU.
     *                      If not known, set to 0.
     * @param sourceAddrNpi Numbering Plan Identity of message originator.
     *                      This is used for verification purposes, and must match that supplied in the original message submission request PDU.
     *                      If not known, set to 0.
     * @param sourceAddr Source address of message(s) to be cancelled.
     *                   This is used for verification purposes, and must match that supplied in the original message submission request PDU(s).
     *                   If not known, set to null.
     * @param destAddrTon Type of number of destination SME address of the message(s) to be cancelled.
     *                    This is used for verification purposes, and must match that supplied in the original message submission request PDU (e.g. submit_sm).
     *                    May be set to 0 when the message_id is provided.
     * @param destAddrNpi Numbering Plan Indicator of destination SME address of the message(s) to be cancelled.
     *                    This is used for verification purposes, and must match that supplied in the original message submission request PDU.
     *                    May be set to 0 when the message_id is provided.
     * @param destinationAddr Destination address of message(s) to be cancelled.
     *                        This is used for verification purposes, and must match that supplied in the original message submission request PDU.
     *                        May be set to null when the message_id is provided.
     * @return the composed cancel_sm PDU
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] cancelSm(int sequenceNumber, String serviceType, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr)
            throws PDUStringException;

    /**
     * Compose cancel short message response (cancel_sm_resp) PDU.
     *
     * @param sequenceNumber Unique sequence number. The associated cancel_sm_resp PDU should echo the same sequence number.
     * @return the composed cancel_sm_resp PDU
     */
    byte[] cancelSmResp(int sequenceNumber);

    /**
     * Compose replace short message response (replace_sm) PDU.
     *
     * @param sequenceNumber An unique sequence number. The associated replace_sm_resp PDU will echo this sequence number.
     * @param messageId Message ID of the message to be replaced. This must be the MC assigned Message ID allocated to the original short message
     *                 when submitted to the MC by the submit_sm, data_sm or submit_multi command, and returned in the response PDU by the MC.
     * @param sourceAddrTon Type of Number of message originator.
     *                      This is used for verification purposes, and must match that supplied in the original request PDU (e.g. submit_sm).
     *                      If not known, set to 0 (Unknown).
     * @param sourceAddrNpi Numbering Plan Indicator for source address of original message.
     *                      If not known, set to 0 (Unknown).
     * @param sourceAddr Address of SME, which originated this message.
     *                   If not known, set to NULL (Unknown).
     * @param scheduleDeliveryTime New scheduled delivery time for the short message.
     *                             Set to NULL to preserve the original scheduled delivery time.
     * @param validityPeriod New expiry time for the short message. Set to null to preserve the original validity period setting.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt, user/manual or delivery ACK or intermediate notification is required.
     * @param smDefaultMsgId Indicates the short message to send from a list of pre- defined (‘canned’) short messages stored on the MC.
     *                       If not using a MC canned message, set to 0.
     * @param shortMessage Up to 254 (SMPP 3)/255 (SMPP 5) octets of short message user data.
     *                     The exact physical limit for short_message size may vary according to the underlying network
     *                     Note: this field is superceded by the message_payload TLV if specified.
     *                     Applications which need to send messages longer than 255 octets should use the message_payload TLV.
     * @return the composed byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] replaceSm(int sequenceNumber, String messageId, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, byte registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage)
        throws PDUStringException;

    /**
     * Compose replace short message response (replace_sm_resp) PDU.
     *
     * @param sequenceNumber The sequence number of original replace_sm PDU.
     * @return the composed byte values.
     */
    byte[] replaceSmResp(int sequenceNumber);

    /**
     * Compose submit multi short messages (submit_multi) PDU.
     *
     * @param sequenceNumber An unique sequence number. The associated submit_multi_resp PDU will echo this sequence number.
     * @param serviceType The service_type parameter can be used to indicate the SMS Application service associated with the message.
     *                    Specifying the service_type allows the ESME to avail of enhanced messaging services such as “replace by service_type” or control the teleservice used on the air interface.
     *                    Set to null for default MC settings.
     * @param sourceAddrTon Type of Number for source address. If not known, set to 0 (Unknown).
     * @param sourceAddrNpi Numbering Plan Indicator for source address. If not known, set to 0 (Unknown).
     * @param sourceAddr Address of SME which originated this message. If not known, set to null (Unknown).
     * @param destinationAddresses Maximum of 255 destination addresses, use 1 destination to send to one distribution list.
     * @param esmClass Indicates Message Mode and Message Type.
     * @param protocolId Protocol Identifier. Network specific field.
     * @param priorityFlag Designates the priority level of the message.
     * @param scheduleDeliveryTime The short message is to be scheduled by the MC for delivery. Set to null for immediate message delivery.
     * @param validityPeriod The validity period of this message. Set to null to request the MC default validity period.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt or an SME acknowledgement is required.
     * @param replaceIfPresentFlag Flag indicating if submitted message should replace an existing message.
     * @param dataCoding Defines the encoding scheme of the short message user data.
     * @param smDefaultMsgId Indicates the short message to send from a list of pre-defined (‘canned’) short messages stored on the MC.
     *                       If not using a MC canned message, set to 0.
     * @param shortMessage Up to 254 (SMPP 3) or 255 (SMPP 5) octets of short message user data.
     *                     The exact physical limit for short_message size may vary according to the underlying network.
     *                     Applications which need to send messages longer than 255 octets should use the message_payload TLV.
     * @param optionalParameters Message Submission Response TLVs.
     * @return the composed submit_multi PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws InvalidNumberOfDestinationsException Invalid number of destination addresses specified.
     */
    byte[] submitMulti(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            DestinationAddress[] destinationAddresses, byte esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, byte registeredDelivery,
            byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUStringException, InvalidNumberOfDestinationsException;

    /**
     * Submit multi short message response (submit_multi_resp).
     *
     * @param sequenceNumber The sequence number of original submit_multi PDU.
     * @param messageId This field contains the MC message ID of the submitted message.
     *                  It may be used at a later stage to query the status of a message, cancel or replace the message.
     * @param unsuccessDeliveries Unsuccessful SME
     * @return the composed submit_multi_resp PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] submitMultiResp(int sequenceNumber, String messageId,
            UnsuccessDelivery... unsuccessDeliveries) throws PDUStringException;

    /**
     * Alert notification (alert_notification).
     *
     * @param sequenceNumber An unique sequence number.
     * @param sourceAddrTon Type of Number for alert SME.
     * @param sourceAddrNpi Numbering Plan Indicator for alert SME.
     * @param sourceAddr Address of alert SME.
     * @param esmeAddrTon Type of Number for ESME address which requested the alert.
     * @param esmeAddrNpi Numbering Plan Indicator for ESME address which requested the alert.
     * @param esmeAddr Address for ESME which requested String the alert.
     * @param optionalParameters Optional TLV, including ms_availability_status to set the status of the mobile station.
     * @return the composed alert_notification PDU byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] alertNotification(int sequenceNumber, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, byte esmeAddrTon,
            byte esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException;

    byte[] broadcastSm(int sequenceNumber,
            String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            String messageId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId,
            OptionalParameter... optionalParameters) throws PDUStringException;

    byte[] broadcastSmResp(int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters) throws PDUStringException;

    byte[] cancelBroadcastSm(int sequenceNumber,
               String serviceType, String messageId,
               byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
               OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose cancel broadcast short message response (cancel_broadcast_sm_resp) PDU.
     *
     * @param sequenceNumber The sequence number of original cancel_broadcast_sm PDU.
     * @return the composed byte values.
     */
    byte[] cancelBroadcastSmResp(int sequenceNumber);

    /**
     * Compose query broadcast short message (query_broadcast_sm) PDU.
     *
     * @param sequenceNumber The sequence number of original broadcast_sm PDU.
     * @param messageId The message id returned in the broadcast_sm_resp.
     * @param sourceAddrTon Type of Number for source address.
     * @param sourceAddrNpi Numbering Plan Indicator for source address.
     * @param sourceAddr Address of SME which originated this message.
     * @param optionalParameters Optional TLVs, user_message_reference.
     * @return the composed byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] queryBroadcastSm(int sequenceNumber,
            String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            OptionalParameter... optionalParameters) throws PDUStringException;
    /**
     * Compose query broadcast short message response (query_broadcast_sm_resp) PDU.
     *
     * @param sequenceNumber The sequence number of original broadcast_sm PDU.
     * @param messageId The message id returned in the broadcast_sm_resp.
     * @param optionalParameters Optional TLVs, message_state, broadcast_area_identifier, broadcast_area_success,
     *                           broadcast_end_time, user_message_reference.
     * @return the composed byte values.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    byte[] queryBroadcastSmResp(int sequenceNumber,
            String messageId,
            OptionalParameter... optionalParameters) throws PDUStringException;

}
