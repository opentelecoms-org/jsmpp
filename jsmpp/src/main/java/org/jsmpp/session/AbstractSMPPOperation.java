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
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSmResp;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.util.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public abstract class AbstractSMPPOperation implements SMPPOperation {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSMPPOperation.class);

    private final Sequence sequence = new Sequence(1);
    private final PDUSender pduSender;
    private final Connection connection;
    private long transactionTimer = 2000;

    public AbstractSMPPOperation(Connection connection, PDUSender pduSender) {
        this.connection = connection;
        this.pduSender = pduSender;
    }

    protected PDUSender pduSender() {
        return pduSender;
    }

    protected Connection connection() {
        return connection;
    }

    public void setTransactionTimer(long transactionTimer) {
        this.transactionTimer = transactionTimer;
    }

    public long getTransactionTimer() {
        return transactionTimer;
    }

    /**
     * Execute send command command task.
     *
     * @param task is the task.
     * @param timeout is the timeout in millisecond.
     * @return the command response.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if the response has reach it timeout.
     * @throws InvalidResponseException if invalid response found.
     * @throws NegativeResponseException if the negative response found.
     * @throws IOException if there is an IO error found.
     */
    protected Command executeSendCommand(SendCommandTask task, long timeout)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {

        int seqNum = sequence.nextValue();
        PendingResponse<Command> pendingResp = new PendingResponse<Command>(timeout);
        try {
            task.executeTask(connection().getOutputStream(), seqNum);
        } catch (IOException e) {
            logger.error("Failed sending {} command", task.getCommandName(), e);
            throw e;
        }

        try {
            pendingResp.waitDone();
            logger.debug("{} response received", task.getCommandName() );
        } catch (ResponseTimeoutException e) {
            logger.debug("Response timeout for {} with sequence_number {}", task.getCommandName(), seqNum);
            throw e;
        }

        Command resp = pendingResp.getResponse();
        validateResponse(resp);
        return resp;

    }

    /**
     * Validate the response, the command_status should be 0 otherwise will
     * throw {@link NegativeResponseException}.
     *
     * @param response is the response.
     * @throws NegativeResponseException if the command_status value is not zero.
     */
    private static void validateResponse(Command response) throws NegativeResponseException {
        if (response.getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            throw new NegativeResponseException(response.getCommandStatus());
        }
    }

    public void unbind() throws ResponseTimeoutException, InvalidResponseException, IOException {
        UnbindCommandTask task = new UnbindCommandTask(pduSender);

        try {
            executeSendCommand(task, transactionTimer);
        } catch (PDUException e) {
            // exception should be never caught since we didn't send any string parameter.
            logger.warn("PDU String should be always valid", e);
        } catch (NegativeResponseException e) {
            // ignore the negative response
            logger.warn("Receive non-ok command_status ({}) for unbind_resp", e.getCommandStatus(), e);
        }
    }

    public void unbindResp(int sequenceNumber) throws IOException {
        pduSender.sendUnbindResp(connection().getOutputStream(), SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    public DataSmResult dataSm(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {

        DataSmCommandTask task = new DataSmCommandTask(pduSender,
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destAddrTon, destAddrNpi, destinationAddr, esmClass,
                registeredDelivery, dataCoding, optionalParameters);

        DataSmResp resp = (DataSmResp)executeSendCommand(task, getTransactionTimer());

        return new DataSmResult(resp.getMessageId(), resp.getOptionalParameters());
    }

    public void dataSmResp(int sequenceNumber, String messageId,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException {
        pduSender.sendDataSmResp(connection().getOutputStream(), sequenceNumber, messageId, optionalParameters);
    }

    public void enquireLink() throws ResponseTimeoutException, InvalidResponseException, IOException {
        EnquireLinkCommandTask task = new EnquireLinkCommandTask(pduSender);
        try {
            executeSendCommand(task, getTransactionTimer());
        } catch (PDUException e) {
            // should never happen, since it doesn't have any String parameter.
            logger.warn("PDU String should be always valid", e);
        } catch (NegativeResponseException e) {
            // the command_status of the response should be always 0
            logger.warn("command_status of response should be always 0", e);
        }
    }

    public void enquireLinkResp(int sequenceNumber) throws IOException {
        pduSender.sendEnquireLinkResp(connection().getOutputStream(), sequenceNumber);
    }

    public void genericNack(int commandStatus, int sequenceNumber) throws IOException {
        pduSender.sendGenericNack(connection().getOutputStream(), commandStatus, sequenceNumber);
    }

}
