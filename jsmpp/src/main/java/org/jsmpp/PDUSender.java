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
package org.jsmpp;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DestinationAddress;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;

/**
 * This class provides a way to send SMPP Commands over an {@link OutputStream}.
 * PDUs will be created and returned as bytes.
 *
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public interface PDUSender {

    /**
     * Send only the PDU header.
     *
     * @param os the {@link OutputStream}
     * @param commandId the command_id
     * @param commandStatus the command_status
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendHeader(OutputStream os, int commandId, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send bind command.
     *
     * @param os the {@link OutputStream}
     * @param bindType the bind type that determine the command_id
     * @param sequenceNumber the sequence_number
     * @param systemId the system_id
     * @param password the password
     * @param systemType the system_type
     * @param interfaceVersion the interface_version
     * @param addrTon the addr_ton
     * @param addrNpi the addr_npi
     * @param addressRange the address_range
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found.
     * @throws IOException if an input or output error occurred
     */
    byte[] sendBind(OutputStream os, BindType bindType, int sequenceNumber,
            String systemId, String password, String systemType,
            InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
            NumberingPlanIndicator addrNpi, String addressRange)
            throws PDUStringException, IOException;

    /**
     * Send bind response command.
     *
     * @param os the {@link OutputStream}
     * @param commandId the command_id
     * @param sequenceNumber the sequence_number
     * @param systemId the system_id
     * @param interfaceVersion the {@link InterfaceVersion} send in the bind response, either 3.3, 3.4 or 5.0
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendBindResp(OutputStream os, int commandId, int sequenceNumber,
            String systemId, InterfaceVersion interfaceVersion) throws PDUStringException, IOException;

    /**
     * Send outbind command.
     *
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param systemId the system_id
     * @param password the password
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendOutbind(OutputStream os,int sequenceNumber,
                       String systemId, String password)
        throws PDUStringException, IOException;

    /**
     * Send unbind command.
     *
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendUnbind(OutputStream os, int sequenceNumber) throws IOException;

    /**
     * Send generic non-acknowledge command.
     *
     * @param os the {@link OutputStream}
     * @param commandStatus the command_status
     * @param sequenceNumber the sequence_number
     * @return the composed bytes.
     * @throws IOException if an input or output error occurred
     */
    byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send unbind response command.
     *
     * @param os the {@link OutputStream}
     * @param commandStatus the command_status
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred

     */
    byte[] sendUnbindResp(OutputStream os, int commandStatus, int sequenceNumber)
            throws IOException;

    /**
     * Send enquire link command.
     *
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendEnquireLink(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send enquire link response command.
     *
     * @param os the {@link OutputStream}.
     * @param sequenceNumber the sequenceNumber
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send submit short message command.
     * 
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param serviceType the service_type
     * @param sourceAddrTon the source_addr_ton
     * @param sourceAddrNpi  the source_addr_npi
     * @param sourceAddr the source_addr
     * @param destAddrTon the dest_addr_ton
     * @param destAddrNpi the dest_addr_npi
     * @param destinationAddr the destination_addr
     * @param esmClass the esm_class
     * @param protocolId the protocol_id
     * @param priorityFlag the priority_flag
     * @param scheduleDeliveryTime the schedule_delivery_time
     * @param validityPeriod the validity_period.
     * @param registeredDelivery the registered_delivery
     * @param replaceIfPresentFlag the replace_if_present_flag
     * @param dataCoding the data_coding
     * @param smDefaultMsgId the sm_default_msg_id
     * @param shortMessage the short_message
     * @param optionalParameters the optional parameters
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendSubmitSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;

    /**
     * Send submit short message response command.
     *
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id
     * @param optionalParameters the optional parameters
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters) throws PDUStringException, IOException;

    /**
     * Send query short message command.
     * 
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id
     * @param sourceAddrTon the source_addr_ton
     * @param sourceAddrNpi the source_addr_npi
     * @param sourceAddr the source_addr
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendQuerySm(OutputStream os, int sequenceNumber, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr) throws PDUStringException, IOException;

    /**
     * Send query short message response command.
     * 
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id
     * @param finalDate the final_date
     * @param messageState the message_state
     * @param errorCode the error_code
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendQuerySmResp(OutputStream os, int sequenceNumber,
            String messageId, String finalDate, MessageState messageState,
            byte errorCode) throws PDUStringException, IOException;

    /**
     * Send the deliver short message command.
     *
     * @param os the {@link OutputStream}.
     * @param sequenceNumber the sequence_number.
     * @param serviceType the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_addr.
     * @param esmClass is the esm_class.
     * @param protocolId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param registeredDelivery is the registered_delivery.
     * @param dataCoding is the data_coding.
     * @param shortMessage is the short_message.
     * @param optionalParameters is the optional parameters.
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;

    /**
     * Send the deliver short message response.
     *
     * @param os the {@link OutputStream}
     * @param commandStatus the command_status
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id.
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendDeliverSmResp(OutputStream os, int commandStatus, int sequenceNumber, String messageId)
            throws IOException;

    /**
     * Send the data short message command.
     * 
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param serviceType the service_type
     * @param sourceAddrTon the source_addr_ton
     * @param sourceAddrNpi the source_addr_npi
     * @param sourceAddr the source_addr
     * @param destAddrTon the dest_addr_ton
     * @param destAddrNpi the dest_addr_npi
     * @param destinationAddr the destination_addr
     * @param esmClass the esm_class.
     * @param registeredDelivery the registered_delivery
     * @param dataCoding the data_coding
     * @param optionalParameters the optional parameters
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if an input or output error occurred
     */
    byte[] sendDataSm(OutputStream os, int sequenceNumber, String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException;

    /**
     * Send data short message response command.
     * 
     * @param os is the {@link OutputStream}
     * @param sequenceNumber is the sequence_number
     * @param messageId the message_id
     * @param optionalParameters the optional parameters
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendDataSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException;

    /**
     * Send cancel short message command.
     * 
     * @param os the {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param serviceType the service_type
     * @param messageId the message_id
     * @param sourceAddrTon the source_addr_ton
     * @param sourceAddrNpi the source_addr_npi
     * @param sourceAddr the source_addr
     * @param destAddrTon the dest_addr_ton
     * @param destAddrNpi the dest_addr_npi
     * @param destinationAddr is the destination_addr
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendCancelSm(OutputStream os, int sequenceNumber,
            String serviceType, String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr) throws PDUStringException, IOException;

    /**
     * Send cancel short message response command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendCancelSmResp(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send replace short message command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id
     * @param sourceAddrTon the source_addr_ton
     * @param sourceAddrNpi the source_addr_npi
     * @param sourceAddr the source_addr
     * @param scheduleDeliveryTime The new scheduled delivery time for the short message.
     *                             Set to null to preserve the original scheduled delivery time.
     * @param validityPeriod The new expiry time for the short message.
     *                       Set to null to preserve the original validity period setting.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt, user/manual or delivery ACK or intermediate notification is required.
     * @param smDefaultMsgId Indicates the short message to send from a list of pre-defined (‘canned’) short messages stored on the MC.
     *                       If not using a MC canned message, set to null.
     * @param shortMessage Up to 255 octets of short message user data.
     *                     Applications which need to send messages longer than 255 octets should use the message_payload TLV.
     *                     In this case the sm_length field should be set to zero
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendReplaceSm(OutputStream os, int sequenceNumber, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage)
            throws PDUStringException, IOException;

    /**
     * Send replace short message response command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @return the composed bytes
     * @throws IOException if an input or output error occurred
     */
    byte[] sendReplaceSmResp(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send submit multi command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber Set to an unique sequence number. The associated submit_multi_resp PDU will echo this sequence number.
     * @param serviceType The service_type parameter can be used to indicate the SMS Application service associated with the message.
     *                    Specifying the service_type allows the ESME to avail of enhaned messaging services
     *                    such as "replace by service_type" or control the teleservice used on the air interface.
     *                    Set to null for default MC settings
     * @param sourceAddrTon Type of Number for source address. If not known, set to UNKNOWN.
     * @param sourceAddrNpi Numbering Plan Indicator for source address. If not known, set to UNKNOWN.
     * @param sourceAddr Address of SME which originated this message
     * @param destinationAddresses list (max 255) of destination addresses
     * @param esmClass Indicates Message Mode and Message Type
     * @param protocolId Protocol Identifier (Network specific)
     * @param priorityFlag Designates the priority level of the message
     * @param scheduleDeliveryTime The short message is to be scheduled by the MC for delivery.
     *                             Set to null for immediate message delivery.
     * @param validityPeriod The validity period of this message.
     *                       Set to null to request the MC default validity period.
     * @param registeredDelivery Indicator to signify if a MC delivery receipt or an SME acknowledgement is required.
     * @param replaceIfPresentFlag Flag indicating if submitted message should replace an existing message.
     * @param dataCoding Defines the encoding scheme of the short message user data.
     * @param smDefaultMsgId Indicates the short message to send from a list of pre-defined (‘canned’) short messages stored on the MC. If not using a MC canned message, set to 0.
     * @param shortMessage Up to 255 octets of short message user data.
     *                     The exact physical limit for short_message size may vary according to the underlying network.
     *                     Applications which need to send messages longer than 255 octets should use the message_payload TLV.
     * @param optionalParameters the optional parameters
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws InvalidNumberOfDestinationsException if an invalid number of destinations in destinationAddresses are existing
     * @throws IOException if an input or output error occurred
     */
    byte[] sendSubmitMulti(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            DestinationAddress[] destinationAddresses, ESMClass esmClass,
            byte protocolId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            InvalidNumberOfDestinationsException, IOException;

    /**
     * Send submit multi response command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param messageId the message_id
     * @param unsuccessDeliveries list of unsuccessful deliveries
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendSubmitMultiResp(OutputStream os, int sequenceNumber,
            String messageId, UnsuccessDelivery... unsuccessDeliveries)
            throws PDUStringException, IOException;

    /**
     * Send alert notification command.
     *
     * @param os {@link OutputStream}
     * @param sequenceNumber the sequence_number
     * @param sourceAddrTon Type of Number for alert SME
     * @param sourceAddrNpi Numbering Plan Indicator for alert SME
     * @param sourceAddr Address of alert SME
     * @param esmeAddrTon Type of Number for ESME address which requested the alert
     * @param esmeAddrNpi Numbering Plan Indicator for ESME address which requested the alert
     * @param esmeAddr Address for ESME which requested the alert
     * @param optionalParameters the optional TLV's
     * @return the composed bytes
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws IOException if an input or output error occurred
     */
    byte[] sendAlertNotification(OutputStream os, int sequenceNumber,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber esmeAddrTon, NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;

    byte[] sendBroadcastSm(OutputStream os, int sequenceNumber, String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            String messageId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException;

    byte[] sendBroadcastSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
        throws PDUStringException, IOException;

    byte[] sendCancelBroadcastSm(OutputStream os, int sequenceNumber, String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException;

    byte[] sendCancelBroadcastSmResp(OutputStream os, int sequenceNumber)
        throws IOException;

    byte[] sendQueryBroadcastSm(OutputStream os, int sequenceNumber, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException;

    byte[] sendQueryBroadcastSmResp(OutputStream os, int sequenceNumber, String messageId,
                                OptionalParameter... optionalParameters)
        throws PDUStringException, IOException;

}