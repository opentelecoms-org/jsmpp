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
import org.jsmpp.PDUSender;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.Connection;

/**
 * @author uudashr
 * 
 */
public class DefaultSMPPClientOperation extends AbstractSMPPOperation implements
        SMPPClientOperation {

    public DefaultSMPPClientOperation(Connection connection, PDUSender pduSender) {
        super(connection, pduSender);
    }

    public BindResult bind(BindType bindType, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange, long timeout) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {

        BindCommandTask task = new BindCommandTask(pduSender(), bindType,
                systemId, password, systemType, interfaceVersion, addrTon,
                addrNpi, addressRange);

        BindResp resp = (BindResp)executeSendCommand(task, timeout);
        return new BindResult(resp.getSystemId(), resp.getOptionalParameters());
    }

    public String submitSm(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {

        SubmitSmCommandTask submitSmTask = new SubmitSmCommandTask(
                pduSender(), serviceType, sourceAddrTon, sourceAddrNpi,
                sourceAddr, destAddrTon, destAddrNpi, destinationAddr,
                esmClass, protocolId, priorityFlag, scheduleDeliveryTime,
                validityPeriod, registeredDelivery, replaceIfPresentFlag,
                dataCoding, smDefaultMsgId, shortMessage, optionalParameters);

        SubmitSmResp resp = (SubmitSmResp)executeSendCommand(submitSmTask,
                getTransactionTimer());
        return resp.getMessageId();
    }

    public SubmitMultiResult submitMulti(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, Address[] destinationAddresses,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter[] optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {

        SubmitMultiCommandTask task = new SubmitMultiCommandTask(pduSender(),
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destinationAddresses, esmClass, protocolId, priorityFlag,
                scheduleDeliveryTime, validityPeriod, registeredDelivery,
                replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
                optionalParameters);

        SubmitMultiResp resp = (SubmitMultiResp)executeSendCommand(task,
                getTransactionTimer());

        return new SubmitMultiResult(resp.getMessageId(), resp
                .getUnsuccessSmes());
    }

    public QuerySmResult querySm(String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {

        QuerySmCommandTask task = new QuerySmCommandTask(pduSender(),
                messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);

        QuerySmResp resp = (QuerySmResp)executeSendCommand(task,
                getTransactionTimer());

        if (resp.getMessageId().equals(messageId)) {
            return new QuerySmResult(resp.getFinalDate(), resp
                    .getMessageState(), resp.getErrorCode());
        } else {
            // message id requested not same as the returned
            throw new InvalidResponseException(
                    "Requested message_id doesn't match with the result");
        }
    }

    public void cancelSm(String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddress)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        
        CancelSmCommandTask task = new CancelSmCommandTask(pduSender(),
                serviceType, messageId, sourceAddrTon, sourceAddrNpi,
                sourceAddr, destAddrTon, destAddrNpi, destinationAddress);

        executeSendCommand(task, getTransactionTimer());
    }

    public void replaceSm(String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
        
        ReplaceSmCommandTask replaceSmTask = new ReplaceSmCommandTask(
                pduSender(), messageId, sourceAddrTon, sourceAddrNpi,
                sourceAddr, scheduleDeliveryTime, validityPeriod,
                registeredDelivery, smDefaultMsgId, shortMessage);

        executeSendCommand(replaceSmTask, getTransactionTimer());
    }

    @Override
    public void deliverSmResp(int sequenceNumber, String messageId) throws IOException {
        pduSender().sendDeliverSmResp(connection().getOutputStream(),
                0, sequenceNumber, messageId);

    }

}
