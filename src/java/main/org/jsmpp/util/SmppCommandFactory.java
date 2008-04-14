package org.jsmpp.util;

import java.io.DataInputStream;
import java.io.IOException;

import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Command;

/**
 * @author uudashr
 * 
 */
public class SmppCommandFactory {
    private PDUReader pduReader;
    
    public Command newInstance(DataInputStream in)
            throws InvalidCommandLengthException, PDUStringException, IOException {
        Command pduHeader = pduReader.readPDUHeader(in);
        byte[] pdu = pduReader.readPDU(in, pduHeader);
        switch (pduHeader.getCommandId()) {
            
        }
        return null;
    }
}
