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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.examples.util.Concatenation;
import org.jsmpp.examples.util.Gsm0338;
import org.jsmpp.examples.util.Ucs2;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example to show submit_sm with multiple segments (concatenation)
 *
 * @author Maciej Pigulski &lt;maciej.pigulski[at]gmail.com&gt;
 * @author Pim Moerenhout &lt;pim.moerenhout[at]gmail.com&gt;
 */
public class SubmitMultipartMultilangualExample {
  private static final Logger log = LoggerFactory.getLogger(SubmitMultipartMultilangualExample.class);

  private static final Random RANDOM = new Random();

  private static final Charset UCS2_CHARSET = StandardCharsets.UTF_16BE;
  private static final Charset ISO_LATIN_CHARSET = StandardCharsets.ISO_8859_1;

  private static final int MAX_SINGLE_MSG_CHAR_SIZE_7BIT = 160;
  private static final int MAX_SINGLE_MSG_CHAR_SIZE_UCS2 = 70;

  public static void main(String[] args) throws IOException, InterruptedException {
    new SubmitMultipartMultilangualExample().sendAndWait();
  }

  private void sendAndWait() throws IOException, InterruptedException {

    String sourceMsisdn = "1616";
    String destinationMsisdn = "666111222";
    // Use messageClass null to set a default message class
    MessageClass messageClass = MessageClass.CLASS1;
    String messageBody = "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies. Curabitur et ligula. Ut molestie a, ultricies porta urna.";
    boolean use16bitReference = false;

    // When using default alphabet, encoding is determined by the SMSC
    Charset smscDefaultCharset = ISO_LATIN_CHARSET;

    SMPPSession session = new SMPPSession();
    session.addSessionStateListener(new SessionStateListenerImpl());
    session.setMessageReceiverListener(new MessageReceiverListenerImpl());

    try {
      session.connectAndBind("localhost", 2775, new BindParameter(BindType.BIND_TRX, "smppclient", "password",
          "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
    } catch (IOException e) {
      throw new IOException("Failed connect and bind to host", e);
    }

    byte[][] messages;
    DataCoding dataCoding;
    ESMClass esmClass;
    if (Gsm0338.isBasicEncodeable(messageBody)) {
      // Use the SMSC default alphabet
      dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, messageClass);
      // In the GSM network it's send as GSM-7 bit septets packed. See if the characters fit...
      // (could also use messageBody.getBytes(GSM_CHARSET).length)
      if (Gsm0338.countSeptets(messageBody) > MAX_SINGLE_MSG_CHAR_SIZE_7BIT) {
        // No additional user data header expected, so with 8-bit reference concatenation leaves room for up to 153 septets.
        messages = Concatenation.splitGsm7bit(messageBody, smscDefaultCharset, RANDOM.nextInt(), use16bitReference);
        esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.UDHI);
      } else {
        messages = new byte[][]{ messageBody.getBytes(smscDefaultCharset) };
        // set UDHI, as concatenation will add UDH
        esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.DEFAULT);
      }
    } else if (Ucs2.isUcs2Encodable(messageBody)) {
      dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2, messageClass);
      if (messageBody.length() > MAX_SINGLE_MSG_CHAR_SIZE_UCS2) {
        // split message according to the maximum available length of a segment, character boundaries, etc.
        messages = Concatenation.splitUcs2(messageBody, RANDOM.nextInt(), use16bitReference);
        // set UDHI, as concatenation will add UDH
        esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.UDHI);
      } else {
        messages = new byte[][]{ messageBody.getBytes(UCS2_CHARSET) };
        esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.DEFAULT);
      }
    } else {
      log.error("The message '{}' contains non-encodeable characters", messageBody);
      return;
    }

    log.info("Sending message '{}'", messageBody);
    log.info("Message is {} characters long and will be sent as {} messages with data coding {}",
        messageBody.length(), messages.length, HexUtil.convertByteToHexString(dataCoding.toByte()));

    // submit all messages
    for (byte[] message : messages) {
      String messageId = submitMessage(session, message, sourceMsisdn, destinationMsisdn,
          dataCoding, esmClass);
      log.info("Message submitted, message_id is {}", messageId);
    }

    log.info("Entering listening mode. Press Enter to finish...");

    try {
      System.in.read();
    } catch (IOException e) {
      log.error("I/O error occurred", e);
    }

    session.unbindAndClose();
  }

  private String submitMessage(SMPPSession session, byte[] message, String sourceMsisdn, String destinationMsisdn,
                               DataCoding dataCoding, ESMClass esmClass) {
    String messageId = null;
    try {
      SubmitSmResult submitSmResult = session.submitShortMessage("CMT", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN,
          sourceMsisdn, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, destinationMsisdn, esmClass,
          (byte) 0, (byte) 1, null, null, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
          (byte) 0, dataCoding, (byte) 0, message);
      messageId = submitSmResult.getMessageId();
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
      log.error("Receive negative response", e);
    } catch (IOException e) {
      log.error("I/O error occurred", e);
    }
    return messageId;
  }

  private static class SessionStateListenerImpl implements SessionStateListener {
    public void onStateChange(SessionState newState, SessionState oldState, Session source) {
      log.info("Session {} state changed from {} to {}", source.getSessionId(), oldState, newState);
    }
  }
}
