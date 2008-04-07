package org.jsmpp;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.util.MessageId;
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

    public void sendSubmitSm(SubmitSm submitSm) throws PDUStringException {
        write(pduComposer.submitSm(submitSm));
    }

    public void sendSubmitSmResp(SubmitSm submitSm, MessageId messageId) throws PDUStringException {
        sendSubmitSmResp(submitSm.getSequenceNumber(), messageId.getValue());
    }

    public void sendSubmitSmResp(int sequenceNumber, String messageId) throws PDUStringException {
        write(pduComposer.submitSmResp(sequenceNumber, messageId));
    }

    public void sendQuerySm(QuerySm querySm) throws PDUStringException {
        write(pduComposer.querySm(querySm.getSequenceNumber(), querySm.getMessageId(), querySm.getSourceAddrTon().value(), querySm.getSourceAddrNpi().value(), querySm.getSourceAddr()));
    }

    public void sendQuerySmResp(int sequenceNumber, String messageId, String finalDate, MessageState messageState, byte errorCode) throws PDUStringException {
        write(pduComposer.querySmResp(sequenceNumber, messageId, finalDate, messageState.value(), errorCode));
    }

    public void sendQuerySmResp(QuerySm querySm, QuerySmResult res) throws PDUStringException {
        sendQuerySmResp(querySm.getSequenceNumber(), querySm.getMessageId(), res.getFinalDate(), res.getMessageState(), res.getErrorCode());
    }

    public void sendDeliverSm(DeliverSm deliverSm) {
        write(pduComposer.deliverSm(deliverSm.getSequenceNumber(), deliverSm.getServiceType(), deliverSm.getSourceAddrTon().value(), deliverSm.getSourceAddrNpi().value(), deliverSm.getSourceAddr(), deliverSm.getDestAddrTon().value(), deliverSm.getDestAddrNpi().value(), deliverSm.getDestAddress(), deliverSm.getEsmClass().value(), deliverSm.getProtocolId(), deliverSm.getPriorityFlag(), deliverSm.getRegisteredDelivery().value(), deliverSm.getDataCoding().value(), deliverSm.getShortMessage(), deliverSm.getOptionalParameters()));
    }

    public void sendDeliverSmResp(DeliverSm deliverSm) {
        sendDeliverSmResp(deliverSm.getSequenceNumber());
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
