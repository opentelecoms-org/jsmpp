package org.jsmpp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jsmpp.bean.Command;


/**
 * This class id implementation of {@link PDUReader} that use synchronize when
 * accessing to the {@link InputStream} or {@link DataInputStream} tha used to
 * read the PDU.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class SynchronizedPDUReader implements PDUReader {
    private final PDUReader pduReader;

    /**
     * Default constructor with specified pdu reader.
     * 
     * @param pduReader is the pdu reader.
     */
    public SynchronizedPDUReader(PDUReader pduReader) {
        this.pduReader = pduReader;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream,
     *      org.jsmpp.bean.Command)
     */
    public byte[] readPDU(InputStream in, Command pduHeader) throws IOException {
        synchronized (in) {
            return pduReader.readPDU(in, pduHeader);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.PDUReader#readPDU(java.io.InputStream, int, int,
     *      int, int)
     */
    public byte[] readPDU(InputStream in, int commandLength, int commandId,
            int commandStatus, int sequenceNumber) throws IOException {
        synchronized (in) {
            return pduReader.readPDU(in, commandLength, commandId,
                    commandStatus, sequenceNumber);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.PDUReader#readPDUHeader(java.io.DataInputStream)
     */
    public Command readPDUHeader(DataInputStream in)
            throws InvalidCommandLengthException, IOException {
        synchronized (in) {
            return pduReader.readPDUHeader(in);
        }
    }

}
