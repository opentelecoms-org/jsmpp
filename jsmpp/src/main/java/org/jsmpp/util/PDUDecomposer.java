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
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.BroadcastSmResp;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.CancelBroadcastSmResp;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.CancelSmResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DataSmResp;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.GenericNack;
import org.jsmpp.bean.Outbind;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QueryBroadcastSmResp;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.ReplaceSmResp;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.Unbind;
import org.jsmpp.bean.UnbindResp;

/**
 * This class provides an interface to decompose SMPP PDU bytes
 * into a SMPP command objects.
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
     * @param b The header bytes.
     * @return the header ({@link Command}) object.
     */
    Command header(byte[] b);

    /**
     * Decompose the SMPP PDU bind command.
     *
     * @param b The bind PDU bytes.
     * @return The bind command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    Bind bind(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU bind response command.
     *
     * @param b The bind_resp PDU bytes.
     * @return The bind response command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    BindResp bindResp(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU unbind command.
     *
     * @param b The unbind PDU bytes.
     * @return the unbind command object.
     */
    Unbind unbind(byte[] b);

    /**
     * Decompose the SMPP PDU unbind response command.
     *
     * @param b The unbind_resp PDU bytes.
     * @return the unbind response command object.
     */
    UnbindResp unbindResp(byte[] b);

    /**
     * Decompose the SMPP PDU outbind command.
     *
     * @param b The outbind PDU bytes.
     * @return the outbind command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    Outbind outbind(byte[] b) throws PDUStringException;

    // ENQUIRE_LINK OPERATION
    /**
     * Decompose the SMPP PDU enquire link command.
     *
     * @param b The enquiry_link PDU bytes.
     * @return the enquire link command object.
     */
    EnquireLink enquireLink(byte[] b);

    /**
     * Decompose the SMPP PDU enquire link response command.
     *
     * @param b The enquiry_link_resp PDU bytes.
     * @return the enquire link response command object.
     */
    EnquireLinkResp enquireLinkResp(byte[] b);

    /**
     * Decompose the SMPP PDU generic nack command.
     *
     * @param b The generic_nack PDU bytes.
     * @return The generic nack command object.
     */
    GenericNack genericNack(byte[] b);

    /**
     * Decompose the SMPP PDU submit short message command.
     *
     * @param b The submit_sm PDU bytes.
     * @return The submit short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    SubmitSm submitSm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU submit short message response command.
     *
     * @param b The submit_sm_resp PDU bytes.
     * @return The submit short message response command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    SubmitSmResp submitSmResp(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU query short message command.
     *
     * @param b The query_sm PDU bytes.
     * @return The query short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    QuerySm querySm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU query short message response command.
     *
     * @param b The query_sm_resp PDU bytes.
     * @return The query short message response command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    QuerySmResp querySmResp(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU deliver short message command.
     *
     * @param b The deliver_sm PDU bytes.
     * @return The deliver short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    DeliverSm deliverSm(byte[] b) throws PDUStringException;

    /**
     * Decompose the SMPP PDU deliver short message response command.
     *
     * @param b The deliver_sm_resp PDU bytes.
     * @return The deliver short message response command object.
     */
    DeliverSmResp deliverSmResp(byte[] b);

    /**
     * Decompose the SMPP PDU delivery receipt content.
     *
     * @param data the delivery receipt string.
     * @return The delivery receipt object.
     * @throws InvalidDeliveryReceiptException throw if the data is invalid, so
     *         it can be parsed.
     */
    DeliveryReceipt deliveryReceipt(String data)
            throws InvalidDeliveryReceiptException;

    /**
     * Decompose the SMPP PDU delivery receipt content.
     *
     * @param data The delivery receipt bytes
     * @return The delivery receipt object.
     * @throws InvalidDeliveryReceiptException throw if the data is invalid, so
     *         it can be parsed.
     */
    DeliveryReceipt deliveryReceipt(byte[] data)
            throws InvalidDeliveryReceiptException;

    /**
     * Decompose the SMPP PDU data short message command.
     *
     * @param data The data_sm PDU bytes.
     * @return The data short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    DataSm dataSm(byte[] data) throws PDUStringException;

    /**
     * Decompose the SMPP PDU data short message response command.
     *
     * @param data The data_sm_resp PDU bytes.
     * @return The data short message response command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    DataSmResp dataSmResp(byte[] data) throws PDUStringException;

    /**
     * Decompose the SMPP PDU cancel short message command.
     *
     * @param data The cancel_sm PDU bytes.
     * @return The cancel short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    CancelSm cancelSm(byte[] data) throws PDUStringException;

    /**
     * Decompose the SMPP PDU cancel short message response command.
     *
     * @param data The cancel_sm_resp PDU bytes.
     * @return The cancel short message response command object.
     */
    CancelSmResp cancelSmResp(byte[] data);

    /**
     * Decompose the SMPP PDU submit multi command.
     *
     * @param data The submit_multi PDU bytes.
     * @return The submit multi command object.
     * @throws PDUStringException if there is an invalid string constraint found
     * @throws InvalidNumberOfDestinationsException if there is an invalid number of destinations found
     */
    SubmitMulti submitMulti(byte[] data) throws PDUStringException,
            InvalidNumberOfDestinationsException;

    /**
     * Decompose the SMPP PDU submit multi response command.
     *
     * @param data The submit_multi_resp PDU bytes.
     * @return The submit multi response command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    SubmitMultiResp submitMultiResp(byte[] data) throws PDUStringException;

    /**
     * Decompose the SMPP PDU replace short message command.
     *
     * @param data The replace_sm PDU bytes.
     * @return the replace short message command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    ReplaceSm replaceSm(byte[] data) throws PDUStringException;

    /**
     * Decompose the SMPP PDU replace short message command.
     *
     * @param data The replace_sm_resp PDU bytes.
     * @return The replace short message response command object.
     */
    ReplaceSmResp replaceSmResp(byte[] data);

    /**
     * Decompose the SMPP PDU alert notification command.
     *
     * @param data The alert_notification PDU bytes.
     * @return the alert notification command object.
     * @throws PDUStringException if there is an invalid string constraint found
     */
    AlertNotification alertNotification(byte[] data) throws PDUStringException;

    BroadcastSm broadcastSm(byte[] data) throws PDUStringException;

    BroadcastSmResp broadcastSmResp(byte[] data) throws PDUStringException;

    CancelBroadcastSm cancelBroadcastSm(byte[] data) throws PDUStringException;

    CancelBroadcastSmResp cancelBroadcastSmResp(byte[] data) throws PDUStringException;

    QueryBroadcastSm queryBroadcastSm(byte[] data) throws PDUStringException;

    QueryBroadcastSmResp queryBroadcastSmResp(byte[] data) throws PDUStringException;
}
