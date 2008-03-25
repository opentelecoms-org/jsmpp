package org.jsmpp.util;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.OptionalParameter;

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

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#composeHeader(int, int, int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#composeHeader(int, int, int)
     */
    public byte[] composeHeader(int commandId, int commandStatus, int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(commandId, commandStatus, sequenceNumber);
        return buf.getBytes();
    }

    // GENERAL BIND OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#bind(int, int, java.lang.String,
     *      java.lang.String, java.lang.String, byte, byte, byte,
     *      java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bind(int, int, java.lang.String, java.lang.String, java.lang.String, byte, byte, byte, java.lang.String)
     */
    public byte[] bind(int commandId, int sequenceNumber, String systemId, String password, String systemType, byte interfaceVersion, byte addrTon, byte addrNpi, String addressRange) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);
        StringValidator.validateString(systemType, StringParameter.SYSTEM_TYPE);
        StringValidator.validateString(addressRange, StringParameter.ADDRESS_RANGE);

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

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#bindResp(int, int,
     *      java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bindResp(int, int, java.lang.String)
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        PDUByteBuffer buf = new PDUByteBuffer(commandId, 0, sequenceNumber);
        buf.append(systemId);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#bindResp(int, int,
     *      java.lang.String, byte)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#bindResp(int, int, java.lang.String, byte)
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId, byte scInterfaceVersion) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        PDUByteBuffer buf = new PDUByteBuffer(commandId, 0, sequenceNumber);
        buf.append(systemId);

        // optional paramters
        // FIXME: buf.append(new OptionalParameter.Byte(Tag.SC_interface_version, scInterfaceVersion));
        buf.append(SMPPConstant.TAG_SC_INTERFACE_VERSION);
        buf.append(0x0001);
        buf.append(scInterfaceVersion);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#unbind(int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#unbind(int)
     */
    public byte[] unbind(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_UNBIND, 0, sequenceNumber);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#unbindResp(int, int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#unbindResp(int, int)
     */
    public byte[] unbindResp(int commandStatus, int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_UNBIND_RESP, commandStatus, sequenceNumber);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#outbind(int, java.lang.String,
     *      java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#outbind(int, java.lang.String, java.lang.String)
     */
    public byte[] outbind(int sequenceNumber, String systemId, String password) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_OUTBIND, 0, sequenceNumber);
        buf.append(systemId);
        buf.append(password);
        return buf.getBytes();
    }

    // ENQUIRE_LINK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#enquireLink(int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#enquireLink(int)
     */
    public byte[] enquireLink(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_ENQUIRE_LINK, 0, sequenceNumber);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#enquireLinkResp(int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#enquireLinkResp(int)
     */
    public byte[] enquireLinkResp(int sequenceNumber) {
        byte[] b = composeHeader(SMPPConstant.CID_ENQUIRE_LINK_RESP, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        return b;
    }

    // GENEICK_NACK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#genericNack(int, int)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#genericNack(int, int)
     */
    public byte[] genericNack(int commandStatus, int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_GENERIC_NACK, commandStatus, sequenceNumber);
    }

    // SUBMIT_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#submitSm(int, java.lang.String,
     *      byte, byte, java.lang.String, byte, byte, java.lang.String, byte,
     *      byte, byte, java.lang.String, java.lang.String, byte, byte, byte,
     *      byte, byte[])
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#submitSm(int, java.lang.String, byte, byte, java.lang.String, byte, byte, java.lang.String, byte, byte, byte, java.lang.String, java.lang.String, byte, byte, byte, byte, byte[])
     */
    public byte[] submitSm(int sequenceNumber, String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr, byte destAddrTon, byte destAddrNpi, String destinationAddr, byte esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, byte registeredDelivery, byte replaceIfPresent, byte dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException {

        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);
        StringValidator.validateString(scheduleDeliveryTime, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM, 0, sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(destAddrTon);
        buf.append(destAddrNpi);
        buf.append(destinationAddr);
        buf.append(esmClass);
        buf.append(protocoId);
        buf.append(priorityFlag);
        buf.append(scheduleDeliveryTime);
        buf.append(validityPeriod);
        buf.append(registeredDelivery);
        buf.append(replaceIfPresent);
        buf.append(dataCoding);
        buf.append(smDefaultMsgId);
        buf.append((byte) shortMessage.length);
        buf.append(shortMessage);
        appendOptional(buf, params);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#submitSmResp(int, java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#submitSmResp(int, java.lang.String)
     */
    public byte[] submitSmResp(int sequenceNumber, String messageId) throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM_RESP, 0, sequenceNumber);
        buf.append(messageId);
        return buf.getBytes();
    }

    // QUERY_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#querySm(int, java.lang.String,
     *      byte, byte, java.lang.String)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#querySm(int, java.lang.String, byte, byte, java.lang.String)
     */
    public byte[] querySm(int sequenceNumber, String messageId, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr) throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM, 0, sequenceNumber);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        return buf.getBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#querySmResp(int, java.lang.String,
     *      java.lang.String, byte, byte)
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#querySmResp(int, java.lang.String, java.lang.String, byte, byte)
     */
    public byte[] querySmResp(int sequenceNumber, String messageId, String finalDate, byte messageState, byte errorCode) throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(finalDate, StringParameter.FINAL_DATE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM_RESP, 0, sequenceNumber);
        buf.append(messageId);
        buf.append(finalDate);
        buf.append(messageState);
        buf.append(errorCode);
        return buf.getBytes();
    }

    // DELIVER_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.DefaultComposer#deliverSm(int, java.lang.String,
     *      byte, byte, java.lang.String, byte, byte, java.lang.String, byte,
     *      byte, byte, byte, byte, byte[])
     */
    /* (non-Javadoc)
     * @see org.jsmpp.util.PDUComposer#deliverSm(int, java.lang.String, byte, byte, java.lang.String, byte, byte, java.lang.String, byte, byte, byte, byte, byte, byte[])
     */
    public byte[] deliverSm(int sequenceNumber, String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr, byte destAddrTon, byte destAddrNpi, String destinationAddr, byte esmClass, byte protocolId, byte priorityFlag, byte registeredDelivery, byte dataCoding, byte[] shortMessage, OptionalParameter... params) throws PDUStringException {

        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);
        /*
         * StringValidator.validateString(scheduleDeliveryTime,
         * StringParameter.SCHEDULE_DELIVERY_TIME);
         * StringValidator.validateString(validityPeriod,
         * StringParameter.VALIDITY_PERIOD);
         */
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM, 0, sequenceNumber);
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
        buf.append((String) null); // schedule delivery time
        // buf.append(validityPeriod);
        buf.append((String) null); // validity period
        buf.append(registeredDelivery);
        // buf.append(replaceIfPresent);
        buf.append((byte) 0); // replace if present flag
        buf.append(dataCoding);
        // buf.append(smDefaultMsgId);
        buf.append((byte) 0); // sm default msg id
        buf.append((byte) shortMessage.length);
        buf.append(shortMessage);
        appendOptional(buf, params);
        return buf.getBytes();
    }

    public byte[] deliverSmResp(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM_RESP, 0, sequenceNumber);
        buf.append((String) null);
        return buf.getBytes();
    }

    /** Append any optional parameters (TLV's) to the end of the byte buffer */
    private void appendOptional(PDUByteBuffer buf, OptionalParameter... params) {
        if (params != null) {
            for (OptionalParameter param : params) {
                buf.append(param);
            }
        }
    }

}
