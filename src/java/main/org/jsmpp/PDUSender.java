package org.jsmpp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.session.QuerySmResult;
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
public class PDUSender {
    static final Logger logger = LoggerFactory.getLogger(PDUSender.class);

    private final PDUComposer pduComposer;
    private final OutputStream out;

    public PDUSender(OutputStream out) {
        this(out, new PDUComposer());
    }

    public PDUSender(OutputStream out, PDUComposer pduComposer) {
        this.pduComposer = pduComposer;
        this.out = new BufferedOutputStream(out);
    }

    public void sendHeader(int commandId, int commandStatus, int sequenceNumber) {
        write(pduComposer.composeHeader(commandId, commandStatus, sequenceNumber));
    }

    public void sendBind(BindType bindType, int sequenceNumber, String systemId, String password, String systemType, InterfaceVersion interfaceVersion, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange) throws PDUStringException {
        write(pduComposer.bind(bindType.commandId(), sequenceNumber, systemId, password, systemType, interfaceVersion.value(), addrTon.value(), addrNpi.value(), addressRange));
    }

    public void sendBindResp(int commandId, int sequenceNumber, String systemId) throws PDUStringException {
        write(pduComposer.bindResp(commandId, sequenceNumber, systemId));
    }

    public void sendUnbind(int sequenceNumber) {
        write(pduComposer.unbind(sequenceNumber));
    }

    public void sendGenericNack(int commandStatus, int sequenceNumber) {
        write(pduComposer.genericNack(commandStatus, sequenceNumber));
    }

    public void sendUnbindResp(int commandStatus, int sequenceNumber) {
        write(pduComposer.unbindResp(commandStatus, sequenceNumber));
    }

    public void sendEnquireLink(int sequenceNumber) {
        write(pduComposer.enquireLink(sequenceNumber));
    }

    public void sendEnquireLinkResp(int sequenceNumber) {
        write(pduComposer.enquireLinkResp(sequenceNumber));
    }

    public void sendSubmitSm(int sequenceNumber, String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException {
        write(pduComposer.submitSm(sequenceNumber, serviceType, sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr, destAddrTon.value(), destAddrNpi.value(), destinationAddr, esmClass.value(), protocoId, priorityFlag, scheduleDeliveryTime, validityPeriod, registeredDelivery.value(), replaceIfPresent, dataCoding.value(), smDefaultMsgId, shortMessage, params));
    }

    public void sendSubmitSmResp(int sequenceNumber, String messageId) throws PDUStringException {
        write(pduComposer.submitSmResp(sequenceNumber, messageId));
    }

    public void sendQuerySm(int sequenceNumber, String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException {
        write(pduComposer.querySm(sequenceNumber, messageId, sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr));
    }

    public void sendQuerySmResp(int sequenceNumber, String messageId, String finalDate, MessageState messageState, byte errorCode) throws PDUStringException {
        write(pduComposer.querySmResp(sequenceNumber, messageId, finalDate, messageState.value(), errorCode));
    }

    public void sendQuerySmResp(QuerySm querySm, QuerySmResult res) throws PDUStringException {
        sendQuerySmResp(querySm.getSequenceNumber(), querySm.getMessageId(), res.getFinalDate(), res.getMessageState(), res.getErrorCode());
    }

    public void sendDeliverSm(int sequenceNumber, String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, RegisteredDelivery registeredDelivery, DataCoding dataCoding, byte[] shortMessage, OptionalParameter... params) throws PDUStringException {
        write(pduComposer.deliverSm(sequenceNumber, serviceType, sourceAddrTon.value(), sourceAddrNpi.value(), sourceAddr, destAddrTon.value(), destAddrNpi.value(), destinationAddr, esmClass.value(), protocoId, priorityFlag, registeredDelivery.value(), dataCoding.value(), shortMessage, params));
    }

    public void sendDeliverSmResp(int sequenceNumber) {
        write(pduComposer.deliverSmResp(sequenceNumber));
    }

    private void write(byte bytes[]) {
        try {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
