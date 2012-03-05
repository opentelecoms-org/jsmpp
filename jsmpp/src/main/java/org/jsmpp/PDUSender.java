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
 * This class provide way to send SMPP Command over an {@link OutputStream}.
 * PDU will be created and returned as bytes.
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
     * @param os is the {@link OutputStream}
     * @param commandId is the SMPP command_id.
     * @param commandStatus is the SMPP command_status.
     * @param sequenceNumber is the SMPP sequence_number.
     * @return the composed bytes.
     * @throws IOException if an I/O error occur.
     */
    byte[] sendHeader(OutputStream os, int commandId, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send bind command.
     * 
     * @param os is the {@link OutputStream}
     * @param bindType is the bind type that determine the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @param password is the password.
     * @param systemType is the system_type.
     * @param interfaceVersion is the interface_version.
     * @param addrTon is the addr_ton.
     * @param addrNpi is the addr_npi.
     * @param addressRange is the address_range.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint found.
     * @throws IOException if an I/O error occur.
     */
    byte[] sendBind(OutputStream os, BindType bindType, int sequenceNumber,
            String systemId, String password, String systemType,
            InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
            NumberingPlanIndicator addrNpi, String addressRange)
            throws PDUStringException, IOException;

    /**
     * Send bind response command.
     * 
     * @param os is the {@link OutputStream}.
     * @param commandId is the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @return the composed bytes.
     * @throws PDUStringException
     * @throws IOException if an IO error occur.
     */
    byte[] sendBindResp(OutputStream os, int commandId, int sequenceNumber,
            String systemId, InterfaceVersion interfaceVersion) throws PDUStringException, IOException;

    /**
     * Send unbind command.
     * 
     * @param os is the {@link OutputStream} .
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     * @throws IOException if an IO error occur.
     */
    byte[] sendUnbind(OutputStream os, int sequenceNumber) throws IOException;

    /**
     * Send generic non-acknowledge command.
     * 
     * @param os is the {@link OutputStream}
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     * @throws IOException if an IO error occur.
     */
    byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send unbind response command.
     * 
     * @param os is the {@link OutputStream}.
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     * @throws IOException if an IO error occur.
     */
    byte[] sendUnbindResp(OutputStream os, int commandStatus, int sequenceNumber)
            throws IOException;

    /**
     * Send enquire link command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     * @throws IOException if an IO error occur.
     */
    byte[] sendEnquireLink(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send enquire link response command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequenceNumber.
     * @return the composed bytes.
     * @throws IOException if an IO error occur.
     */
    byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException;

    /**
     * Send submit short message command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @param serviceType is the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_addr.
     * @param esmClass is the esm_class.
     * @param protocolId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param scheduleDeliveryTime is the schedule_delivery_time
     * @param validityPeriod is the validity_period.
     * @param registeredDelivery is the registered_delivery.
     * @param replaceIfPresentFlag is the replace_if_present_flag.
     * @param dataCoding is the data_coding.
     * @param smDefaultMsgId is the sm_default_msg_id.
     * @param shortMessage is the short_message.
     * @param optionalParameters is the optional parameters.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if there is an IO error occur.
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
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId) throws PDUStringException, IOException;

    /**
     * Send query short message command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid constraint found.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendQuerySm(OutputStream os, int sequenceNumber, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr) throws PDUStringException, IOException;

    /**
     * Send query short message response command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @param finalDate is the final_date.
     * @param messageState is the message_state.
     * @param errorCode is the error_code.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid constraint found.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendQuerySmResp(OutputStream os, int sequenceNumber,
            String messageId, String finalDate, MessageState messageState,
            byte errorCode) throws PDUStringException, IOException;

    /**
     * Send the deliver short message command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber the sequence_number.
     * @param serviceType the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_addr.
     * @param esmClass is the esm_class.
     * @param protocoId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param registeredDelivery is the registered_delivery.
     * @param dataCoding is the data_coding.
     * @param shortMessage is the short_message.
     * @param optionalParameters is the optional parameters.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;

    /**
     * Send the deliver short message response.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendDeliverSmResp(OutputStream os, int commandStatus, int sequenceNumber)
            throws IOException;

    /**
     * Send the data short message command.
     * 
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber the sequence_number.
     * @param serviceType the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_addr.
     * @param esmClass is the esm_class.
     * @param registeredDelivery is the registered_delivery.
     * @param dataCoding is the data_coding.
     * @param optionalParameters is the optional parameters.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if there is an IO error occur.
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
     * @param os is the {@link OutputStream}.
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @param optionalParameters is the optional parameters.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     * @throws IOException if there is an IO error occur.
     */
    byte[] sendDataSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException;

    /**
     * Send cancel short message command.
     * 
     * @param os
     * @param sequenceNumber
     * @param serviceType
     * @param messageId
     * @param sourceAddrTon
     * @param sourceAddrNpi
     * @param sourceAddr
     * @param destAddrTon
     * @param destAddrNpi
     * @param destinationAddr
     * @return
     * @throws PDUStringException
     * @throws IOException
     */
    byte[] sendCancelSm(OutputStream os, int sequenceNumber,
            String serviceType, String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr) throws PDUStringException, IOException;

    /**
     * Send cancel short message response command.
     * 
     * @param os
     * @param sequenceNumber
     * @return
     * @throws IOException
     */
    byte[] sendCancelSmResp(OutputStream os, int sequenceNumber)
            throws IOException;

    byte[] sendReplaceSm(OutputStream os, int sequenceNumber, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage)
            throws PDUStringException, IOException;

    byte[] sendReplaceSmResp(OutputStream os, int sequenceNumber)
            throws IOException;

    byte[] sendSubmiMulti(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            DestinationAddress[] destinationAddresses, ESMClass esmClass,
            byte protocolId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            InvalidNumberOfDestinationsException, IOException;

    byte[] sendSubmitMultiResp(OutputStream os, int sequenceNumber,
            String messageId, UnsuccessDelivery... unsuccessDeliveries)
            throws PDUStringException, IOException;

    byte[] sendAlertNotification(OutputStream os, int sequenceNumber,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte esmeAddrTon, byte esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;
}