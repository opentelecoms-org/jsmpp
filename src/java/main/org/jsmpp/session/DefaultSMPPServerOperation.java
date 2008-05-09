package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 * 
 */
public class DefaultSMPPServerOperation extends AbstractSMPPOperation implements
        SMPPServerOperation {

    public DefaultSMPPServerOperation(Connection connection, PDUSender pduSender) {
        super(connection, pduSender);
    }

    public void deliverSm(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {

        DeliverSmCommandTask task = new DeliverSmCommandTask(pduSender(),
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId,
                protocoId, registeredDelivery, dataCoding, shortMessage,
                optionalParameters);

        executeSendCommand(task, getTransactionTimer());
    }

    public void alertNotification(int sequenceNumber,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber esmeAddrTon,
            NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
        pduSender().sendAlertNotification(connection().getOutputStream(),
                sequenceNumber, sourceAddrTon.value(), sourceAddrNpi.value(),
                sourceAddr, esmeAddrTon.value(), esmeAddrNpi.value(), esmeAddr,
                optionalParameters);
    }

    public void querySmResp(String messageId, String finalDate,
            MessageState messageState, byte errorCode, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender().sendQuerySmResp(connection().getOutputStream(),
                sequenceNumber, messageId, finalDate, messageState, errorCode);
    }

    public void replaceSmResp(int sequenceNumber) throws IOException {
        pduSender().sendReplaceSmResp(connection().getOutputStream(),
                sequenceNumber);
    }

    public void submitMultiResp(int sequenceNumber, String messageId,
            UnsuccessDelivery... unsuccessDeliveries)
            throws PDUStringException, IOException {
        pduSender().sendSubmitMultiResp(connection().getOutputStream(), 
                sequenceNumber, messageId, unsuccessDeliveries);
    }

    public void submitSmResp(MessageId messageId, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender().sendSubmitSmResp(connection().getOutputStream(),
                sequenceNumber, messageId.getValue());
    }
}
