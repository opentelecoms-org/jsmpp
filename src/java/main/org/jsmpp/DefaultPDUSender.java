package org.jsmpp;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.util.PDUComposer;



/**
 * The SMPP PDU reader class.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class DefaultPDUSender implements PDUSender {
    private PDUComposer pduComposer;

    /**
     * Construct with specified pduComposer.
     * 
     * @param pduComposer is the {@link PDUComposer}
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
        synchronized (os) {
            os.write(b);
            os.flush();
        }
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
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendBindResp(java.io.OutputStream, int, int, java.lang.String)
     */
    public byte[] sendBindResp(OutputStream os, int commandId,
            int sequenceNumber, String systemId) throws PDUStringException,
            IOException {

        byte[] b = pduComposer.bindResp(commandId, sequenceNumber, systemId);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendUnbind(java.io.OutputStream, int)
     */
    public byte[] sendUnbind(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.unbind(sequenceNumber);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendGenericNack(java.io.OutputStream, int, int)
     */
    public byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.genericNack(commandStatus, sequenceNumber);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendUnbindResp(java.io.OutputStream, int, int)
     */
    public byte[] sendUnbindResp(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException {
        byte[] b = pduComposer.unbindResp(commandStatus, sequenceNumber);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendEnquireLink(java.io.OutputStream, int)
     */
    public byte[] sendEnquireLink(OutputStream out, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.enquireLink(sequenceNumber);
        synchronized (out) {
            out.write(b);
            out.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendEnquireLinkResp(java.io.OutputStream, int)
     */
    public byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException {
        byte[] b = pduComposer.enquireLinkResp(sequenceNumber);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendSubmitSm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte, byte[])
     */
    public byte[] sendSubmitSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocoId, priorityFlag,
                scheduleDeliveryTime, validityPeriod, registeredDelivery
                        .value(), replaceIfPresent, dataCoding.value(),
                smDefaultMsgId, shortMessage);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendSubmitSmResp(java.io.OutputStream, int, java.lang.String)
     */
    public byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId) throws PDUStringException, IOException {
        byte[] b = pduComposer.submitSmResp(sequenceNumber, messageId);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
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
     * @see org.jsmpp.PDUSender#sendDeliverSm(java.io.OutputStream, int, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.TypeOfNumber, org.jsmpp.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.DataCoding, byte[])
     */
    public byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.deliverSm(sequenceNumber, serviceType,
                sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr,
                destAddrTon.value(), destAddrNpi.value(), destinationAddr,
                esmClass.value(), protocoId, priorityFlag, registeredDelivery
                        .value(), dataCoding.value(), shortMessage);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUSender#sendDeliverSmResp(java.io.OutputStream, int)
     */
    public byte[] sendDeliverSmResp(OutputStream os, int sequenceNumber)
            throws PDUStringException, IOException {
        byte[] b = pduComposer.deliverSmResp(sequenceNumber);
        synchronized (os) {
            os.write(b);
            os.flush();
        }
        return b;
    }
}
