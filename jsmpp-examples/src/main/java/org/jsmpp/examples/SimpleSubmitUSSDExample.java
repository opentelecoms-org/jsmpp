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
package org.jsmpp.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pmoerenhout
 */
public class SimpleSubmitUSSDExample {
  private static final Logger log = LoggerFactory.getLogger(SimpleSubmitUSSDExample.class);
  private static final MessageIDGenerator MESSAGE_ID_GENERATOR = new RandomMessageIDGenerator();

  public static void main(String[] args) {

    final CountDownLatch latch = new CountDownLatch(1);

    SMPPSession session = new SMPPSession();

    // Set listener to receive deliver_sm with USSD
    session.setMessageReceiverListener(new MessageReceiverListener() {
      @Override
      public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
          // delivery receipt
          log.info("Received unexpected deliver receipt from {}", deliverSm.getSourceAddr());
        } else {
          // USSD short message
          String serviceType = deliverSm.getServiceType();
          log.info("Receiving {} USSD message: {}", serviceType, new String(deliverSm.getShortMessage(), StandardCharsets.US_ASCII));
          for (OptionalParameter optionalParameter : deliverSm.getOptionalParameters()) {
            String tagHex = String.format("%04x", optionalParameter.tag);
            if (optionalParameter instanceof OptionalParameter.Null) {
              log.info("Optional parameter {}: null", tagHex);
            } else if (optionalParameter instanceof OptionalParameter.Byte) {
              OptionalParameter.Byte optionalByte = (OptionalParameter.Byte) optionalParameter;
              log.info("Optional parameter {}: {}", tagHex, (optionalByte.getValue() & 0xff));
            } else if (optionalParameter instanceof OptionalParameter.Short) {
              OptionalParameter.Short optionalShort = (OptionalParameter.Short) optionalParameter;
              log.info("Optional parameter {}: {}", tagHex, (optionalShort.getValue() & 0xffff));
            } else if (optionalParameter instanceof OptionalParameter.Int) {
              OptionalParameter.Int optionalInt = (OptionalParameter.Int) optionalParameter;
              log.info("Optional parameter {}: {}", tagHex, optionalInt.getValue());
            } else if (optionalParameter instanceof OptionalParameter.COctetString) {
              OptionalParameter.COctetString cOctetString = (OptionalParameter.COctetString) optionalParameter;
              log.info("Optional parameter {}: {}", tagHex, cOctetString.getValueAsString());
            } else if (optionalParameter instanceof OptionalParameter.OctetString) {
              OptionalParameter.OctetString octetString = (OptionalParameter.OctetString) optionalParameter;
              log.info("Optional parameter {}: {}", tagHex, octetString.getValueAsString());
            }
          }
          latch.countDown();
        }
      }

      @Override
      public void onAcceptAlertNotification(AlertNotification alertNotification) {
        log.info("Received alert_notification");
      }

      @Override
      public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
        log.info("Received data_sm");
        return new DataSmResult(MESSAGE_ID_GENERATOR.newMessageId(), new OptionalParameter[]{});
      }
    });

    try {
      log.info("Connecting");
      String systemId = session.connectAndBind("localhost", 2775,
          new BindParameter(BindType.BIND_TRX, "SP", "password", "0", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
      log.info("Connected with USSD Message Centre with system id {}", systemId);

      String sourceAddress = "*111";
      String destinationAddress = "628176504657";

      try {
        // Service Type:
        // Service Type byte 0: B=BEGIN C=CONTINUE E=END A=ABORT F=CHARGEINDICATION
        // Service Type byte 1: R=REQUEST N=NOTIFY A=RESPONSE F=RELEASE
        // Example service_type BR = BEGIN REQUEST

        // Optional parameter Dest_bearer_type
        // 0x04 = USSD
        // Optional parameter Ussd_service_op
        // 0x02 = USSR Request
        // Optional parameter Dest_addr_subunit
        // 0x02 = Mobile Equipment

        String message = "1. First Menu Item\n2. Second Menu Item\n0. Help\n* Exit";

        SubmitSmResult beginRequestResult = session.submitShortMessage("BR",
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, sourceAddress,
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, destinationAddress,
            new ESMClass(), (byte) 0, (byte) 0, null, null,
            new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte) 0,
            new GeneralDataCoding(Alphabet.ALPHA_8_BIT), (byte) 0,
            message.getBytes(StandardCharsets.US_ASCII));

        String beginRequestMessageId = beginRequestResult.getMessageId();

        // Possible Optional Parameters:
        //  new OptionalParameter.Dest_bearer_type(USSD),
        //  new OptionalParameter.Dest_addr_subunit(MOBILE_EQUIPMENT),
        //  new OptionalParameter.Ussd_service_op((byte) 0x02));

        log.info("USSD BR message submitted, message_id is {}", beginRequestMessageId);

        // Wait for deliver_sm
        latch.await(60000, TimeUnit.MILLISECONDS);

        // End and Release the USSD session
        SubmitSmResult endReleaseResult = session.submitShortMessage("EF",
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, sourceAddress,
            TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.ISDN, destinationAddress,
            new ESMClass(), (byte) 0, (byte) 0, null, null,
            new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte) 0,
            new GeneralDataCoding(Alphabet.ALPHA_8_BIT), (byte) 0,
            "The weather is nice today.".getBytes(StandardCharsets.US_ASCII));

        String endReleaseMessageId = endReleaseResult.getMessageId();
        // Possible Optional Parameters:
        //  new OptionalParameter.Dest_bearer_type(USSD),
        //  new OptionalParameter.Dest_addr_subunit(MOBILE_EQUIPMENT),
        //  new OptionalParameter.Ussd_service_op((byte) 0x02));
        log.info("USSD EF message submitted, message_id is {}", endReleaseMessageId);
      } catch (InterruptedException e) {
        log.error("Interrupted", e);
        Thread.currentThread().interrupt();
      } catch (PDUException e) {
        // Invalid PDU parameter
        log.error("Invalid PDU parameter", e);
      } catch (ResponseTimeoutException e) {
        // Response timeout
        log.error("Response timeout", e);
      } catch (InvalidResponseException e) {
        // Invalid response
        log.error("Receive invalid response", e);
      } catch (NegativeResponseException e) {
        // Receiving negative response (non-zero command_status)
        log.error("Receive negative response, e");
      } catch (IOException e) {
        log.error("IO error occurred", e);
      }

      session.unbindAndClose();

    } catch (IOException e) {
      log.error("Failed connect and bind to host", e);
    }
  }

}
