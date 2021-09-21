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
package org.jsmpp.util;

import org.jsmpp.InvalidNumberOfDestinationsException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Address;
import org.jsmpp.bean.DestinationAddress;
import org.jsmpp.bean.DistributionList;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.UnsuccessDelivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link PDUComposer}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class DefaultComposer implements PDUComposer {
    private static final Logger log = LoggerFactory.getLogger(DefaultComposer.class);
    
    public DefaultComposer() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#composeHeader(int, int, int)
     */
    @Override
    public byte[] composeHeader(int commandId, int commandStatus,
            int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(commandId, commandStatus,
                sequenceNumber);
        return buf.toBytes();
    }

    // GENERAL BIND OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#bind(int, int, java.lang.String,
     *      java.lang.String, java.lang.String, byte, byte, byte,
     *      java.lang.String)
     */
    @Override
    public byte[] bind(int commandId, int sequenceNumber, String systemId,
            String password, String systemType, byte interfaceVersion,
            byte addrTon, byte addrNpi, String addressRange)
            throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);
        StringValidator.validateString(systemType, StringParameter.SYSTEM_TYPE);
        StringValidator.validateString(addressRange, StringParameter.ADDRESS_RANGE);

        PDUByteBuffer buf = new PDUByteBuffer(commandId,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);

        buf.append(systemId);
        buf.append(password);
        buf.append(systemType);
        buf.append(interfaceVersion);
        buf.append(addrTon);
        buf.append(addrNpi);
        buf.append(addressRange);
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#bindResp(int, int, java.lang.String,
     *      org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        PDUByteBuffer buf = new PDUByteBuffer(commandId,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(systemId);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#unbind(int)
     */
    @Override
    public byte[] unbind(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_UNBIND,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#unbindResp(int, int)
     */
    @Override
    public byte[] unbindResp(int commandStatus, int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_UNBIND_RESP,
            commandStatus,  sequenceNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#outbind(int, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public byte[] outbind(int sequenceNumber, String systemId, String password)
            throws PDUStringException {
        StringValidator.validateString(systemId, StringParameter.SYSTEM_ID);
        StringValidator.validateString(password, StringParameter.PASSWORD);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_OUTBIND,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(systemId);
        buf.append(password);
        return buf.toBytes();
    }

    // ENQUIRE_LINK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#enquireLink(int)
     */
    @Override
    public byte[] enquireLink(int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_ENQUIRE_LINK,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#enquireLinkResp(int)
     */
    @Override
    public byte[] enquireLinkResp(int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_ENQUIRE_LINK_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    // GENERIC_NACK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#genericNack(int, int)
     */
    @Override
    public byte[] genericNack(int commandStatus, int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_GENERIC_NACK, commandStatus,
                sequenceNumber);
    }

    // SUBMIT_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#submitSm(int, java.lang.String, byte,
     *      byte, java.lang.String, byte, byte, java.lang.String, byte, byte,
     *      byte, java.lang.String, java.lang.String, byte, byte, byte, byte,
     *      byte[], org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] submitSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            byte registeredDelivery, byte replaceIfPresentFlag,
            byte dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);
        StringValidator.validateString(scheduleDeliveryTime, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
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
        buf.append(replaceIfPresentFlag);
        buf.append(dataCoding);
        buf.append(smDefaultMsgId);
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#submitSmResp(int, java.lang.String, org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] submitSmResp(int sequenceNumber, String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        /* Since SMPP 5.0 */
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    // QUERY_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#querySm(int, java.lang.String, byte,
     *      byte, java.lang.String)
     */
    @Override
    public byte[] querySm(int sequenceNumber, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#querySmResp(int, java.lang.String,
     *      java.lang.String, byte, byte)
     */
    @Override
    public byte[] querySmResp(int sequenceNumber, String messageId,
            String finalDate, byte messageState, byte errorCode)
            throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(finalDate, StringParameter.FINAL_DATE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.append(finalDate);
        buf.append(messageState);
        buf.append(errorCode);
        return buf.toBytes();
    }

    // DELIVER_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#deliverSm(int, java.lang.String, byte,
     *      byte, java.lang.String, byte, byte, java.lang.String, byte, byte,
     *      byte, byte, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] deliverSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            byte registeredDelivery, byte dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
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
        buf.append((String)null); // schedule delivery time
        buf.append((String)null); // validity period
        buf.append(registeredDelivery);
        buf.append((byte)0); // replace if present flag
        buf.append(dataCoding);
        buf.append((byte)0); // sm default msg id
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#deliverSmResp(int, int, String)
     */
    @Override
    public byte[] deliverSmResp(int commandStatus, int sequenceNumber, String messageId) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DELIVER_SM_RESP,
                commandStatus, sequenceNumber);
        buf.append(messageId);
        return buf.toBytes();
    }

    // DATA_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#dataSm(int, java.lang.String, byte, byte,
     *      java.lang.String, byte, byte, java.lang.String, byte, byte, byte,
     *      org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] dataSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte registeredDelivery, byte dataCoding,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DATA_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
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
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUComposer#dataSmResp(int, java.lang.String,
     *      org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] dataSmResp(int sequenceNumber, String messageId,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_DATA_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        if (optionalParameters != null && optionalParameters.length > 0) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    // CANCEL_SM OPERATION
    @Override
    public byte[] cancelSm(int sequenceNumber, String serviceType,
            String messageId, byte sourceAddrTon, byte sourceAddrNpi,
            String sourceAddr, byte destAddrTon, byte destAddrNpi,
            String destinationAddr) throws PDUStringException {
        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(destinationAddr, StringParameter.DESTINATION_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_CANCEL_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(serviceType);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(destAddrTon);
        buf.append(destAddrNpi);
        buf.append(destinationAddr);
        return buf.toBytes();
    }

    @Override
    public byte[] cancelSmResp(int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_CANCEL_SM_RESP,
                SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    // REPLACE_SM OPERATION
    @Override
    public byte[] replaceSm(int sequenceNumber, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            String scheduleDeliveryTime, String validityPeriod,
            byte registeredDelivery, byte smDefaultMsgId, byte[] shortMessage)
            throws PDUStringException {
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(scheduleDeliveryTime, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_REPLACE_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(scheduleDeliveryTime);
        buf.append(validityPeriod);
        buf.append(registeredDelivery);
        buf.append(smDefaultMsgId);
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        return buf.toBytes();
    }

    @Override
    public byte[] replaceSmResp(int sequenceNumber) {
        return composeHeader(SMPPConstant.CID_REPLACE_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    // SUBMIT_MULTI OPERATION
    @Override
    public byte[] submitMulti(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            DestinationAddress[] destinationAddresses, byte esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, byte registeredDelivery,
            byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUStringException, InvalidNumberOfDestinationsException {
        StringValidator.validateString(serviceType, StringParameter.SERVICE_TYPE);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(scheduleDeliveryTime, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(shortMessage, StringParameter.SHORT_MESSAGE);
        
        if (destinationAddresses.length > 255) {
            throw new InvalidNumberOfDestinationsException(
                    "Number of destinations is invalid. Should be no more than 255. Actual number is "
                            + destinationAddresses.length, destinationAddresses.length);
        }
        
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_MULTI,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        
        buf.append((byte)destinationAddresses.length);
        for (DestinationAddress destAddr : destinationAddresses) {
            buf.append(destAddr.getFlag().getValue());
            if (destAddr instanceof Address) {
                Address addr = (Address)destAddr;
                StringValidator.validateString(addr.getAddress(),
                        StringParameter.DESTINATION_ADDR);
                buf.append(addr.getTon());
                buf.append(addr.getNpi());
                buf.append(addr.getAddress());
            } else if (destAddr instanceof DistributionList) {
                DistributionList dl = (DistributionList)destAddr;
                StringValidator.validateString(dl.getName(), StringParameter.DL_NAME);
            } else {
                log.warn("Unknown destination address flag: {}", destAddr.getClass());
            }
        }

        buf.append(esmClass);
        buf.append(protocolId);
        buf.append(priorityFlag);
        buf.append(scheduleDeliveryTime);
        buf.append(validityPeriod);
        buf.append(registeredDelivery);
        buf.append(replaceIfPresentFlag);
        buf.append(dataCoding);
        buf.append(smDefaultMsgId);
        buf.append((byte)shortMessage.length);
        buf.append(shortMessage);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    @Override
    public byte[] submitMultiResp(int sequenceNumber, String messageId,
            UnsuccessDelivery... unsuccessDeliveries) throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_SUBMIT_MULTI_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.append((byte)unsuccessDeliveries.length); // no_unsuccess
        for (UnsuccessDelivery delivery : unsuccessDeliveries) {
            StringValidator.validateString(delivery.getDestinationAddress()
                    .getAddress(), StringParameter.DESTINATION_ADDR);
            Address destAddr = delivery.getDestinationAddress();
            buf.append(destAddr.getTon());
            buf.append(destAddr.getNpi());
            buf.append(destAddr.getAddress());
            buf.append(delivery.getErrorStatusCode());
        }
        return buf.toBytes();
    }

    // ALERT_NOTIFICATION OPERATION
    @Override
    public byte[] alertNotification(int sequenceNumber, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, byte esmeAddrTon,
            byte esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters) throws PDUStringException {
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(esmeAddr, StringParameter.ESME_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_ALERT_NOTIFICATION,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(esmeAddrTon);
        buf.append(esmeAddrNpi);
        buf.append(esmeAddr);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    // BROADCAST_SM OPERATION
    @Override
    public byte[] broadcastSm(int sequenceNumber,
                       String serviceType, byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
                       String messageId, byte priorityFlag, String scheduleDeliveryTime,
                       String validityPeriod, byte replaceIfPresentFlag, byte dataCoding, byte smDefaultMsgId,
                       OptionalParameter... optionalParameters)
        throws PDUStringException {
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(scheduleDeliveryTime, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(validityPeriod, StringParameter.VALIDITY_PERIOD);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_BROADCAST_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(serviceType);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        buf.append(messageId);
        buf.append(priorityFlag);
        buf.append(scheduleDeliveryTime);
        buf.append(validityPeriod);
        buf.append(replaceIfPresentFlag);
        buf.append(dataCoding);
        buf.append(smDefaultMsgId);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    @Override
    public byte[] broadcastSmResp(int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
        throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_BROADCAST_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.appendAll(optionalParameters);
        return buf.toBytes();
    }

    // CANCEL_BROADCAST_SM OPERATION
    @Override
    public byte[] cancelBroadcastSm(int sequenceNumber,
                              String serviceType, String messageId,
                              byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
                              OptionalParameter... optionalParameters)
        throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_CANCEL_BROADCAST_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(serviceType);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    @Override
    public byte[] cancelBroadcastSmResp(int sequenceNumber) {
        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_CANCEL_BROADCAST_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        return buf.toBytes();
    }

    // QUERY_BROADCAST_SM OPERATION
    @Override
    public byte[] queryBroadcastSm(int sequenceNumber,
                            String messageId,
                            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
                            OptionalParameter... optionalParameters)
        throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);
        StringValidator.validateString(sourceAddr, StringParameter.SOURCE_ADDR);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_BROADCAST_SM,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        buf.append(sourceAddrTon);
        buf.append(sourceAddrNpi);
        buf.append(sourceAddr);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

    @Override
    public byte[] queryBroadcastSmResp(int sequenceNumber,
                              String messageId,
                              OptionalParameter... optionalParameters)
        throws PDUStringException {
        StringValidator.validateString(messageId, StringParameter.MESSAGE_ID);

        PDUByteBuffer buf = new PDUByteBuffer(SMPPConstant.CID_QUERY_BROADCAST_SM_RESP,
            SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        buf.append(messageId);
        if (optionalParameters != null) {
            buf.appendAll(optionalParameters);
        }
        return buf.toBytes();
    }

}
