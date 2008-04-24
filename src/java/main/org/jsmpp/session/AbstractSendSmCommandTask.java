package org.jsmpp.session;

import java.io.OutputStream;

import org.jsmpp.PDUSender;

/**
 * @author uudashr
 *
 */
public abstract class AbstractSendSmCommandTask implements SendSmCommandTask {
    protected final OutputStream out;
    protected final PDUSender pduSender;
    
    public AbstractSendSmCommandTask(OutputStream out, PDUSender pduSender) {
        this.out = out;
        this.pduSender = pduSender;
    }
}
