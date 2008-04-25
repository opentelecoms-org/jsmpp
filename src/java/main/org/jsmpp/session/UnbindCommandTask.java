package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;

/**
 * @author uudashr
 *
 */
public class UnbindCommandTask extends AbstractSendCommandTask {
    
    public UnbindCommandTask(OutputStream out, PDUSender pduSender) {
        super(out, pduSender);
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws IOException {
        pduSender.sendUnbind(out, sequenceNumber);
    }
    
    public String getCommandName() {
        return "unbind";
    }
}
