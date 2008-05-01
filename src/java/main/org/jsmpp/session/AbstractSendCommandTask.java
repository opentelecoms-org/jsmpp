package org.jsmpp.session;

import org.jsmpp.PDUSender;

/**
 * Abstract send command task.
 * 
 * @author uudashr
 *
 */
public abstract class AbstractSendCommandTask implements SendCommandTask {
    protected final PDUSender pduSender;
    
    public AbstractSendCommandTask(PDUSender pduSender) {
        this.pduSender = pduSender;
    }
}
