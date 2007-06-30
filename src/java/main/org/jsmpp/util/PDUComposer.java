package org.jsmpp.util;

import org.jsmpp.PDUStringException;

/**
 * This class contains utility to compose SMPP PDU.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public interface PDUComposer {

    /**
     * Compose SMPP header.
     * 
     * @param commandId is the command_id.
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     */
    public byte[] composeHeader(int commandId, int commandStatus,
            int sequenceNumber);

    /**
     * Compose the SMPP bind command.
     * 
     * @param commandId is the command_id.
     * @param sequenceNumber is the sequnce_number.
     * @param systemId is the system_id.
     * @param password is the password.
     * @param systemType is the system_type.
     * @param interfaceVersion is the interface_version.
     * @param addrTon is the addr_ton.
     * @param addrNpi is the addr_npi.
     * @param addressRange is the address_range.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] bind(int commandId, int sequenceNumber, String systemId,
            String password, String systemType, byte interfaceVersion,
            byte addrTon, byte addrNpi, String addressRange)
            throws PDUStringException;

    /**
     * Compose the bind response command..
     * 
     * @param commandId is the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId)
            throws PDUStringException;

    /**
     * Compose the bind reponse command.
     * 
     * @param commandId is the command_id.
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @param scInterfaceVersion is the sc_interface_version.
     * @return the composed bytes.
     * @throws PDUStringException if there is an ivalid string constraint found.
     */
    public byte[] bindResp(int commandId, int sequenceNumber, String systemId,
            byte scInterfaceVersion) throws PDUStringException;

    /**
     * Compose the unbind command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     */
    public byte[] unbind(int sequenceNumber);

    /**
     * Compose the unbind reponse command.
     * 
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     */
    public byte[] unbindResp(int commandStatus, int sequenceNumber);

    /**
     * Compose the outbind command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @param systemId is the system_id.
     * @param password is the password.
     * @return the composed bytes.
     * @throws PDUStringException if the is an invalid string constraint found.
     */
    public byte[] outbind(int sequenceNumber, String systemId, String password)
            throws PDUStringException;

    // ENQUIRE_LINK OPERATION
    /**
     * Compose the enquire link command.
     * 
     * @param sequenceNumber is the sequence number.
     * @return the composed bytes.
     */
    public byte[] enquireLink(int sequenceNumber);

    /**
     * Compose the enquire link response command.
     * 
     * @param sequenceNumber is the sequence number.
     * @return the composed bytes.
     */
    public byte[] enquireLinkResp(int sequenceNumber);

    // GENERIC_NACK OPERATION
    /**
     * Compose the generic nack command.
     * 
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     */
    public byte[] genericNack(int commandStatus, int sequenceNumber);

    // SUBMIT_SM OPERATION
    /**
     * Compose the submit sm command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @param serviceType is the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_addr.
     * @param esmClass is the esm_class.
     * @param protocoId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param scheduleDeliveryTime is the schedule_delivery_time.
     * @param validityPeriod is the validity_period.
     * @param registeredDelivery is the registered_delivery.
     * @param replaceIfPresent is the replace_if_present.
     * @param dataCoding is the data_coding.
     * @param smDefaultMsgId os the sm_default_msg_id.
     * @param shortMessage is the short_message.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string
     *         constraingtfound.
     */
    public byte[] submitSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocoId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            byte registeredDelivery, byte replaceIfPresent, byte dataCoding,
            byte smDefaultMsgId, byte[] shortMessage) throws PDUStringException;

    /**
     * Compose the submit sm response command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] submitSmResp(int sequenceNumber, String messageId)
            throws PDUStringException;

    // QUERY_SM OPERATION
    /**
     * Compose the querr sm command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] querySm(int sequenceNumber, String messageId,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr)
            throws PDUStringException;

    /**
     * Compose the query sm reponse command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @param messageId is the message_id.
     * @param finalDate is the final_date.
     * @param messageState is the message_state.
     * @param errorCode is the error_code.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] querySmResp(int sequenceNumber, String messageId,
            String finalDate, byte messageState, byte errorCode)
            throws PDUStringException;

    // DELIVER_SM OPERATION
    /**
     * Compose the deliver sm command.
     * 
     * @param sequenceNumber is the sequence number.
     * @param serviceType is the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_ton.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is thr destination_addr.
     * @param esmClass is the esm_class.
     * @param protocolId is the protocol_id.
     * @param priorityFlag is the priority_flag.
     * @param registeredDelivery is the registered_delivery.
     * @param dataCoding is the data_coding.
     * @param shortMessage is the short_message.
     * @return the composed bytes.
     * @throws PDUStringException if there is an invalid string constraint
     *         found.
     */
    public byte[] deliverSm(int sequenceNumber, String serviceType,
            byte sourceAddrTon, byte sourceAddrNpi, String sourceAddr,
            byte destAddrTon, byte destAddrNpi, String destinationAddr,
            byte esmClass, byte protocolId, byte priorityFlag,
            byte registeredDelivery, byte dataCoding, byte[] shortMessage)
            throws PDUStringException;

    /**
     * Compose the deliver sm reponse command.
     * 
     * @param sequenceNumber is the sequence_number.
     * @return the composed bytes.
     */
    public byte[] deliverSmResp(int sequenceNumber);

}
