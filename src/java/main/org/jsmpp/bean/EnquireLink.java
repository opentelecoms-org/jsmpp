package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class EnquireLink extends Command {

    public EnquireLink(int sequenceNumber) {
        super();
        commandLength = 16;
        commandId = SMPPConstant.CID_ENQUIRE_LINK;
        commandStatus = 0;
        this.sequenceNumber = sequenceNumber;
    }

    public EnquireLink() {
        super();
    }

}
