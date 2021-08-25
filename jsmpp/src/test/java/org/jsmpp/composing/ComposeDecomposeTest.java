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
package org.jsmpp.composing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.PDUSender;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DataSmResp;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.GenericNack;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.testng.annotations.Test;

/**
 * Simulating sending and receive data from client to server (vice versa) by
 * simple compose and decomposing the PDU.
 *
 * @author uudashr
 * @author pmoerenhout
 *
 */
public class ComposeDecomposeTest {

    private PDUSender pduSender = new DefaultPDUSender();
    private DefaultPDUReader pduReader = new DefaultPDUReader();
    private PDUDecomposer decomposer = new DefaultDecomposer();

    /**
     * Test the generick_nack composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendGenericNack() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        pduSender.sendGenericNack(out, SMPPConstant.STAT_ESME_RINVBNDSTS, 10);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_GENERIC_NACK, "Unexpected command id");
        assertEquals(header.getCommandLength(), 16, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_RINVBNDSTS, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 10, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        GenericNack nack = decomposer.genericNack(pdu);
        assertNotNull(nack);
    }

    /**
     * Test the submit_sm composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendSubmitSm50() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter billingIdentification = new OptionalParameter.Billing_identification(new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78});

        pduSender.sendSubmitSm(out, 1234, "CMT",
            TypeOfNumber.ABBREVIATED, NumberingPlanIndicator.UNKNOWN, "123",
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "316123456789",
            new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.DEFAULT),
            (byte)0x00, (byte)0x00, null, null,
            (new RegisteredDelivery()).setSMSCDeliveryReceipt(SMSCDeliveryReceipt.DEFAULT),
            (byte)0x00, DataCodings.ZERO, (byte)0x00, new byte[]{}, billingIdentification);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_SUBMIT_SM, "Unexpected command id");
        assertEquals(header.getCommandLength(), 61, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 1234, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        SubmitSm submitSm = decomposer.submitSm(pdu);
        assertEquals(submitSm.getSourceAddr(), "123", "Unexpected source address");
        assertEquals(submitSm.getDestAddress(), "316123456789", "Unexpected destination address");
        assertEquals(submitSm.getEsmClass(), (byte)0x00, "Unexpected ESM class");
        assertEquals(submitSm.getDataCoding(), (byte)0x00, "Unexpected data coding");
        assertEquals(submitSm.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.Billing_identification decomposedBillingIdentification = OptionalParameters.get(OptionalParameter.Billing_identification.class, submitSm.getOptionalParameters());
        assertEquals(decomposedBillingIdentification.tag, OptionalParameter.Tag.BILLING_IDENTIFICATION.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedBillingIdentification.getValue(), new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78}, "Unexpected optional parameters value");
    }

    /**
     * Test the submit_sm_resp for SMPP 3.4 composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendSubmitSmResp34() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        pduSender.sendSubmitSmResp(out, 2345, "41fbf22d-fb3b-49ac-9c5e-71b762158808", null);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_SUBMIT_SM_RESP, "Unexpected command id");
        assertEquals(header.getCommandLength(), 53, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 2345, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        SubmitSmResp submitSmResp = decomposer.submitSmResp(pdu);
        assertEquals(submitSmResp.getMessageId(), "41fbf22d-fb3b-49ac-9c5e-71b762158808", "Unexpected message id");
        assertEquals(submitSmResp.getOptionalParameters().length, 0, "Unexpected optional parameters length");
    }

    /**
     * Test the submit_sm_resp for SMPP 5.0 composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendSubmitSmResp50() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter additionalStatusInfoText = new OptionalParameter.Additional_status_info_text("Oops");

        pduSender.sendSubmitSmResp(out, 2345, "41fbf22d-fb3b-49ac-9c5e-71b762158808", additionalStatusInfoText);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_SUBMIT_SM_RESP, "Unexpected command id");
        assertEquals(header.getCommandLength(), 62, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 2345, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        SubmitSmResp submitSmResp = decomposer.submitSmResp(pdu);
        assertEquals(submitSmResp.getMessageId(), "41fbf22d-fb3b-49ac-9c5e-71b762158808", "Unexpected message id");
        assertEquals(submitSmResp.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.Additional_status_info_text decomposedAdditionalStatusInfoText = OptionalParameters.get(OptionalParameter.Additional_status_info_text.class, submitSmResp.getOptionalParameters());
        assertEquals(decomposedAdditionalStatusInfoText.tag, OptionalParameter.Tag.ADDITIONAL_STATUS_INFO_TEXT.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedAdditionalStatusInfoText.getValueAsString(), "Oops", "Unexpected optional parameters value");
    }

    /**
     * Test the data_sm composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendDataSm() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter messagePayload = new OptionalParameter.Message_payload(new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78});

        pduSender.sendDataSm(out, 1234, "CMT",
            TypeOfNumber.ABBREVIATED, NumberingPlanIndicator.UNKNOWN, "567",
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, "316123456789",
            new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.DEFAULT),
            (new RegisteredDelivery()).setSMSCDeliveryReceipt(SMSCDeliveryReceipt.DEFAULT),
            DataCodings.ZERO, messagePayload);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_DATA_SM, "Unexpected command id");
        assertEquals(header.getCommandLength(), 54, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 1234, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        DataSm dataSm = decomposer.dataSm(pdu);
        assertEquals(dataSm.getSourceAddr(), "567", "Unexpected source address");
        assertEquals(dataSm.getDestAddress(), "316123456789", "Unexpected destination address");
        assertEquals(dataSm.getEsmClass(), (byte)0x00, "Unexpected ESM class");
        assertEquals(dataSm.getDataCoding(), (byte)0x00, "Unexpected data coding");
        assertEquals(dataSm.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.Message_payload decomposedMessagePayload = OptionalParameters.get(OptionalParameter.Message_payload.class, dataSm.getOptionalParameters());
        assertEquals(decomposedMessagePayload.tag, OptionalParameter.Tag.MESSAGE_PAYLOAD.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedMessagePayload.getValue(), new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78}, "Unexpected optional parameters value");
    }

    /**
     * Test the data_sm_resp composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendDataSmResp() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter additionalStatusInfoText = new OptionalParameter.Additional_status_info_text(new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78});

        pduSender.sendDataSmResp(out, 1234, "41fbf22d-fb3b-49ac-9c5e-71b762158808", additionalStatusInfoText);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_DATA_SM_RESP, "Unexpected command id");
        assertEquals(header.getCommandLength(), 63, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 1234, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        DataSmResp dataSmResp = decomposer.dataSmResp(pdu);
        assertEquals(dataSmResp.getMessageId(), "41fbf22d-fb3b-49ac-9c5e-71b762158808", "Unexpected message id");
        assertEquals(dataSmResp.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.Additional_status_info_text decomposedAdditionalStatusInfoText = OptionalParameters.get(OptionalParameter.Additional_status_info_text.class, dataSmResp.getOptionalParameters());
        assertEquals(decomposedAdditionalStatusInfoText.tag, OptionalParameter.Tag.ADDITIONAL_STATUS_INFO_TEXT.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedAdditionalStatusInfoText.getValue(), new byte[]{ 0x00, 0x12, 0x34, 0x45, 0x67, 0x78}, "Unexpected optional parameters value");
    }

    /**
     * Test the data_sm_resp composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendEnquireLink() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        pduSender.sendEnquireLink(out, 1234);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_ENQUIRE_LINK, "Unexpected command id");
        assertEquals(header.getCommandLength(), 16, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 1234, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        EnquireLink enquireLink = decomposer.enquireLink(pdu);
        assertEquals(enquireLink.getSequenceNumber(), 1234, "Unexpected sequence number");
    }

    /**
     * Test the broadcast_sm composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendBroadcastSm() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter broadcastContentType = new OptionalParameter.Broadcast_content_type(new byte[]{ 0x01, 0x00, 0x010});

        pduSender.sendBroadcastSm(out, 98761, "CBS",
            TypeOfNumber.ABBREVIATED, NumberingPlanIndicator.UNKNOWN, "123",
            "d7908928-58cc-4a02-a157-189f38484a9c",
            (byte)0x00, null, null, ReplaceIfPresentFlag.DEFAULT,
            DataCodings.ZERO, (byte)0x00, broadcastContentType);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_BROADCAST_SM, "Unexpected command id");
        assertEquals(header.getCommandLength(), 76, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 98761, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        BroadcastSm broadcastSm = decomposer.broadcastSm(pdu);
        assertEquals(broadcastSm.getMessageId(), "d7908928-58cc-4a02-a157-189f38484a9c", "Unexpected message id");
        assertEquals(broadcastSm.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.Broadcast_content_type decomposedBroadcastContentType =
            OptionalParameters.get(OptionalParameter.Broadcast_content_type.class, broadcastSm.getOptionalParameters());
        assertEquals(decomposedBroadcastContentType.tag, OptionalParameter.Tag.BROADCAST_CONTENT_TYPE.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedBroadcastContentType.getValue(), new byte[]{ 0x01, 0x00, 0x010}, "Unexpected optional parameters value");
    }

    /**
     * Test the cancel_broadcast_sm composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendCancelBroadcastSm() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter userMessageReference = new OptionalParameter.User_message_reference((short)0xabcd);

        pduSender.sendCancelBroadcastSm(out, 34567, "CBS",
            "52e1dfd7-f7f6-41a5-a8c5-12a6219be0a8",
            TypeOfNumber.ABBREVIATED, NumberingPlanIndicator.UNKNOWN, "123",
            userMessageReference);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_CANCEL_BROADCAST_SM, "Unexpected command id");
        assertEquals(header.getCommandLength(), 69, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 34567, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        CancelBroadcastSm cancelBroadcastSm = decomposer.cancelBroadcastSm(pdu);
        assertEquals(cancelBroadcastSm.getMessageId(), "52e1dfd7-f7f6-41a5-a8c5-12a6219be0a8", "Unexpected message id");
        assertEquals(cancelBroadcastSm.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.User_message_reference decomposedUserMessageReference =
            OptionalParameters.get(OptionalParameter.User_message_reference.class, cancelBroadcastSm.getOptionalParameters());
        assertEquals(decomposedUserMessageReference.tag, OptionalParameter.Tag.USER_MESSAGE_REFERENCE.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedUserMessageReference.getValue(), (short)0xabcd, "Unexpected optional parameters value");
    }

    /**
     * Test the query_broadcast_sm composed PDU can decomposed properly.
     *
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendQueryBroadcastSm() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OptionalParameter userMessageReference = new OptionalParameter.User_message_reference((short)0x1234);

        pduSender.sendQueryBroadcastSm(out, 56789, "aa494ed3-3bda-4e96-9651-d8ed69de00b8",
            TypeOfNumber.ABBREVIATED, NumberingPlanIndicator.UNKNOWN, "888",
            userMessageReference);

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_QUERY_BROADCAST_SM, "Unexpected command id");
        assertEquals(header.getCommandLength(), 65, "Unexpected command length");
        assertEquals(header.getCommandStatus(), SMPPConstant.STAT_ESME_ROK, "Unexpected command status");
        assertEquals(header.getSequenceNumber(), 56789, "Unexpected sequence number");

        byte[] pdu = pduReader.readPDU(in, header);
        QueryBroadcastSm queryBroadcastSm = decomposer.queryBroadcastSm(pdu);
        assertEquals(queryBroadcastSm.getMessageId(), "aa494ed3-3bda-4e96-9651-d8ed69de00b8", "Unexpected message id");
        assertEquals(queryBroadcastSm.getOptionalParameters().length, 1, "Unexpected optional parameters length");

        OptionalParameter.User_message_reference decomposedUserMessageReference =
            OptionalParameters.get(OptionalParameter.User_message_reference.class, queryBroadcastSm.getOptionalParameters());
        assertEquals(decomposedUserMessageReference.tag, OptionalParameter.Tag.USER_MESSAGE_REFERENCE.code(), "Unexpected optional parameters tag");
        assertEquals(decomposedUserMessageReference.getValue(), (short)0x1234, "Unexpected optional parameters value");
    }
}
