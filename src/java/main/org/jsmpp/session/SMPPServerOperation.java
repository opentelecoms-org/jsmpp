package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 * 
 */
public interface SMPPServerOperation extends SMPPOperation {

    void deliverSm(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    void alertNotification();

    void submitSmResp(MessageId messageId, int sequenceNumber)
            throws PDUStringException, IOException;

    void submitMultiResp();

    void querySmResp(String messageId, String finalDate,
            MessageState messageState, byte errorCode, int sequenceNumber)
            throws PDUStringException, IOException;

    void replaceSmResp(int sequenceNumber) throws IOException;
}
