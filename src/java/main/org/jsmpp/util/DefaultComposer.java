package org.jsmpp.util;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;

/**
 * Default implementation of {@link PDUComposer}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class DefaultComposer implements PDUComposer {
    public DefaultComposer() {
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#composeHeader(int, int, int)
     */
    public byte[] composeHeader(int commandId, int commandStatus,
            int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(commandId, commandStatus,
                sequenceNumber);
        return buf.getBytes();
    }

    // GENERAL BIND OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bind(int, int, java.lang.String, java.lang.String, java.lang.String, byte, byte, byte, java.lang.String)
     */
    public byte[] bind(int commandId, int sequenceNumber, String systemId,
            String password, String systemType, byte interfaceVersion,
            byte addrTon, byte addrNpi, String addressRange)
            throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);
        StringValidator.validateString(systemType, StringParameter.SYSTEM_TYPE);
        StringValidator.validateString(addressRange,
                StringParameter.ADDRESS_RANGE);

        PDUByteBuffer buf = new PDUByteBuffer(commandId, 0, sequenceNumber);

        buf.append(systemId);
        buf.append(password);
        buf.append(systemType);
        buf.append(interfaceVersion);
        buf.append(addrTon);
        buf.append(addrNpi);
        buf.append(addressRange);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bindResp(int, int, java.lang.String)
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId)
            throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        PDUByteBuffer buf = new PDUByteBuffer(commandId, 0, sequenceNumber);
        buf.append(systemId);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bindResp(int, int, java.lang.String, byte)
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId,
            byte scInterfaceVersion) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        PDUByteBuffer buf = new PDUByteBuffer(commandId, 0, sequenceNumber);
        buf.append(systemId);

        OptionalParameter optParam = new OptionalParameter.Byte(Tag.SC_INTERFACE_VERSION, scInterfaceVersion);
        buf.append(optParam.serialize());
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#unbind(int)
     */
    public byte[] unbind(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_UNBIND, 0,
                sequenceNumber);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#unbindResp(int, int)
     */
    public byte[] unbindResp(int commandStatus, int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_UNBIND_RESP,
                commandStatus, sequenceNumber);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#outbind(int, java.lang.String, java.lang.String)
     */
    public byte[] outbind(int sequenceNumber, String systemId, String password)
            throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_OUTBIND, 0,
                sequenceNumber);
        buf.append(systemId);
        buf.append(password);
        return buf.getBytes();
    }

    // ENQUIRE_LINK OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#enquireLink(int)
     */
    public byte[] enquireLink(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_ENQUIRE_LINK, 0,
                sequenceNumber);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#enquireLinkResp(int)
     */
    public byte[] enquireLinkResp(int sequenceNumber) {
        byte[] b = composeHeader(SMPPConstant.CID_ENQUIRE_LINK_RESP,
                SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        return b;
    }

    // GENEICK_NACK OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#genericNack(int, int)
     */
    public byte[] genericNack(int commandStatus, int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_GENERIC_NACK, commandStatus,
                sequenceNumber);
    }

    // SUBMIT_SM OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#submitSm(int, java.lang.String, byte, byte, java.lang.String, byte, byte, java.lang.String, byte, byte, byte, java.lang.String, java.lang.String, byte, byte, byte, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] submitSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            byte registeredDelivery, byte replaceIfPresent, byte dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException {

        StringValidator.validateString(serviceType,
                StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr,
                StringParameter.DESTINATION_ADDR);
        StringValidator.validateString(scheduleDeliveryTime,
                StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod,
                StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(shortMessage,
                StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM, 0,
                sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(destAddrTon);
        buf.append(destAddrNpi);
        buf.append(destinationAddr);
        buf.append(esmClass);
        buf.append(protocolId);
        buf.append(priorityFlag);
        buf.append(scheduleDeliveryTime);
        buf.append(validityPeriod);
        buf.append(registeredDelivery);
        buf.append(replaceIfPresent);
        buf.append(dataCoding);
        buf.append(smDefaultMsgId);
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        buf.appendAll(optionalParameters);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#submitSmResp(int, java.lang.String)
     */
    public byte[] submitSmResp(int sequenceNumber, String messageId)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM_RESP,
                0, sequenceNumber);
        buf.append(messageId);
        return buf.getBytes();
    }

    // QUERY_SM OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#querySm(int, java.lang.String, byte, byte, java.lang.String)
     */
    public byte[] querySm(int sequenceNumber, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM, 0,
                sequenceNumber);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        return buf.getBytes();
    }

    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#querySmResp(int, java.lang.String, java.lang.String, byte, byte)
     */
    public byte[] querySmResp(int sequenceNumber, String messageId,
            String finalDate, byte messageState, byte errorCode)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(finalDate, StringParameter.FINAL_DATE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM_RESP,
                0, sequenceNumber);
        buf.append(messageId);
        buf.append(finalDate);
        buf.append(messageState);
        buf.append(errorCode);
        return buf.getBytes();
    }

    // DELIVER_SM OPERATION
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#deliverSm(int, java.lang.String, byte, byte, java.lang.String, byte, byte, java.lang.String, byte, byte, byte, byte, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] deliverSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            byte registeredDelivery, byte dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException {

        StringValidator.validateString(serviceType,
                StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr,
                StringParameter.DESTINATION_ADDR);
        StringValidator.validateString(shortMessage,
                StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM, 0,
                sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(destAddrTon);
        buf.append(destAddrNpi);
        buf.append(destinationAddr);
        buf.append(esmClass);
        buf.append(protocolId);
        buf.append(priorityFlag);
        // buf.append(scheduleDeliveryTime);
        buf.append((String)null); // schedule delivery time
        // buf.append(validityPeriod);
        buf.append((String)null); // validity period
        buf.append(registeredDelivery);
        // buf.append(replaceIfPresent);
        buf.append((byte)0); // replace if present flag
        buf.append(dataCoding);
        // buf.append(smDefaultMsgId);
        buf.append((byte)0); // sm default msg id
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        buf.appendAll(optionalParameters);;
        return buf.getBytes();
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#deliverSmResp(int)
     */
    public byte[] deliverSmResp(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM_RESP,
                0, sequenceNumber);
        buf.append((String)null);
        return buf.getBytes();
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#dataSm(int, java.lang.String, byte, byte, java.lang.String, byte, byte, java.lang.String, byte, byte, byte, org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] dataSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte registeredDelivery, byte dataCoding,
            OptionalParameter... optionalParameters) throws PDUStringException {
        
        StringValidator.validateString(serviceType,
                StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr,
                StringParameter.DESTINATION_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DATA_SM, 0,
                sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(destAddrTon);
        buf.append(destAddrNpi);
        buf.append(destinationAddr);
        buf.append(esmClass);
        buf.append(registeredDelivery);
        buf.append(dataCoding);
        buf.appendAll(optionalParameters);
        return buf.getBytes();
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#dataSmResp(int, java.lang.String, org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] dataSmResp(int sequenceNumber, String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DATA_SM_RESP,
                0, sequenceNumber);
        buf.append(messageId);
        
        return buf.getBytes();
    }
}
