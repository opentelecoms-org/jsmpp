package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class MessageRequest extends Command {
    protected String serviceType;
    protected byte sourceAddrTon;
    protected byte sourceAddrNpi;
    protected String sourceAddr;
    protected byte destAddrTon;
    protected byte destAddrNpi;
    protected String destAddress;
    protected byte esmClass;
    protected byte protocolId;
    protected byte priorityFlag;
    protected String scheduleDeliveryTime;
    protected String validityPeriod;
    protected byte registeredDelivery;
    protected byte replaceIfPresent;
    protected byte dataCoding;
    protected byte smDefaultMsgId;
    protected byte smLength;
    protected byte[] shortMessage;
    protected OptionalParameter[] optionalParametes;
    
    public MessageRequest() {
        super();
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isDefaultMessageType() {
        return isDefaultMessageType(esmClass);
    }

    /**
     * Message Type.
     */
    public void setDefaultMessageType() {
        esmClass = composeDefaultMessageType(esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isUdhi() {
        return isUdhi(esmClass);
    }

    /**
     * Specific Features.
     */
    public void setUdhi() {
        esmClass = composeUdhi(esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isReplyPath() {
        return isReplyPath(esmClass);
    }

    /**
     * Specific Features.
     */
    public void setReplyPath() {
        esmClass = composeReplyPath(esmClass);
    }

    /**
     * Specific Features.
     * 
     * @return
     */
    public boolean isUdhiAndReplyPath() {
        return isUdhiAndReplyPath(esmClass);
    }

    /**
     * Specific Features.
     */
    public void setUdhiAndReplyPath() {
        esmClass = composeUdhiAndReplyPath(esmClass);
    }

    /**
     * @return the dataCoding
     */
    public byte getDataCoding() {
        return dataCoding;
    }

    /**
     * @param dataCoding the dataCoding to set
     */
    public void setDataCoding(byte dataCoding) {
        this.dataCoding = dataCoding;
    }

    public byte getDestAddrTon() {
        return destAddrTon;
    }
    
    public void setDestAddrTon(byte destAddrTon) {
        this.destAddrTon = destAddrTon;
    }
    
    public byte getDestAddrNpi() {
        return destAddrNpi;
    }
    
    public void setDestAddrNpi(byte destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }
    
    public String getDestAddress() {
        return destAddress;
    }
    
    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }

    /**
     * @return the esmClass
     */
    public byte getEsmClass() {
        return esmClass;
    }

    /**
     * @param esmClass the esmClass to set
     */
    public void setEsmClass(byte esmClass) {
        this.esmClass = esmClass;
    }

    /**
     * @return the priorityFlag
     */
    public byte getPriorityFlag() {
        return priorityFlag;
    }

    /**
     * @param priorityFlag the priorityFlag to set
     */
    public void setPriorityFlag(byte priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    /**
     * @return the protocolId
     */
    public byte getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(byte protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the registeredDelivery
     */
    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    /**
     * @param registeredDelivery the registeredDelivery to set
     */
    public void setRegisteredDelivery(byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    /**
     * @return the replaceIfPresent
     */
    public byte getReplaceIfPresent() {
        return replaceIfPresent;
    }

    /**
     * @param replaceIfPresent the replaceIfPresent to set
     */
    public void setReplaceIfPresent(byte replaceIfPresent) {
        this.replaceIfPresent = replaceIfPresent;
    }

    /**
     * @return the scheduleDeliveryTime
     */
    public String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime the scheduleDeliveryTime to set
     */
    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the shortMessage
     */
    public byte[] getShortMessage() {
        return shortMessage;
    }

    /**
     * @param shortMessage the shortMessage to set
     */
    public void setShortMessage(byte[] shortMessage) {
        this.shortMessage = shortMessage;
    }
    
    public OptionalParameter[] getOptionalParametes() {
        return optionalParametes;
    }
    
    public void setOptionalParametes(OptionalParameter[] optionalParametes) {
        this.optionalParametes = optionalParametes;
    }
    
    /**
     * @return the smDefaultMsgId
     */
    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId the smDefaultMsgId to set
     */
    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return the smLength
     */
    public byte getSmLength() {
        return smLength;
    }

    /**
     * @param smLength the smLength to set
     */
    public void setSmLength(byte smLength) {
        this.smLength = smLength;
    }

    public byte getSourceAddrTon() {
        return sourceAddrTon;
    }
    
    public void setSourceAddrTon(byte sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }
    
    public byte getSourceAddrNpi() {
        return sourceAddrNpi;
    }
    
    public void setSourceAddrNpi(byte sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }
    
    public String getSourceAddr() {
        return sourceAddr;
    }
    
    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }
    
    /**
     * @return the validityPeriod
     */
    public String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
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
