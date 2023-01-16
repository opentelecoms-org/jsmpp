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
package org.jsmpp;

import static org.jsmpp.bean.OptionalParameters.EMPTY_OPTIONAL_PARAMETERS;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DestinationAddress;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.util.DefaultComposer;
import org.jsmpp.util.HexUtil;
import org.jsmpp.util.OctetUtil;
import org.jsmpp.util.PDUComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The SMPP PDU reader class.
 * 
 * @author uudashr
 * @version 1.1
 * @since 1.0
 * 
 */
public class DefaultPDUSender implements PDUSender {

    private static final Logger log = LoggerFactory.getLogger(DefaultPDUSender.class);
    private static final byte[] EMPTY_SHORT_MESSAGE = new byte[]{};
    private final PDUComposer pduComposer;

    /**
     * Default constructor.
     */
    public DefaultPDUSender() {
        this(new DefaultComposer());
    }

    /**
     * Construct with specified PDU composer.
     *
     * @param pduComposer is the PDU composer.
     */
    public DefaultPDUSender(PDUComposer pduComposer) {
        this.pduComposer = pduComposer;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendHeader(java.io.OutputStream, int, int, int)
     */
    @Override
    public byte[] sendHeader(OutputStream os, int commandId, int commandStatus,
            int sequenceNumber) throws IOException {

        byte[] b = pduComposer.composeHeader(commandId, commandStatus,
                sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendBind(java.io.OutputStream,
     *      org.jsmpp.BindType, int, java.lang.String, java.lang.String,
     *      java.lang.String, org.jsmpp.InterfaceVersion,
     *      org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator,
     *      java.lang.String)
     */
    @Override
    public byte[] sendBind(OutputStream os, BindType bindType,
            int sequenceNumber, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws PDUStringException, IOException {

        byte[] b = pduComposer.bind(bindType.commandId(), sequenceNumber,
                systemId, password, systemType, interfaceVersion.value(),
                addrTon.value(), addrNpi.value(), addressRange);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendBindResp(java.io.OutputStream, int, int,
     *      java.lang.String)
     */
    @Override
    public byte[] sendBindResp(OutputStream os, int commandId,
            int sequenceNumber, String systemId, InterfaceVersion interfaceVersion) throws PDUStringException,
            IOException {

        OptionalParameter[] optionalParameters;
        if (interfaceVersion != null) {
            OptionalParameter interfaceVersionParam = new OptionalParameter.Byte(Tag.SC_INTERFACE_VERSION, interfaceVersion.value());
            optionalParameters = new OptionalParameter[] {interfaceVersionParam};
        } else {
            optionalParameters = EMPTY_OPTIONAL_PARAMETERS;
        }

        byte[] b = pduComposer.bindResp(commandId, sequenceNumber, systemId, optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendOutbind(java.io.OutputStream,
     *      int, java.lang.String, java.lang.String)
     */
    @Override
    public byte[] sendOutbind(OutputStream os, int sequenceNumber, String systemId, String password)
        throws PDUStringException, IOException
    {
        byte[] b = this.pduComposer.outbind( sequenceNumber,systemId, password);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendUnbind(java.io.OutputStream, int)
     */
    @Override
    public byte[] sendUnbind(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.unbind(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendGenericNack(java.io.OutputStream, int, int)
     */
    @Override
    public byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.genericNack(commandStatus, sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendUnbindResp(java.io.OutputStream, int, int)
     */
    @Override
    public byte[] sendUnbindResp(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.unbindResp(commandStatus, sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendEnquireLink(java.io.OutputStream, int)
     */
    @Override
    public byte[] sendEnquireLink(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.enquireLink(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendEnquireLinkResp(java.io.OutputStream, int)
     */
    @Override
    public byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.enquireLinkResp(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendSubmitSm(java.io.OutputStream, int,
     *      java.lang.String, org.jsmpp.TypeOfNumber,
     *      org.jsmpp.NumberingPlanIndicator, java.lang.String,
     *      org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator,
     *      java.lang.String, org.jsmpp.bean.ESMClass, byte, byte,
     *      java.lang.String, java.lang.String,
     *      org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding,
     *      byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] sendSubmitSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocolId, priorityFlag,
                scheduleDeliveryTime, validityPeriod,
                registeredDelivery.value(), replaceIfPresent, dataCoding.toByte(),
                smDefaultMsgId, checkShortMessage(shortMessage), optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendSubmitSmResp(java.io.OutputStream, int,
     *      java.lang.String)
     */
    @Override
    public byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSmResp(sequenceNumber, messageId, optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendQuerySm(java.io.OutputStream, int,
     *      java.lang.String, org.jsmpp.TypeOfNumber,
     *      org.jsmpp.NumberingPlanIndicator, java.lang.String)
     */
    @Override
    public byte[] sendQuerySm(OutputStream os, int sequenceNumber,
            String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.querySm(sequenceNumber, messageId, sourceAddrTon
                .value(), sourceAddrNpi.value(), sourceAddr);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendQuerySmResp(java.io.OutputStream, int,
     *      java.lang.String, java.lang.String, org.jsmpp.bean.MessageState,
     *      byte)
     */
    @Override
    public byte[] sendQuerySmResp(OutputStream os, int sequenceNumber,
            String messageId, String finalDate, MessageState messageState,
            byte errorCode) throws PDUStringException, IOException {
        byte[] b = pduComposer.querySmResp(sequenceNumber, messageId,
                finalDate, messageState.value(), errorCode);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendDeliverSm(java.io.OutputStream, int,
     *      java.lang.String, org.jsmpp.TypeOfNumber,
     *      org.jsmpp.NumberingPlanIndicator, java.lang.String,
     *      org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator,
     *      java.lang.String, org.jsmpp.bean.ESMClass, byte, byte,
     *      org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.DataCoding,
     *      byte[], org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException {

        byte[] b = pduComposer.deliverSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocolId, priorityFlag, registeredDelivery.value(),
                dataCoding.toByte(), checkShortMessage(shortMessage), optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendDeliverSmResp(java.io.OutputStream, int, int, String)
     */
    @Override
    public byte[] sendDeliverSmResp(OutputStream os, int commandStatus, int sequenceNumber, String messageId)
            throws IOException {
        byte[] b = pduComposer.deliverSmResp(commandStatus, sequenceNumber, messageId);
        writeAndFlush(os, b);
        return b;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jsmpp.PDUSender#sendDataSm(java.io.OutputStream, int,
     *      java.lang.String, org.jsmpp.TypeOfNumber,
     *      org.jsmpp.NumberingPlanIndicator, java.lang.String,
     *      org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator,
     *      java.lang.String, org.jsmpp.bean.ESMClass,
     *      org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.DataCoding,
     *      org.jsmpp.bean.OptionalParameter[])
     */
    @Override
    public byte[] sendDataSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException {
        byte[] b = pduComposer.dataSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), registeredDelivery.value(), dataCoding
                        .toByte(), optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendDataSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.dataSmResp(sequenceNumber, messageId,
                optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendCancelSm(OutputStream os, int sequenceNumber,
            String serviceType, String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr) throws PDUStringException, IOException {
        byte[] b = pduComposer.cancelSm(sequenceNumber, serviceType, messageId,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendCancelSmResp(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.cancelSmResp(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendReplaceSm(OutputStream os, int sequenceNumber,
            String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUStringException, IOException {
        byte[] b = pduComposer.replaceSm(sequenceNumber, messageId,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                scheduleDeliveryTime, validityPeriod,
                registeredDelivery.value(), smDefaultMsgId,
                checkShortMessage(shortMessage));
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendReplaceSmResp(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.replaceSmResp(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    public byte[] sendSubmitMulti(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            DestinationAddress[] destinationAddresses, ESMClass esmClass,
            byte protocolId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
            byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            InvalidNumberOfDestinationsException, IOException {
        byte[] b = pduComposer.submitMulti(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destinationAddresses, esmClass.value(), protocolId,
                priorityFlag, scheduleDeliveryTime, validityPeriod,
                registeredDelivery.value(), replaceIfPresentFlag.value(),
                dataCoding.toByte(), smDefaultMsgId, checkShortMessage(shortMessage),
                optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendSubmitMultiResp(OutputStream os, int sequenceNumber,
            String messageId, UnsuccessDelivery... unsuccessDeliveries)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.submitMultiResp(sequenceNumber, messageId,
                unsuccessDeliveries);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendAlertNotification(OutputStream os, int sequenceNumber,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber esmeAddrTon, NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
            OptionalParameter... optionalParameters)
        throws PDUStringException, IOException {
        byte[] b = pduComposer.alertNotification(sequenceNumber,
            sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
            esmeAddrTon.value(), esmeAddrNpi.value(), esmeAddr, optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendBroadcastSm(final OutputStream os, final int sequenceNumber, final String serviceType,
            final TypeOfNumber sourceAddrTon, final NumberingPlanIndicator sourceAddrNpi, final String sourceAddr,
            final String messageId,
            byte priorityFlag, String scheduleDeliveryTime, String validityPeriod,
            ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId,
            OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.broadcastSm(sequenceNumber, serviceType,
            sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr, messageId, priorityFlag,
            scheduleDeliveryTime, validityPeriod, replaceIfPresentFlag.value(),
            dataCoding.toByte(), smDefaultMsgId, optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendBroadcastSmResp(final OutputStream os, final int sequenceNumber, final String messageId,
                                      final OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.broadcastSmResp(sequenceNumber, messageId,
            optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendCancelBroadcastSm(final OutputStream os, final int sequenceNumber, final String serviceType, final String messageId,
                                        final TypeOfNumber sourceAddrTon, final NumberingPlanIndicator sourceAddrNpi, final String sourceAddr,
                                        final OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.cancelBroadcastSm(sequenceNumber, serviceType, messageId,
            sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
            optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendCancelBroadcastSmResp(final OutputStream os, final int sequenceNumber) throws IOException {
        byte[] b = pduComposer.cancelBroadcastSmResp(sequenceNumber);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendQueryBroadcastSm(final OutputStream os, final int sequenceNumber, final String messageId,
                                       final TypeOfNumber sourceAddrTon, final NumberingPlanIndicator sourceAddrNpi, final String sourceAddr,
                                       final OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.queryBroadcastSm(sequenceNumber, messageId,
            sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
            optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    @Override
    public byte[] sendQueryBroadcastSmResp(final OutputStream os, final int sequenceNumber, final String messageId,
                                       final OptionalParameter... optionalParameters) throws PDUStringException, IOException {
        byte[] b = pduComposer.queryBroadcastSmResp(sequenceNumber, messageId, optionalParameters);
        writeAndFlush(os, b);
        return b;
    }

    private static void writeAndFlush(OutputStream out, byte[] b)
            throws IOException {
        int commandId = OctetUtil.bytesToInt(b, 4);
        if (commandId == SMPPConstant.CID_ENQUIRE_LINK
            || commandId == SMPPConstant.CID_ENQUIRE_LINK_RESP) {
            if (log.isTraceEnabled()) {
                log.trace("Sending PDU {}", HexUtil.convertBytesToHexString(b, 0, b.length));
            }
        } else if (log.isDebugEnabled()) {
            log.debug("Sending PDU {}", HexUtil.convertBytesToHexString(b, 0, b.length));
        }
        out.write(b);
        out.flush();
    }

    private byte[] checkShortMessage(byte[] shortMessage) {
        return shortMessage != null ? shortMessage : EMPTY_SHORT_MESSAGE;
    }
}
