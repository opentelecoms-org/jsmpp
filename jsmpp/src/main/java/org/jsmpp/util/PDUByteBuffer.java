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

import org.jsmpp.bean.OptionalParameter;


/**
 * Utility to compose the PDU bytes. The size of the buffer depends on the
 * entities appended to the buffer.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class PDUByteBuffer {
    private static final CapacityPolicy DEFAULT_CAPACITY_POLICY = new SimpleCapacityPolicy();
    private CapacityPolicy capacityPolicy;
    private byte[] bytes;
    private int bytesLength;
    
    /**
     * Construct with specified command_id, command_status, and sequence_number.
     * 
     * @param commandId is the command_id.
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     */
    public PDUByteBuffer(int commandId, int commandStatus, int sequenceNumber) {
        this(commandId, commandStatus, sequenceNumber, DEFAULT_CAPACITY_POLICY);
    }
    
    /**
     * Construct with specified command_id, command_status, sequence_number and
     * capacity policy.
     * 
     * @param commandId is the command_id.
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence_number.
     * @param capacityPolicy is the capacity policy.
     */
    public PDUByteBuffer(int commandId, int commandStatus, int sequenceNumber, CapacityPolicy capacityPolicy) {
        this(capacityPolicy);
        append(commandId);
        append(commandStatus);
        append(sequenceNumber);
        normalizeCommandLength();
    }

    /**
     * Default constructor.
     */
    public PDUByteBuffer() {
        this(DEFAULT_CAPACITY_POLICY);
    }
    
    /**
     * Construct with specified capacity policy.
     * 
     * @param capacityPolicy is the capacity policy.
     */
    public PDUByteBuffer(CapacityPolicy capacityPolicy) {
        /*
         * the initial is 4 byte, just for the command_length
         */
        bytes = new byte[4];
        this.capacityPolicy = capacityPolicy;
        bytesLength = 4;
        normalizeCommandLength();
    }
    
    /**
     * Append bytes to specified offset and length.
     * 
     * @param b is the bytes to append.
     * @param offset is the offset where the bytes will be placed.
     * @param length the length that will specified which part of the bytes will
     *        be append.
     * @return the latest length of the byte buffer.
     */
    public int append(byte[] b, int offset, int length) {
        int oldLength = bytesLength;
        bytesLength += length;
        int newCapacity = capacityPolicy.ensureCapacity(bytesLength, bytes.length);
        if (newCapacity > bytes.length) {
            byte[] newB = new byte[newCapacity];
            System.arraycopy(bytes, 0, newB, 0, bytes.length); // copy current bytes to new bytes
            bytes = newB;
        }
        System.arraycopy(b, offset, bytes, oldLength, length); // assign value
        normalizeCommandLength();
        return bytesLength;
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
     * Append {@code String} value and optionally append a null byte.
     * 
     * @param stringValue The {@code String} value to append.
     * @param nullTerminated {@code true} means C-Octet String. The default value is {@code true}.
     * @return the latest length of the buffer.
     */
    public int append(String stringValue, boolean nullTerminated) {
        if (stringValue != null)
            append(stringValue.getBytes());
        if (nullTerminated)
            append((byte)0);
        return bytesLength;
    }

    /**
     * Append C-Octet String / null terminated String value.
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
     * @param optionalParameter The optional parameters.
     * @return the latest length of the buffer.
     */
    public int append(OptionalParameter optionalParameter) {
        return append(optionalParameter.serialize());
    }
    
    /**
     * Append all optional parameters.
     * 
     * @param optionalParameters The optional parameters.
     * @return the latest length of the buffer.
     */
    public int appendAll(OptionalParameter[] optionalParameters) {
        int length = 0;
        for (OptionalParameter optionalParamameter : optionalParameters) {
            length += append(optionalParamameter);
        }
        return length;
    }
    
    /**
     * Assign the proper command length to the first 4 octet.
     */
    private void normalizeCommandLength() {
        System.arraycopy(OctetUtil.intToBytes(bytesLength), 0, bytes, 0, 4);
    }

    /**
     * Get the composed bytes of PDU.
     * 
     * @return the composed bytes.
     */
    public byte[] toBytes() {
        byte[] returnBytes = new byte[bytesLength];
        System.arraycopy(bytes, 0, returnBytes, 0, bytesLength);
        return returnBytes;
    }
    
    int getCommandLengthValue() {
        return OctetUtil.bytesToInt(bytes, 0);
    }
    
    int getBytesLength() {
        return bytesLength;
    }
}