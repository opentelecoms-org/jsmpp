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
package org.jsmpp;

import static org.jsmpp.SMPPConstant.PDU_HEADER_LENGTH;

import java.io.DataInputStream;
import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.util.OctetUtil;


/**
 * Default implementation of {@link PDUReader}.
 *
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public class DefaultPDUReader implements PDUReader {

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDUHeader(java.io.DataInputStream)
     */
    @Override
    public Command readPDUHeader(DataInputStream in)
            throws InvalidCommandLengthException, IOException {
        Command header = new Command();
        header.setCommandLength(in.readInt());

        if (header.getCommandLength() < PDU_HEADER_LENGTH) {
            // command length too short, read the left dump anyway
            byte[] dump = new byte[header.getCommandLength()];
            if (header.getCommandLength() >= 4) {
                in.read(dump, 4, header.getCommandLength() - 4);
            }
            throw new InvalidCommandLengthException("Command length "
                    + header.getCommandLength() + " is too short");
        }
        header.setCommandId(in.readInt());
        header.setCommandStatus(in.readInt());
        header.setSequenceNumber(in.readInt());
        return header;
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream, org.jsmpp.bean.Command)
     */
    @Override
    public byte[] readPDU(DataInputStream in, Command pduHeader) throws IOException {
        return readPDU(in, pduHeader.getCommandLength(), pduHeader
                .getCommandId(), pduHeader.getCommandStatus(), pduHeader
                .getSequenceNumber());
    }

    /* (non-Javadoc)
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream, int, int, int, int)
     */
    @Override
    public byte[] readPDU(DataInputStream in, int commandLength, int commandId,
            int commandStatus, int sequenceNumber) throws IOException {

        byte[] b = new byte[commandLength];
        System.arraycopy(OctetUtil.intToBytes(commandLength), 0, b, 0, 4);
        System.arraycopy(OctetUtil.intToBytes(commandId), 0, b, 4, 4);
        System.arraycopy(OctetUtil.intToBytes(commandStatus), 0, b, 8, 4);
        System.arraycopy(OctetUtil.intToBytes(sequenceNumber), 0, b, 12, 4);

        if (commandLength > PDU_HEADER_LENGTH) {
            synchronized (in) {
                in.readFully(b, PDU_HEADER_LENGTH, commandLength - PDU_HEADER_LENGTH);
            }
        }
        return b;
    }
}
