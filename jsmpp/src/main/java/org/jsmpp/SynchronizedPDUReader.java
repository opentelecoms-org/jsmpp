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

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.jsmpp.bean.Command;


/**
 * This class is an implementation of a {@link PDUReader} that uses synchronization when
 * accessing the {@link InputStream} or {@link DataInputStream} used to read the PDU.
 * 
 * @author uudashr
 * @version 1.1
 * @since 1.0
 * 
 */
public class SynchronizedPDUReader implements PDUReader {
    private final PDUReader pduReader;
    
    /**
     * Default constructor.
     */
    public SynchronizedPDUReader() {
        this(new DefaultPDUReader());
    }
    
    /**
     * Construct with specified pdu reader.
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
    public byte[] readPDU(DataInputStream in, Command pduHeader) throws IOException {
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
    public byte[] readPDU(DataInputStream in, int commandLength, int commandId,
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
