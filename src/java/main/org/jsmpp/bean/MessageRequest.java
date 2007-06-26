package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class MessageRequest extends Command {
    protected String _serviceType;
    protected byte _sourceAddrTon;
    protected byte _sourceAddrNpi;
    protected String _sourceAddr;
    protected byte _destAddrTon;
    protected byte _destAddrNpi;
    protected String _destinationAddr;
    protected byte _esmClass;
    protected byte _protocolId;
    protected byte _priorityFlag;
    // FIXME uud: change using Date instead of String.
    protected String _scheduleDeliveryTime;
    // FIXME uud: change using Date instead of String.
    protected String _validityPeriod;
    protected byte _registeredDelivery;
    protected byte _replaceIfPresent;
    protected byte _dataCoding;
    protected byte _smDefaultMsgId;
    protected byte _smLength;
    protected byte[] _shortMessage;

    public MessageRequest() {
        super();
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isDefaultMessageType() {
        return isDefaultMessageType(_esmClass);
    }

    /**
     * Message Type.
     */
    public void setDefaultMessageType() {
        _esmClass = composeDefaultMessageType(_esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isUdhi() {
        return isUdhi(_esmClass);
    }

    /**
     * Specific Features.
     */
    public void setUdhi() {
        _esmClass = composeUdhi(_esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isReplyPath() {
        return isReplyPath(_esmClass);
    }

    /**
     * Specific Features.
     */
    public void setReplyPath() {
        _esmClass = composeReplyPath(_esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isUdhiAndReplyPath() {
        return isUdhiAndReplyPath(_esmClass);
    }

    /**
     * Specific Features.
     */
    public void setUdhiAndReplyPath() {
        _esmClass = composeUdhiAndReplyPath(_esmClass);
    }

    /**
     * @return the dataCoding
     */
    public byte getDataCoding() {
        return _dataCoding;
    }

    /**
     * @param dataCoding the dataCoding to set
     */
    public void setDataCoding(byte dataCoding) {
        _dataCoding = dataCoding;
    }

    /**
     * @return the destAddrNpi
     */
    public byte getDestAddrNpi() {
        return _destAddrNpi;
    }

    /**
     * @param destAddrNpi the destAddrNpi to set
     */
    public void setDestAddrNpi(byte destAddrNpi) {
        _destAddrNpi = destAddrNpi;
    }

    /**
     * @return the destAddrTon
     */
    public byte getDestAddrTon() {
        return _destAddrTon;
    }

    /**
     * @param destAddrTon the destAddrTon to set
     */
    public void setDestAddrTon(byte destAddrTon) {
        _destAddrTon = destAddrTon;
    }

    /**
     * @return the destinationAddr
     */
    public String getDestinationAddr() {
        return _destinationAddr;
    }

    /**
     * @param destinationAddr the destinationAddr to set
     */
    public void setDestinationAddr(String destinationAddr) {
        _destinationAddr = destinationAddr;
    }

    /**
     * @return the esmClass
     */
    public byte getEsmClass() {
        return _esmClass;
    }

    /**
     * @param esmClass the esmClass to set
     */
    public void setEsmClass(byte esmClass) {
        _esmClass = esmClass;
    }

    /**
     * @return the priorityFlag
     */
    public byte getPriorityFlag() {
        return _priorityFlag;
    }

    /**
     * @param priorityFlag the priorityFlag to set
     */
    public void setPriorityFlag(byte priorityFlag) {
        _priorityFlag = priorityFlag;
    }

    /**
     * @return the protocolId
     */
    public byte getProtocolId() {
        return _protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(byte protocolId) {
        _protocolId = protocolId;
    }

    /**
     * @return the registeredDelivery
     */
    public byte getRegisteredDelivery() {
        return _registeredDelivery;
    }

    /**
     * @param registeredDelivery the registeredDelivery to set
     */
    public void setRegisteredDelivery(byte registeredDelivery) {
        _registeredDelivery = registeredDelivery;
    }

    /**
     * @return the replaceIfPresent
     */
    public byte getReplaceIfPresent() {
        return _replaceIfPresent;
    }

    /**
     * @param replaceIfPresent the replaceIfPresent to set
     */
    public void setReplaceIfPresent(byte replaceIfPresent) {
        _replaceIfPresent = replaceIfPresent;
    }

    /**
     * @return the scheduleDeliveryTime
     */
    public String getScheduleDeliveryTime() {
        return _scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime the scheduleDeliveryTime to set
     */
    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        _scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return _serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        _serviceType = serviceType;
    }

    /**
     * @return the shortMessage
     */
    public byte[] getShortMessage() {
        return _shortMessage;
    }

    /**
     * @param shortMessage the shortMessage to set
     */
    public void setShortMessage(byte[] shortMessage) {
        _shortMessage = shortMessage;
    }

    /**
     * @return the smDefaultMsgId
     */
    public byte getSmDefaultMsgId() {
        return _smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId the smDefaultMsgId to set
     */
    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        _smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return the smLength
     */
    public byte getSmLength() {
        return _smLength;
    }

    /**
     * @param smLength the smLength to set
     */
    public void setSmLength(byte smLength) {
        _smLength = smLength;
    }

    /**
     * @return the sourceAddr
     */
    public String getSourceAddr() {
        return _sourceAddr;
    }

    /**
     * @param sourceAddr the sourceAddr to set
     */
    public void setSourceAddr(String sourceAddr) {
        _sourceAddr = sourceAddr;
    }

    /**
     * @return the sourceAddrNpi
     */
    public byte getSourceAddrNpi() {
        return _sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi the sourceAddrNpi to set
     */
    public void setSourceAddrNpi(byte sourceAddrNpi) {
        _sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return the sourceAddrTon
     */
    public byte getSourceAddrTon() {
        return _sourceAddrTon;
    }

    /**
     * @param sourceAddrTon the sourceAddrTon to set
     */
    public void setSourceAddrTon(byte sourceAddrTon) {
        _sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return the validityPeriod
     */
    public String getValidityPeriod() {
        return _validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(String validityPeriod) {
        _validityPeriod = validityPeriod;
    }

    /**
     * Message Type. Default message Type.
     * 
     * @param esmClass
     * @return <tt>true</tt> if esmClass indicate delivery receipt
     */
    public static boolean isDefaultMessageType(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_DEFAULT_MESSAGE_TYPE);
    }

    /**
     * Message Type. Compose the esm_class as with default message Type.
     * 
     * @param esmClass
     * @return
     */
    public static byte composeDefaultMessageType(byte esmClass) {
        return cleanMessageType(esmClass);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static boolean isUdhi(byte esmClass) {
        return isSpecificFeatures(esmClass,
                SMPPConstant.ESMCLS_UDHI_INDICATOR_SET);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static byte composeUdhi(byte esmClass) {
        return composeSpecificFeatures(esmClass,
                SMPPConstant.ESMCLS_UDHI_INDICATOR_SET);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static boolean isReplyPath(byte esmClass) {
        return isSpecificFeatures(esmClass, SMPPConstant.ESMCLS_REPLY_PATH);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static byte composeReplyPath(byte esmClass) {
        return composeSpecificFeatures(esmClass, SMPPConstant.ESMCLS_REPLY_PATH);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static boolean isUdhiAndReplyPath(byte esmClass) {
        return isSpecificFeatures(esmClass, SMPPConstant.ESMCLS_UDHI_REPLY_PATH);
    }

    /**
     * Specific Features.
     * 
     * @param esmClass
     * @return
     */
    public static byte composeUdhiAndReplyPath(byte esmClass) {
        return composeSpecificFeatures(esmClass,
                SMPPConstant.ESMCLS_UDHI_REPLY_PATH);
    }

    /*
     * Messaging Mode.
     */
    protected final static boolean isMessagingMode(byte esmClass,
            byte messagingModeValue) {
        // 00000011 = 0x03
        return (esmClass & 0x03) == messagingModeValue;
    }

    /**
     * Compose the Messaging Mode. Messaging Mode encoded on ESM Class at bits 1 -
     * 0.
     * 
     * @param esmClass current/old ESM class.
     * @param messagingModeValue
     * @return the encoded messaging mode at ESM class
     */
    protected final static byte composeMessagingMode(byte esmClass,
            byte messagingModeValue) {
        return (byte)(cleanMessagingMode(esmClass) | messagingModeValue);
    }

    /**
     * Clean the Messaging Mode or clean the ESM Class at bits 1 - 0.
     * 
     * @param esmClass
     * @return
     */
    protected final static byte cleanMessagingMode(byte esmClass) {
        /*
         * 00000011 = 0x03
         * 
         * esmClass & 0x03 -> will clear the bits 1 - 0
         */
        return (byte)(esmClass & 0x03);
    }

    /*
     * Message Type.
     */
    protected final static boolean isMessageType(byte esmClass,
            byte messageTypeValue) {
        // 00111100 = 0x3c
        return (esmClass & 0x3c) == messageTypeValue;
    }

    /**
     * Compose the Message Type. Message Type encoded on ESM Class at bits 5 -
     * 2.
     * 
     * @param esmClass
     * @param messageTypeValue
     * @return
     */
    protected final static byte composeMessageType(byte esmClass,
            byte messageTypeValue) {
        return (byte)(cleanMessageType(esmClass) | messageTypeValue);
    }

    /**
     * Clean the Message Type or clean the ESM Class at bits 5 - 2.
     * 
     * @param esmClass
     * @return
     */
    protected final static byte cleanMessageType(byte esmClass) {
        /*
         * 11000011 = 0xc3
         * 
         * esmClass & 0xc3 -> will clear the bits 5 - 2
         */
        return (byte)(esmClass & 0xc3);
    }

    /*
     * Specific Features.
     */
    protected final static boolean isSpecificFeatures(byte esmClass,
            byte specificFeaturesValue) {
        // 01000000 = 0xC0
        return (esmClass & 0xC0) == specificFeaturesValue;
    }

    /**
     * Compose Specific Features. Specific Features encoded on ESM Class at bits
     * 7 - 6.
     * 
     * @param esmClass
     * @param specificFeaturesValue
     * @return
     */
    protected final static byte composeSpecificFeatures(byte esmClass,
            byte specificFeaturesValue) {
        return (byte)(cleanSpecificFeatures(esmClass) | specificFeaturesValue);
    }

    /**
     * Clean the Specific Features or ESM Class at bits 7 - 6.
     * 
     * @param esmClass
     * @return
     */
    protected final static byte cleanSpecificFeatures(byte esmClass) {
        /*
         * 00111111 = 0x3f
         * 
         * esmClass & 0x3f -> will clear the bits 7 -6
         */
        return (byte)(esmClass & 0x3f);
    }

    /*
     * SMSC Delivert Receipt.
     */
    /**
     * SMSC Delivery Receipt.
     * 
     * @param registeredDelivery
     * @param smscDeliveryReceiptValue
     * @return
     */
    protected static final boolean isSmscDeliveryReceipt(
            byte registeredDelivery, byte smscDeliveryReceiptValue) {
        // xxxxxx11 = 0x03 mask bits 1 - 0
        return (registeredDelivery & 0x03) == smscDeliveryReceiptValue;
    }

    protected static final byte composeSmscDelReceipt(byte registeredDelivery,
            byte smscDeliveryReceiptValue) {
        return (byte)(cleanSmscDeliveryReceipt(registeredDelivery) | smscDeliveryReceiptValue);
    }

    protected static final byte cleanSmscDeliveryReceipt(byte registeredDelivery) {
        // 11111100 = 0x0fc
        return (byte)(registeredDelivery & 0x0fc);
    }

    /*
     * SME originated Acknowledgement.
     */
    /**
     * SME originated Acknowledgement.
     * 
     * @param registeredDeliery
     * @param smeOriginatedAckValue
     * @return
     */
    protected static final boolean isSmeAck(byte registeredDeliery,
            byte smeOriginatedAckValue) {
        // xxxx11xx = 0x0c mask bits 3 - 2
        return (registeredDeliery & 0x0c) == smeOriginatedAckValue;
    }

    protected static final byte composeSmeAck(byte registeredDelivery,
            byte smeOriginatedValue) {
        return (byte)(cleanSmeAck(registeredDelivery) | smeOriginatedValue);
    }

    protected static final byte cleanSmeAck(byte registeredDelivery) {
        // 11110011 = 0xf3
        // (registeredDelivery & 0x0c) will clean the bits 3 - 2.
        return (byte)(registeredDelivery & 0x0c);
    }
}
