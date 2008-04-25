package org.jsmpp.session;

import java.io.OutputStream;

import org.jsmpp.PDUSender;

/**
 * Abstract send command task.
 * 
 * @author uudashr
 *
 */
public abstract class AbstractSendCommandTask implements SendCommandTask {
    protected final OutputStream out;
    protected final PDUSender pduSender;
    
    public AbstractSendCommandTask(OutputStream out, PDUSender pduSender) {
        this.out = out;
        this.pduSender = pduSender;
    }
}
