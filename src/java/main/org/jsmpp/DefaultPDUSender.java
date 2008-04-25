package org.jsmpp;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.util.DefaultComposer;
import org.jsmpp.util.PDUComposer;



/**
 * The SMPP PDU reader class.
 * 
 * @author uudashr
 * @version 1.1
 * @since 1.0
 * 
 */
public class DefaultPDUSender implements PDUSender {
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

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendHeader(java.io.OutputStream, int, int, int)
     */
    public byte[] sendHeader(OutputStream os, int commandId, int commandStatus,
            int sequenceNumber) throws IOException {

        byte[] b = pduComposer.composeHeader(commandId, commandStatus,
                sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendBind(java.io.OutputStream, org.jsmpp.BindType, int, java.lang.String, java.lang.String, java.lang.String, org.jsmpp.InterfaceVersion, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String)
     */
    public byte[] sendBind(OutputStream os, BindType bindType,
            int sequenceNumber, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws PDUStringException, IOException {

        byte[] b = pduComposer.bind(bindType.commandId(), sequenceNumber,
                systemId, password, systemType, interfaceVersion.value(),
                addrTon.value(), addrNpi.value(), addressRange);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendBindResp(java.io.OutputStream, int, int, java.lang.String)
     */
    public byte[] sendBindResp(OutputStream os, int commandId,
            int sequenceNumber, String systemId) throws PDUStringException,
            IOException {

        byte[] b = pduComposer.bindResp(commandId, sequenceNumber, systemId);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendUnbind(java.io.OutputStream, int)
     */
    public byte[] sendUnbind(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.unbind(sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendGenericNack(java.io.OutputStream, int, int)
     */
    public byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.genericNack(commandStatus, sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendUnbindResp(java.io.OutputStream, int, int)
     */
    public byte[] sendUnbindResp(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.unbindResp(commandStatus, sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendEnquireLink(java.io.OutputStream, int)
     */
    public byte[] sendEnquireLink(OutputStream out, int sequenceNumber)
            throws IOException {
        
        byte[] b = pduComposer.enquireLink(sequenceNumber);
        out.write(b);
        out.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendEnquireLinkResp(java.io.OutputStream, int)
     */
    public byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.enquireLinkResp(sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendSubmitSm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] sendSubmitSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId,
            byte[] shortMessage, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocoId, priorityFlag, scheduleDeliveryTime, 
                validityPeriod, registeredDelivery.value(), replaceIfPresent, 
                dataCoding.value(), smDefaultMsgId, shortMessage, optionalParameters);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendSubmitSmResp(java.io.OutputStream, int, java.lang.String)
     */
    public byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId) throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSmResp(sequenceNumber, messageId);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendQuerySm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String)
     */
    public byte[] sendQuerySm(OutputStream os, int sequenceNumber,
            String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.querySm(sequenceNumber, messageId, sourceAddrTon
                .value(), sourceAddrNpi.value(), sourceAddr);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendQuerySmResp(java.io.OutputStream, int, java.lang.String, java.lang.String, org.jsmpp.bean.MessageState, byte)
     */
    public byte[] sendQuerySmResp(OutputStream os, int sequenceNumber,
            String messageId, String finalDate, MessageState messageState,
            byte errorCode) throws PDUStringException, IOException {
        byte[] b = pduComposer.querySmResp(sequenceNumber, messageId,
                finalDate, messageState.value(), errorCode);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendDeliverSm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.DataCoding, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException {
        
        byte[] b = pduComposer.deliverSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocoId, priorityFlag, 
                registeredDelivery.value(), dataCoding.value(), shortMessage, 
                optionalParameters);
        os.write(b);
        os.flush();
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendDeliverSmResp(java.io.OutputStream, int)
     */
    public byte[] sendDeliverSmResp(OutputStream os, int sequenceNumber)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.deliverSmResp(sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendDataSm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.DataCoding, org.jsmpp.bean.OptionalParameter[])
     */
    public byte[] sendDataSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            OptionalParameter... optionalParameters) throws PDUStringException,
            IOException {
        byte[] b = pduComposer.dataSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr, 
                destAddrTon.value(), destAddrNpi.value(), destinationAddr, 
                esmClass.value(), registeredDelivery.value(), dataCoding.value(), 
                optionalParameters);
        os.write(b);
        os.flush();
        return b;
    }
    
    public byte[] sendDataSmResp(OutputStream os, int sequenceNumber,
            String messageId, OptionalParameter... optionalParameters)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.dataSmResp(sequenceNumber, messageId, optionalParameters);
        os.write(b);
        os.flush();
        return b;
    }
    
    public byte[] sendCancelSm(OutputStream os, int sequenceNumber,
            String serviceType, String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr) throws PDUStringException, IOException {

        byte[] b = pduComposer.cancelSm(sequenceNumber, serviceType, messageId, 
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr, 
                destAddrTon.value(), destAddrNpi.value(), destinationAddr);
        os.write(b);
        os.flush();
        return b;
    }
    
    public byte[] sendCancelSmResp(OutputStream os, int sequenceNumber)
            throws IOException {
        
        byte[] b = pduComposer.cancelSmResp(sequenceNumber);
        os.write(b);
        os.flush();
        return b;
    }
}
