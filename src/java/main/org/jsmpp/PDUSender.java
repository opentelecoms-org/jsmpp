package org.jsmpp;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.RegisteredDelivery;


public interface PDUSender {

    /**
     * Send only the PDU header.
     * 
     * @param os is the {@link OutputStream}
     * @param commandId is the SMPP command_id.
     * @param commandStatus is the SMPP command_status.
     * @param sequenceNumber is the SMPP sequence_number.
     * @return the composed bytes.
     * @throws IOException is an I/O error occur.
     */
    public byte[] sendHeader(OutputStream os, int commandId, int commandStatus,
            int sequenceNumber) throws IOException;

    /**
     * Send bind command.
     * 
     * @param os is the {@link OutputStream}
     * @param bindType is the bind type that determine the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @param password is the password.
     * @param systemType is the system_type.
     * @param interfaceVersion is the interface_version.
     * @param addrTon is the addr_ton.
     * @param addrNpi is the addr_npi.
     * @param addressRange is the address_range.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constaint found.
     * @throws IOException if an I/O error occur.
     */
    public byte[] sendBind(OutputStream os, BindType bindType,
            int sequenceNumber, String systemId, String password,
            String systemType, InterfaceVersion interfaceVersion,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws PDUStringException, IOException;

    /**
     * Send bind response.
     * 
     * @param os is the {@link OutputStream}.
     * @param commandId is the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @return
     * @throws PDUStringException
     * @throws IOException
     */
    public byte[] sendBindResp(OutputStream os, int commandId,
            int sequenceNumber, String systemId) throws PDUStringException,
            IOException;

    public byte[] sendUnbind(OutputStream os, int sequenceNumber)
            throws IOException;

    public byte[] sendGenericNack(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException;

    public byte[] sendUnbindResp(OutputStream os, int commandStatus,
            int sequenceNumber) throws IOException;

    public byte[] sendEnquireLink(OutputStream out, int sequenceNumber)
            throws IOException;

    public byte[] sendEnquireLinkResp(OutputStream os, int sequenceNumber)
            throws IOException;

    public byte[] sendSubmitSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte replaceIfPresent, DataCoding dataCoding, byte smDefaultMsgId,
            byte[] shortMessage) throws PDUStringException, IOException;

    public byte[] sendSubmitSmResp(OutputStream os, int sequenceNumber,
            String messageId) throws PDUStringException, IOException;

    public byte[] sendQuerySm(OutputStream os, int sequenceNumber,
            String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
            throws PDUStringException, IOException;

    public byte[] sendQuerySmResp(OutputStream os, int sequenceNumber,
            String messageId, String finalDate, MessageState messageState,
            byte errorCode) throws PDUStringException, IOException;

    public byte[] sendDeliverSm(OutputStream os, int sequenceNumber,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocoId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage)
            throws PDUStringException, IOException;

    public byte[] sendDeliverSmResp(OutputStream os, int sequenceNumber)
            throws PDUStringException, IOException;

}