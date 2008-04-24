package org.jsmpp.util;

import org.jsmpp.bean.OptionalParameter;


/**
 * Utility to compose the pdu bytes. The size of the buffer is depends to the
 * entities appended to the buffer.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
class PDUByteBuffer {
    private byte[] bytes;

    /**
     * Construct with specified command_id, command_status, and sequence_number.
     * 
     * @param commandId is the command_id.
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     */
    public PDUByteBuffer(int commandId, int commandStatus, int sequenceNumber) {
        this();
        append(commandId);
        append(commandStatus);
        append(sequenceNumber);
        normalizeCommandLength();
    }

    /**
     * Default constuctor.
     */
    public PDUByteBuffer() {
        /*
         * the initial is 4 byte, just for the command_length
         */
        bytes = new byte[4];
        normalizeCommandLength();
    }

    /**
     * Append byets to specified offset and length.
     * 
     * @param b is the bytes to append.
     * @param offset is the offset where the bytes will be placed.
     * @param length the length that will specified which part of the bytes will
     *        be append.
     * @return the latest length of the byte buffer.
     */
    public int append(byte[] b, int offset, int length) {
        byte[] newB = new byte[bytes.length + length];
        System.arraycopy(bytes, 0, newB, 0, bytes.length);
        System.arraycopy(b, offset, newB, bytes.length, length);
        bytes = newB;
        normalizeCommandLength();
        return bytes.length;
    }

    /**
     * Append all bytes.
     * 
     * @param bytes is the bytes to append.
     * @return the latest length of the buffer.
     */
    public int append(byte[] bytes) {
        return append(bytes, 0, bytes.length);
    }

    /**
     * Append single byte.
     * 
     * @param b is the byte to append.
     * @return the latest length of the buffer.
     */
    public int append(byte b) {
        return append(new byte[] { b });
    }

    /**
     * Append int value (contains 4 octet).
     * 
     * @param intValue is the value to append.
     * @return the latest length of the buffer.
     */
    public int append(int intValue) {
        return append(OctetUtil.intToBytes(intValue));
    }

    /**
     * Append <tt>String</tt> value.
     * 
     * @param stringValue
     * @param nullTerminated <tt>true<tt> means C-Octet String. 
     *      The default value is <tt>true</tt>.
     * @return te latest length of the buffer.
     */
    public int append(String stringValue, boolean nullTerminated) {
        if (stringValue != null)
            append(stringValue.getBytes());
        if (nullTerminated)
            append((byte)0);
        return bytes.length;
    }

    /**
     * Append C-Octet String / null terminated Stirng value.
     * 
     * @param stringValue is the value to append.
     * @return the latest length of the buffer.
     */
    public int append(String stringValue) {
        return append(stringValue, true);
    }
    
    /**
     * Append an optional parameter.
     * 
     * @param optionalParameter is the optional parameter.
     * @return the latest length of the buffer.
     */
    public int append(OptionalParameter optionalParameter) {
        return append(optionalParameter.serialize());
    }
    
    /**
     * Append all optional parameters.
     * 
     * @param optionalParameters is the optional parameters.
     * @return the latest length of the buffer.
     */
    public int appendAll(OptionalParameter[] optionalParameters) {
        int length = 0;
        for (OptionalParameter optionalParamameter : optionalParameters) {
            append(optionalParamameter);
        }
        return length;
    }
    
    /**
     * Normalize the command_length parameter.
     */
    private void normalizeCommandLength() {
        System.arraycopy(OctetUtil.intToBytes(bytes.length), 0, bytes, 0, 4);
    }

    /**
     * Get the composed bytes of PDU.
     * 
     * @return the composed bytes.
     */
    public byte[] getBytes() {
        return bytes;
    }
}