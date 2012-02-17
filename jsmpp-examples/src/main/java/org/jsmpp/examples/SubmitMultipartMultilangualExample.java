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
import java.util.Random;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
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
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;

/**
 * 
 * @author Maciej Pigulski <maciej.pigulski[at]gmail.com>
 * 
 */
public class SubmitMultipartMultilangualExample {

	private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2 = 134;
	private static final int MAX_SINGLE_MSG_SEGMENT_SIZE_UCS2 = 70;
	private static final int MAX_MULTIPART_MSG_SEGMENT_SIZE_7BIT = 154;
	private static final int MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT = 160;

	private class SessionStateListenerImpl implements SessionStateListener {
		public void onStateChange(SessionState newState, SessionState oldState, Session source) {
			System.out.println("Session state changed from " + oldState + " to " + newState);
		}
	}

	private byte[][] splitUnicodeMessage(byte[] aMessage, Integer maximumMultipartMessageSegmentSize) {

		final byte UDHIE_HEADER_LENGTH = 0x05;
		final byte UDHIE_IDENTIFIER_SAR = 0x00;
		final byte UDHIE_SAR_LENGTH = 0x03;

		// determine how many messages have to be sent
		int numberOfSegments = aMessage.length / maximumMultipartMessageSegmentSize;
		int messageLength = aMessage.length;
		if (numberOfSegments > 255) {
			numberOfSegments = 255;
			messageLength = numberOfSegments * maximumMultipartMessageSegmentSize;
		}
		if ((messageLength % maximumMultipartMessageSegmentSize) > 0) {
			numberOfSegments++;
		}

		// prepare array for all of the msg segments
		byte[][] segments = new byte[numberOfSegments][];

		int lengthOfData;

		// generate new reference number
		byte[] referenceNumber = new byte[1];
		new Random().nextBytes(referenceNumber);

		// split the message adding required headers
		for (int i = 0; i < numberOfSegments; i++) {
			if (numberOfSegments - i == 1) {
				lengthOfData = messageLength - i * maximumMultipartMessageSegmentSize;
			} else {
				lengthOfData = maximumMultipartMessageSegmentSize;
			}

			// new array to store the header
			segments[i] = new byte[6 + lengthOfData];

			// UDH header
			// doesn't include itself, its header length
			segments[i][0] = UDHIE_HEADER_LENGTH;
			// SAR identifier
			segments[i][1] = UDHIE_IDENTIFIER_SAR;
			// SAR length
			segments[i][2] = UDHIE_SAR_LENGTH;
			// reference number (same for all messages)
			segments[i][3] = referenceNumber[0];
			// total number of segments
			segments[i][4] = (byte) numberOfSegments;
			// segment number
			segments[i][5] = (byte) (i + 1);

			// copy the data into the array
			System.arraycopy(aMessage, (i * maximumMultipartMessageSegmentSize), segments[i], 6, lengthOfData);

		}
		return segments;
	}

