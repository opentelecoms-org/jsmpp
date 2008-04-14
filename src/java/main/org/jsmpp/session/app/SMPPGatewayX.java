package org.jsmpp.session.app;

import org.jsmpp.MessageCoding;
import org.jsmpp.util.OctetUtil;


/**
 * @author uudashr
 *
 */
public class SMPPGatewayX {
	private static final int MAX_MESSAGE_8_BIT = 140;
	private static final int MAX_MESSAGE_7_BIT = 160;
	
	private static final int MAX_SEGMENT_8_BIT = 134; // 140 - 6
	private static final int MAX_SEGMENT_7_BIT = 153; // 160 - 7
	private static final int MAX_SEGMENT_UCS2 = 67; // (140 - 6) / 2
	
	private final static byte SAR_IDENTIFIER = 0x08;
	private final static byte SAR_LENGTH = 0x04;
	
	private static byte[][] splitSMPPMessage(byte[] message, MessageCoding messageCoding) {
		return null;
	}
	private static byte[][] splitSMPPMessage(byte[] message, short referenceNumber) {
		int messageLength = message.length;
		int totalSegment = messageLength / MAX_SEGMENT_8_BIT;
		
		int modulus = messageLength % MAX_SEGMENT_8_BIT;
		if (modulus > 0)
			totalSegment++;
		
		if (totalSegment > 255) {
			// limit of total segment is 255
			totalSegment = 255;
			// fix up the message length
			messageLength = totalSegment * MAX_SEGMENT_8_BIT;
		}
		
		byte[] refNumber = OctetUtil.shortToBytes(referenceNumber);
		byte[][] segments = new byte[totalSegment][];
		for (int i = 0; i < totalSegment; i++) {
			int lenMessage = message.length - MAX_SEGMENT_8_BIT * (i + 1);
			if (i + 1 == totalSegment) {
				lenMessage = modulus;
			} else {
				lenMessage = MAX_SEGMENT_8_BIT;
			}
			
			segments[i] = new byte[7 + lenMessage];
			
			// It seems header for segment of concatenated message
			segments[i][0] = 6;
			// SAR (Segmentation and Reassemble) identifier
			segments[i][1] = SAR_IDENTIFIER; 
			// SAR length
			segments[i][2] = SAR_LENGTH;
			// Reference number
			System.arraycopy(refNumber, 0, segments[i], 3, 2);
			// Total segment
			segments[i][5] = (byte)totalSegment;
			// Segment number
			segments[i][6] = (byte)(i + 1);
			// Data
			System.arraycopy(message, MAX_SEGMENT_8_BIT * i, segments[i], 7, lenMessage);
		}
		return segments;
	}
	
	public static void main(String[] args) {
		/*
		SortedMap<String, Charset> charsets = Charset.availableCharsets();
		for (String name : charsets.keySet()) {
			System.out.println(name);
		}
		*/
		
		String message = "“ , contoh “Cillin”";
		byte[] newMsg = message.getBytes();
		for (int i = 0; i < newMsg.length; i++) {
			newMsg[i] = (byte)(newMsg[i] & 0x7f);
		}
		System.out.println(new String(newMsg));
		/*
		String message = "Quick brown for jump over the tree, Quick brown for jump over the tree, Quick brown for jump over the tree, Quick brown for jump over the tree, Quick brown for jump over the tree, Quick brown for jump over the tree";
		System.out.println(message.length());
		System.out.println(Charset.defaultCharset().name());
		byte[][] b = splitSMPPMessage(message.getBytes(), (short)1);
		for (int i = 0; i < b.length; i++) {
			String segment = new String(b[i]);
			System.out.println(segment);
			System.out.println(segment.length());
		}
		
		byte[][] b2 = LongSMS.splitMessage8Bit(message.getBytes());
		for (int i = 0; i < b2.length; i++) {
			String segment = new String(b2[i]);
			System.out.println(segment);
			System.out.println(segment.length());
		}
		*/
	}
}
