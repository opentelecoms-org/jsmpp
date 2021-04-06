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
package org.jsmpp.bean;

import java.util.Arrays;
import java.util.Objects;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.OptionalParameter.Tag;

/**
 * @author uudashr
 *
 */
public class AbstractSmCommand extends Command {
    private static final long serialVersionUID = 745091499168413812L;
    
    protected String serviceType;
    protected byte sourceAddrTon;
    protected byte sourceAddrNpi;
    protected String sourceAddr;
    protected byte destAddrTon;
    protected byte destAddrNpi;
    protected String destAddress;
    protected byte esmClass;
    protected byte registeredDelivery;
    protected byte dataCoding;
    protected OptionalParameter[] optionalParameters;

    public AbstractSmCommand() {
        super();
    }

    /**
     * Message Type.
     *
     * @return if the esmClass is the default message type (00)
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
     * @return if the esmClass contains the User Data Header Indicator
     */
    public boolean isUdhi() {
        return isUdhi(esmClass);
    }

    /**
     * Set the UDHI in the ESMClass
     */
    public void setUdhi() {
        esmClass = composeUdhi(esmClass);
    }

    /**
     * Specific Features.
     *
     * @return {@code true} if the {@link ESMClass} of the command has Reply Path set; {@code false} otherwise.
     */
    public boolean isReplyPath() {
        return isReplyPath(esmClass);
    }

    /**
     * Set the Reply Path in the ESMClass
     */
    public void setReplyPath() {
        esmClass = composeReplyPath(esmClass);
    }

    /**
     * Specific Features.
     *
     * @return {@code true} if the {@link ESMClass} of the command has UDHI and Reply Path set; {@code false} otherwise.
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
     * @return the data coding scheme
     */
    public byte getDataCoding() {
        return dataCoding;
    }

