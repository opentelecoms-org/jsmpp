package org.jsmpp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.util.OctetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of {@link PDUReader}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class DefaultPDUReader implements PDUReader {
    final DataInputStream in;
    static final Logger logger = LoggerFactory.getLogger(DefaultPDUReader.class);

    public DefaultPDUReader(InputStream in) {
        this.in = new DataInputStream(new BufferedInputStream(in));
    }

    public Command readPDUHeader() throws InvalidCommandLengthException, IOException {
        Command header = new Command();
        header.setCommandLength(in.readInt());

        if (header.getCommandLength() < 16) {
            // command length to short, read the left dump anyway
            byte[] dump = new byte[header.getCommandLength()];
            in.read(dump, 4, header.getCommandLength() - 4);

            throw new InvalidCommandLengthException("Command length " + header.getCommandLength() + " is to short");
        }
        header.setCommandId(in.readInt());
        header.setCommandStatus(in.readInt());
        header.setSequenceNumber(in.readInt());
        return header;
    }

    public byte[] readPDU(Command pduHeader) throws IOException {
        return readPDU(pduHeader.getCommandLength(), pduHeader.getCommandId(), pduHeader.getCommandStatus(), pduHeader.getSequenceNumber());
    }

    public synchronized PDU readPDU() throws InvalidCommandLengthException, IOException {
        Command pduHeader = readPDUHeader();
        return new PDU(pduHeader, readPDU(pduHeader));
    }

    public byte[] readPDU(int commandLength, int commandId, int commandStatus, int sequenceNumber) throws IOException {
        byte[] b = new byte[commandLength];
        System.arraycopy(OctetUtil.intToBytes(commandLength), 0, b, 0, 4);
        System.arraycopy(OctetUtil.intToBytes(commandId), 0, b, 4, 4);
        System.arraycopy(OctetUtil.intToBytes(commandStatus), 0, b, 8, 4);
        System.arraycopy(OctetUtil.intToBytes(sequenceNumber), 0, b, 12, 4);

        if (commandLength > 16) {
            int len = commandLength - 16;
            int totalRead = -1;
            totalRead = in.read(b, 16, commandLength - 16);
            if (totalRead != len) {
                throw new IOException("Unexpected length of byte readed. Expecting " + len + " but only read " + totalRead);
            }
        }
        return b;
    }
}
