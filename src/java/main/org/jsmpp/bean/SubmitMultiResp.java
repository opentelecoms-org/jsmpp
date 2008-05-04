package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class SubmitMultiResp extends Command {
    private String messageId;
    private UnsuccessDelivery[] unsuccessSmes;
    
    public SubmitMultiResp() {
        super();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public UnsuccessDelivery[] getUnsuccessSmes() {
        return unsuccessSmes;
    }

    public void setUnsuccessSmes(UnsuccessDelivery[] unsuccessSmes) {
        this.unsuccessSmes = unsuccessSmes;
    }
}