    /**
     * @param dataCoding the data coding scheme to set
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

    public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass)
    {
    	return OptionalParameters.get(tagClass, optionalParameters);
    }
    
    public OptionalParameter getOptionalParameter(Tag tagEnum)
    {
    	return OptionalParameters.get(tagEnum.code(), optionalParameters);
    }

    public OptionalParameter getOptionalParameter(short code)
    {
        return OptionalParameters.get(code, optionalParameters);
    }
    
    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }

    public void setOptionalParameters(OptionalParameter... optionalParametes) {
        this.optionalParameters = optionalParametes;
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
     * Message Type. Default message Type.
     *
     * @param esmClass the ESMClass
     * @return {@code true} if esmClass indicates delivery receipt
     */
    public static boolean isDefaultMessageType(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_DEFAULT_MESSAGE_TYPE);
    }

    /**
     * Message Type. Compose the esm_class as with default message Type.
     *
     * @param esmClass the ESMClass
     * @return the modified ESMClass as {@code byte}
     */
    public static byte composeDefaultMessageType(byte esmClass) {
        return cleanMessageType(esmClass);
    }

    /**
     * Check the ESM Class for the UDHI bit.
     *
     * @param esmClass the ESM Class byte to examine
     * @return {@code true} if the esmClass parameter indicates the User Data Header Indicator (UDHI)
     */
    public static boolean isUdhi(byte esmClass) {
        return isSpecificFeatures(esmClass,
                SMPPConstant.ESMCLS_UDHI_INDICATOR_SET);
    }

    /**
     * Set the UDHI bit in the ESM Class.
     *
     * @param esmClass the original ESM Class
     * @return the modified ESMClass as {@code byte}
     */
    public static byte composeUdhi(byte esmClass) {
        return composeSpecificFeatures(esmClass,
                SMPPConstant.ESMCLS_UDHI_INDICATOR_SET);
    }

    /**
     * Check the ESM Class for the Reply Path feature.
     *
     * @param esmClass the ESM class byte to examine
     * @return {@code true} if the ESM class parameter indicates an Reply Path feature
     */
    public static boolean isReplyPath(byte esmClass) {
        return isSpecificFeatures(esmClass, SMPPConstant.ESMCLS_REPLY_PATH);
    }

    /**
     * Set the Reply Path feature in the ESM class.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM Class with Reply Path
     */
    public static byte composeReplyPath(byte esmClass) {
        return composeSpecificFeatures(esmClass, SMPPConstant.ESMCLS_REPLY_PATH);
    }

    /**
     * Check the ESM Class for the UDHI and Reply Path features.
     *
     * @param esmClass the ESM class byte to examine
     * @return {@code true} if the ESM class has User Data Header Indicator and Reply Path
     */
    public static boolean isUdhiAndReplyPath(byte esmClass) {
        return isSpecificFeatures(esmClass, SMPPConstant.ESMCLS_UDHI_REPLY_PATH);
    }

    /**
     * Specific Features.
     *
     * @param esmClass the original ESM class
     * @return The modified ESM class with User Data Header Indicator and Reply Path features set.
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
     * @param esmClass original ESM class.
     * @param messagingModeValue the messaging mode bits to set
     * @return the encoded messaging mode at ESM class
     */
    protected final static byte composeMessagingMode(byte esmClass,
            byte messagingModeValue) {
        return (byte)(cleanMessagingMode(esmClass) | messagingModeValue);
    }

    /**
     * Clean the Messaging Mode or clean the ESM Class at bits 1 - 0.
     *
     * @param esmClass the original ESM class
     * @return the ESM class without the Messaging Mode bits
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
     * Compose the Message Type. Message Type encoded on ESM Class at bits 5 - 2.
     *
     * @param esmClass the original ESM class
     * @param messageTypeValue the message type to be set in the ESM class
     * @return the modified ESM class as {@code byte}
     */
    protected final static byte composeMessageType(byte esmClass,
            byte messageTypeValue) {
        return (byte)(cleanMessageType(esmClass) | messageTypeValue);
    }

    /**
     * Clean the Message Type or clean the ESM Class at bits 5 - 2.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class as {@code byte}
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
     * Compose Specific Features. Specific Features encoded on ESM Class at bits 7 - 6.
     *
     * @param esmClass the original ESM class
     * @param specificFeaturesValue the specific features to set
     * @return the modified ESM class as {@code byte}
     */
    protected final static byte composeSpecificFeatures(byte esmClass,
            byte specificFeaturesValue) {
        return (byte)(cleanSpecificFeatures(esmClass) | specificFeaturesValue);
    }

    /**
     * Clean the Specific Features or ESM Class at bits 7 - 6.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class as {@code byte}
     */
    protected final static byte cleanSpecificFeatures(byte esmClass) {
        /*
         * 00111111 = 0x3f
         *
         * esmClass & 0x3f -> will clear the bits 7 -6
         */
        return (byte)(esmClass & 0x3f);
    }

    /**
     * Check SMSC Delivery Receipt for receipt value.
     *
     * @param registeredDelivery the registered delivery
     * @param smscDeliveryReceiptValue the SMSC Delivery Receipt to check for
     * @return {@code true} if the registered delivery has the SMSC Delivery Receipt value set
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

    /**
     * SME originated Acknowledgement.
     *
     * @param registeredDelivery the registered delivery
     * @param smeOriginatedAckValue the SME Originated Ack to check for
     * @return {@code true} if the registered delivery has the SME Originated Ack value set
     */
    protected static final boolean isSmeAck(byte registeredDelivery,
            byte smeOriginatedAckValue) {
        // xxxx11xx = 0x0c mask bits 3 - 2
        return (registeredDelivery & 0x0c) == smeOriginatedAckValue;
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final AbstractSmCommand that = (AbstractSmCommand) o;
        return sourceAddrTon == that.sourceAddrTon &&
            sourceAddrNpi == that.sourceAddrNpi &&
            destAddrTon == that.destAddrTon &&
            destAddrNpi == that.destAddrNpi &&
            esmClass == that.esmClass &&
            registeredDelivery == that.registeredDelivery &&
            dataCoding == that.dataCoding &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(sourceAddr, that.sourceAddr) &&
            Objects.equals(destAddress, that.destAddress) &&
            Arrays.equals(optionalParameters, that.optionalParameters);
    }

    @Override
    public int hashCode() {
        int result = Objects
            .hash(super.hashCode(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destAddress, esmClass, registeredDelivery,
                dataCoding);
        result = 31 * result + Arrays.hashCode(optionalParameters);
        return result;
    }
}
