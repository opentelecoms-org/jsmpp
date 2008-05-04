package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;

/**
 * @author uudashr
 *
 */
public interface SMPPOperation {
    
    void unbind() throws ResponseTimeoutException, InvalidResponseException, IOException;
    
    void unbindResp(int sequenceNumber) throws IOException;
    
    DataSmResult dataSm(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, OptionalParameter... optionalParameters)
            throws PDUStringException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;
    
    void dataSmResp(int sequenceNumber, String messageId,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException;
    
    void enquireLink() throws ResponseTimeoutException,
            InvalidResponseException, IOException;
        
    void enquireLinkResp(int sequenceNumber) throws IOException;
    
    void genericNack(int commandStatus, int sequenceNumber) throws IOException;
}
