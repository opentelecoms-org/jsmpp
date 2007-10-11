package org.jsmpp.util;

import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.GenericNack;
import org.jsmpp.bean.Outbind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.Unbind;
import org.jsmpp.bean.UnbindResp;

/**
 * This class is provide interface to decompose SMPP PDU bytes form into SMPP
 * command object.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public interface PDUDecomposer {

    /**
     * Decompose the header only SMPP PDU command.
     * 
     * @param b is the PDU.
     * @return the header ( {@link Command} ) object.
     */
    public Command header(byte[] b);

    // BIND OPERATION
    /**
     * Decompose the SMPP PDU bind command.
     * 
     * @param b is the PDU.
     * @return the bind command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public Bind bind(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU bind response command.
     * 
     * @param b is the PDU.
     * @return the bind response command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public BindResp bindResp(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU unbind command.
     * 
     * @param b is the PDU.
     * @return the unbind command object.
     */
    public Unbind unbind(byte[] b);

    /**
     * Decompose the SMPP PDU unbind response command.
     * 
     * @param b is the PDU.
     * @return the unbind response command object.
     */
    public UnbindResp unbindResp(byte[] b);

    /**
     * Decompose the SMPP PDU outbind command.
     * 
     * @param b is the PDU.
     * @return the outbind command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public Outbind outbind(byte[] b) throws PDUStringException;

    // ENQUIRE_LINK OPERATION
    /**
     * Decompose the SMPP PDU enquire link command.
     * 
     * @param b is the PDU.
     * @return the enquire link command object.
     */
    public EnquireLink enquireLink(byte[] b);

    /**
     * Decompose the SMPP PDU enquire link response command.
     * 
     * @param b is the PDU.
     * @return the enquire link response command object.
     */
    public EnquireLinkResp enquireLinkResp(byte[] b);

    // GENERICK_NACK OPERATION
    /**
     * Decompose the SMPP PDU generic nack command.
     * 
     * @param b is the PDU.
     * @return the generic nack command object.
     */
    public GenericNack genericNack(byte[] b);

    // SUBMIT_SM OPERATION
    /**
     * Decompose the SMPP PDU submit short message command.
     * 
     * @param b is the PDU.
     * @return the submit short message command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public SubmitSm submitSm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU submit short message response command.
     * 
     * @param b is the PDU.
     * @return the submit short message response command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public SubmitSmResp submitSmResp(byte[] b) throws PDUStringException;

    // QUERY_SM OPERATION
    /**
     * Decompose the SMPP PDU query short message command.
     * 
     * @param b is the PDU.
     * @return the query short message command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public QuerySm querySm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU query short message reponse command.
     * 
     * @param b is the PDU.
     * @return the query short message response command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public QuerySmResp querySmResp(byte[] b) throws PDUStringException;

    // DELIVER_SM OPERATION
    /**
     * Decompose the SMPP PDU deliver short message command.
     * 
     * @param b is the PDU.
     * @return the deliver short message command object.
     * @throws PDUStringException throw if there is an invalid constraint of
     *         string parameter found.
     */
    public DeliverSm deliverSm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU deliver short message response command.
     * 
     * @param b is the PDU.
     * @return the deliver short message response command object.
     */
    public DeliverSmResp deliverSmResp(byte[] b);

    /**
     * Decompose the SMPP PDU delivery receipt content.
     * 
     * @param data is the content.
     * @return the delivery receipt object.
     * @throws InvalidDeliveryReceiptException throw if the data is invalid, so
     *         it can be parsed.
     */
    public DeliveryReceipt deliveryReceipt(String data)
            throws InvalidDeliveryReceiptException;

    /**
     * Decompose the SMPP PDU delivery receipt content.
     * 
     * @param data is the content.
     * @return the delivert receipt object.
     * @throws InvalidDeliveryReceiptException throw if the data is invalid, so
     *         it can be parsed.
     */
    public DeliveryReceipt deliveryReceipt(byte[] data)
            throws InvalidDeliveryReceiptException;
}
