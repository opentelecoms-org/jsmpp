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
package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;

/**
 * This interface provides all operation that the client session can do. It
 * doesn't distinct the operation of specific session type (Transmitter,
 * Receiver) it's just like Transceiver. The distinction might should be
 * recognized in a different way, such as by user code when they do a binding or
 * by throwing exception when invoking illegal operation.
 * 
 * @author uudashr
 * 
 */
public interface ClientSession extends Session, AutoCloseable {
    
    /**
     * Submit a short message to specified destination address. This method will
     * blocks until response received or timeout reached. This method simplify
     * operations of sending SUBMIT_SM command and receiving the SUBMIT_SM_RESP.
     * 
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
     * @param scheduleDeliveryTime is the schedule_delivery_time.
     * @param validityPeriod is the validity_period.
     * @param registeredDelivery is the registered_delivery.
     * @param replaceIfPresentFlag is the replace_if_present_flag.
     * @param dataCoding is the data_coding.
     * @param smDefaultMsgId is the sm_default_msg_id.
     * @param shortMessage is the short_message.
     * @param optionalParameters is the optional parameters.
     * @return the SubmitSmResult with the message_id to identify the submitted short message for
     *         later use (delivery receipt, query_sm, cancel_sm, replace_sm) and optional parameters.
     * @throws PDUException if there is invalid PDU parameter found..
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    SubmitSmResult submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon,
                                      NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
                                      TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
                                      String destinationAddr, ESMClass esmClass, byte protocolId,
                                      byte priorityFlag, String scheduleDeliveryTime,
                                      String validityPeriod, RegisteredDelivery registeredDelivery,
                                      byte replaceIfPresentFlag, DataCoding dataCoding,
                                      byte smDefaultMsgId, byte[] shortMessage,
                                      OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    /**
     * Submit short message to multiple destination address. It's similar to
     * submit short message, but it sending to multiple address. This method
     * will blocks until response received or timeout reached. This method is
     * simplify operations of sending SUBMIT_MULTI and receiving
     * SUBMIT_MULTI_RESP.
     * 
     * @param serviceType is the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destinationAddresses is the destination addresses.
     * @param esmClass is the esm_class.
     * @param protocolId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param scheduleDeliveryTime is the schedule_delivery_time.
     * @param validityPeriod is the validity_period.
     * @param registeredDelivery is the registered_delivery.
     * @param replaceIfPresentFlag is the replace_if_present_flag.
     * @param dataCoding is the data_coding.
     * @param smDefaultMsgId is the sm_default_msg_id.
     * @param shortMessage is the short_message.
     * @param optionalParameters is the optional parameters.
     * @return the message_id and the un-success deliveries.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    SubmitMultiResult submitMultiple(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, Address[] destinationAddresses,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;
    
    /**
     * Query previous submitted short message based on it's message_id and
     * message_id. This method will blocks until response received or timeout
     * reached. This method is simplify operations of sending QUERY_SM and
     * receiving QUERY_SM_RESP.
     * 
     * @param messageId is the message_id.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @return the result of query short message.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    QuerySmResult queryShortMessage(String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr) throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;

    /**
     * Cancel the previous submitted short message. This method will blocks
     * until response received or timeout reached. This method is simplify
     * operations of sending CANCEL_SM and receiving CANCEL_SM_RESP.
     * 
     * @param serviceType is the service_type.
     * @param messageId is the message_id.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddress is destination_address.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    void cancelShortMessage(String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddress)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;

    /**
     * Replace the previous submitted short message. This method will blocks
     * until response received or timeout reached. This method is simplify
     * operations of sending REPLACE_SM and receiving REPLACE_SM_RESP.
     * 
     * @param messageId is the message_id.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param scheduleDeliveryTime is the schedule_delivery_time.
     * @param validityPeriod is the validity_period.
     * @param registeredDelivery is the registered_delivery.
     * @param smDefaultMsgId is the sm_default_msg_id.
     * @param shortMessage is the short_message.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    void replaceShortMessage(String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;
    
    /**
     * Open connection and bind immediately. The default
     * timeout is 1 minutes.
     * 
     * @param host is the SMSC host address.
     * @param port is the SMSC listen port.
     * @param bindType is the bind type.
     * @param systemId is the system id.
     * @param password is the password.
     * @param systemType is the system type.
     * @param addrTon is the address TON.
     * @param addrNpi is the address NPI.
     * @param addressRange is the address range.
     * @throws IOException if there is an IO error found.
     */
    void connectAndBind(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws IOException;
    
    /**
     * Open connection and bind immediately with specified timeout. The default
     * timeout is 1 minutes.
     * 
     * @param host the SMSC host address.
     * @param port the SMSC listen port.
     * @param bindType the bind type.
     * @param systemId the system id.
     * @param password the password.
     * @param systemType the system type.
     * @param addrTon the address TON.
     * @param addrNpi the address NPI.
     * @param addressRange the address range.
     * @param timeout the timeout in milliseconds.
     * @throws IOException if an input or output error occurred.
     */
    void connectAndBind(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange, long timeout) throws IOException;
    
    /**
     * Open connection and bind immediately.
     * 
     * @param host the SMSC host address.
     * @param port the SMSC listen port.
     * @param bindParam the bind parameters.
     * @return the SMSC system id.
     * @throws IOException if an input or output error occurred.
     */
    String connectAndBind(String host, int port,
            BindParameter bindParam) 
            throws IOException;
    
    /**
     * Open connection and bind immediately.
     * 
     * @param host the SMSC host address.
     * @param port the SMSC listen port.
     * @param bindParam the bind parameters.
     * @param timeout the timeout in milliseconds.
     * @return the SMSC system id.
     * @throws IOException if an input or output error occurred.
     */
    String connectAndBind(String host, int port,
            BindParameter bindParam, long timeout) 
            throws IOException;

    /**
     * Get the current message receiver listener that is currently registered for this smpp session.
     * @return The current message receiver listener
     */
    MessageReceiverListener getMessageReceiverListener();
    
    /**
     * Sets a message receiver listener for this smpp session.
     * @param messageReceiverListener is the new listener
     */
    void setMessageReceiverListener(
            MessageReceiverListener messageReceiverListener);
}
