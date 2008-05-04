package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;

/**
 * @author uudashr
 * 
 */
public interface SMPPClientOperation extends SMPPOperation {
    BindResult bind(BindType bindType, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange, long timeout) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    String submitSm(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    SubmitMultiResult submitMulti(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, Address[] destinationAddresses,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter[] optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    QuerySmResult querySm(String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
            throws PDUStringException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;

    void cancelSm(String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddress)
            throws PDUStringException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException;

    void replaceSm(String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;

    void deliverSmResp(int sequenceNumber) throws IOException;
}
