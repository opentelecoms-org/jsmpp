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
package org.jsmpp.examples.gateway;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.SMPPSession;

/**
 * This is a gateway to submit short message.
 * 
 * @author uudashr
 */
public interface Gateway {

    /**
     * Submit the short message. It has the same parameter as
     * {@link SMPPSession#submitShortMessage(String, TypeOfNumber, NumberingPlanIndicator, String, TypeOfNumber, NumberingPlanIndicator, String, ESMClass, byte, byte, String, String, RegisteredDelivery, byte, DataCoding, byte, byte[], OptionalParameter...)}.
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
     * @return the message_id to identified the submitted short message for
     *         later use (delivery receipt, QUERY_SM, CANCEL_SM, REPLACE_SM).
     * @throws PDUException if there is invalid PDU parameter found..
     * @throws ResponseTimeoutException if timeout has been reach.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    String submitShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
            DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;
}
