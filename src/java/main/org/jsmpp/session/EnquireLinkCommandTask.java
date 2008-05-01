package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;

/**
 * @author uudashr
 *
 */
public class EnquireLinkCommandTask extends AbstractSendCommandTask {
    
    public EnquireLinkCommandTask(PDUSender pduSender) {
        super(pduSender);
    }

    public void executeTask(OutputStream out, int sequenceNumber)
            throws IOException {
        pduSender.sendEnquireLink(out, sequenceNumber);
    }
    
    public String getCommandName() {
        return "enquire_link";
    }
}
