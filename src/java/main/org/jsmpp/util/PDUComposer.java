package org.jsmpp.util;

import org.jsmpp.PDUStringException;
import org.jsmpp.bean.OptionalParameter;

/**
 * This is utility to compose the PDU from parameter values to byte.
 * 
 * @author uudashr
 *
 */
public interface PDUComposer {

    public byte[] composeHeader(int commandId, int commandStatus,
            int sequenceNumber);

    // GENERAL BIND OPERATION
    public byte[] bind(int commandId, int sequenceNumber, String systemId,
            String password, String systemType, byte interfaceVersion,
            byte addrTon, byte addrNpi, String addressRange)
            throws PDUStringException;

    public byte[] bindResp(int commandId, int sequenceNumber, String systemId)
            throws PDUStringException;

    public byte[] bindResp(int commandId, int sequenceNumber, String systemId,
            byte scInterfaceVersion) throws PDUStringException;
    
    public byte[] unbind(int sequenceNumber);

    public byte[] unbindResp(int commandStatus, int sequenceNumber);

    public byte[] outbind(int sequenceNumber, String systemId, String password)
            throws PDUStringException;

    // ENQUIRE_LINK OPERATION
    public byte[] enquireLink(int sequenceNumber);

    public byte[] enquireLinkResp(int sequenceNumber);

    // GENEICK_NACK OPERATION
    public byte[] genericNack(int commandStatus, int sequenceNumber);

    // SUBMIT_SM OPERATION
    /**
     * Submit short message (submit_sm).
     * 
     * @param sequenceNumber
     * @param serviceType
     * @param sourceAddrTon
     * @param sourceAddrNpi
     * @param sourceAddr
     * @param destAddrTon
     * @param destAddrNpi
     * @param destinationAddr
     * @param esmClass
     * @param protocolId
     * @param priorityFlag
     * @param scheduleDeliveryTime
     * @param validityPeriod
     * @param registeredDelivery
     * @param replaceIfPresent
     * @param dataCoding
     * @param smDefaultMsgId
     * @param shortMessage
     * @param optionalParameters
     * @return the composed byte values.
     * @throws PDUStringException
     */
    public byte[] submitSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            byte registeredDelivery, byte replaceIfPresent, byte dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Submit short message response (submit_sm_resp).
     * 
     * @param sequenceNumber
     * @param messageId
     * @return the composed byte values.
     * @throws PDUStringException
     */
    public byte[] submitSmResp(int sequenceNumber, String messageId)
            throws PDUStringException;

    // QUERY_SM OPERATION
    public byte[] querySm(int sequenceNumber, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr)
            throws PDUStringException;

    public byte[] querySmResp(int sequenceNumber, String messageId,
            String finalDate, byte messageState, byte errorCode)
            throws PDUStringException;

    // DELIVER_SM OPERATION
    public byte[] deliverSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            byte registeredDelivery, byte dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException;

    public byte[] deliverSmResp(int sequenceNumber);
    
    
    /**
     * Compose data short message (data_sm) PDU.
     * 
     * @param sequenceNumber
     * @param serviceType
     * @param sourceAddrTon
     * @param sourceAddrNpi
     * @param sourceAddr
     * @param destAddrTon
     * @param destAddrNpi
     * @param destinationAddr
     * @param esmClass
     * @param registeredDelivery
     * @param dataCoding
     * @param optionalParameters
     * @return
     * @throws PDUStringException
     */
    public byte[] dataSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte registeredDelivery, byte dataCoding,
            OptionalParameter... optionalParameters) throws PDUStringException;

    /**
     * Compose data short message response (submit_sm_resp) PDU.
     * 
     * @param sequenceNumber is the sequence number.
     * @param messageId is the the message identifier.
     * @param optionalParameters is the optional parameter(s).
     * @return the composed byte values.
     * @throws PDUStringException
     */
    public byte[] dataSmResp(int sequenceNumber, String messageId,
            OptionalParameter... optionalParameters)
            throws PDUStringException;
}