	private void sendAndWait() throws IOException, InterruptedException {

		String sourceMsisdn = "1616";
		String destinationMsisdn = "666111222";
		MessageClass messageClass = MessageClass.CLASS1;
		String messageBody = "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies. Curabitur et ligula. Ut molestie a, ultricies porta urna.";

		SMPPSession session = new SMPPSession();
		session.addSessionStateListener(new SessionStateListenerImpl());
		session.setMessageReceiverListener(new MessageReceiverListenerImpl());

		try {
			session.connectAndBind("localhost", 2775, new BindParameter(BindType.BIND_TRX, "smppclient", "password",
					"cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
		} catch (IOException e) {
			System.err.println("Failed connect and bind to host");
			e.printStackTrace();
		}

		// configure variables acording to if message contains national
		// characters
		Alphabet alphabet = null;
		int maximumSingleMessageSize = 0;
		int maximumMultipartMessageSegmentSize = 0;
		byte[] byteSingleMessage = null;
		if (Gsm0338.isEncodeableInGsm0338(messageBody)) {
			byteSingleMessage = messageBody.getBytes();
			alphabet = Alphabet.ALPHA_DEFAULT;
			maximumSingleMessageSize = MAX_SINGLE_MSG_SEGMENT_SIZE_7BIT;
			maximumMultipartMessageSegmentSize = MAX_MULTIPART_MSG_SEGMENT_SIZE_7BIT;
		} else {
			byteSingleMessage = messageBody.getBytes("UTF-16BE");
			alphabet = Alphabet.ALPHA_UCS2;
			maximumSingleMessageSize = MAX_SINGLE_MSG_SEGMENT_SIZE_UCS2;
			maximumMultipartMessageSegmentSize = MAX_MULTIPART_MSG_SEGMENT_SIZE_UCS2;
		}

		// check if message needs splitting and set required sending parameters
		byte[][] byteMessagesArray = null;
		ESMClass esmClass = null;
		if (messageBody.length() > maximumSingleMessageSize) {
			// split message according to the maximum length of a segment
			byteMessagesArray = splitUnicodeMessage(byteSingleMessage, maximumMultipartMessageSegmentSize);
			// set UDHI so PDU will decode the header
			esmClass = new ESMClass(MessageMode.DEFAULT, MessageType.DEFAULT, GSMSpecificFeature.UDHI);
		} else {
			byteMessagesArray = new byte[][] { byteSingleMessage };
			esmClass = new ESMClass();
		}

		System.out.println("Sending message " + messageBody);
		System.out.printf("Message is %d characters long and will be sent as %d messages with params: %s %s ",
				messageBody.length(), byteMessagesArray.length, alphabet, messageClass);
		System.out.println();

		// submit all messages
		for (int i = 0; i < byteMessagesArray.length; i++) {
			String messageId = submitMessage(session, byteMessagesArray[i], sourceMsisdn, destinationMsisdn,
					messageClass, alphabet, esmClass);
			System.out.println("Message submitted, message_id is " + messageId);
		}

		System.out.println("Entering listening mode. Press enter to finish...");

		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}

		session.unbindAndClose();
	}

	private String submitMessage(SMPPSession session, byte[] message, String sourceMsisdn, String destinationMsisdn,
			MessageClass messageClass, Alphabet alphabet, ESMClass esmClass) {
		String messageId = null;
		try {
			messageId = session.submitShortMessage("CMT", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN,
					sourceMsisdn, TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, destinationMsisdn, esmClass,
					(byte) 0, (byte) 1, null, null, new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE),
					(byte) 0, new GeneralDataCoding(alphabet, esmClass), (byte) 0, message);
		} catch (PDUException e) {
			// Invalid PDU parameter
			System.err.println("Invalid PDU parameter");
			e.printStackTrace();
		} catch (ResponseTimeoutException e) {
			// Response timeout
			System.err.println("Response timeout");
			e.printStackTrace();
		} catch (InvalidResponseException e) {
			// Invalid response
			System.err.println("Receive invalid respose");
			e.printStackTrace();
		} catch (NegativeResponseException e) {
			// Receiving negative response (non-zero command_status)
			System.err.println("Receive negative response");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("IO error occur");
			e.printStackTrace();
		}
		return messageId;
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		new SubmitMultipartMultilangualExample().sendAndWait();
	}
}

/**
 * Based on http://www.smsitaly.com/Download/ETSI_GSM_03.38.pdf
 */
class Gsm0338 {

	private static final short ESC_CHARACTER = (short) 27;

	private static final short[] isoGsm0338Array = { 64, 163, 36, 165, 232, 233, 249, 236, 242, 199, 10, 216, 248, 13,
			197, 229, 0, 95, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 198, 230, 223, 201, 32, 33, 34, 35, 164, 37, 38, 39, 40, 41,
			42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 161, 65, 66, 67,
			68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 196, 214, 209,
			220, 167, 191, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115,
			116, 117, 118, 119, 120, 121, 122, 228, 246, 241, 252, 224 };

	private static final short[][] extendedIsoGsm0338Array = { { 10, 12 }, { 20, 94 }, { 40, 123 }, { 41, 125 },
			{ 47, 92 }, { 60, 91 }, { 61, 126 }, { 62, 93 }, { 64, 124 }, { 101, 164 } };

	public static boolean isEncodeableInGsm0338(String isoString) {
		byte[] isoBytes = isoString.getBytes();
		outer: for (int i = 0; i < isoBytes.length; i++) {
			for (int j = 0; j < isoGsm0338Array.length; j++) {
				if (isoGsm0338Array[j] == isoBytes[i]) {
					continue outer;
				}
			}
			for (int j = 0; j < extendedIsoGsm0338Array.length; j++) {
				if (extendedIsoGsm0338Array[j][1] == isoBytes[i]) {
					continue outer;
				}
			}
			return false;
		}
		return true;
	}

}