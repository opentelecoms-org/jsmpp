package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class SubmitMultiResult {
    private String messageId;
    private UnsuccessDelivery[] unsuccessDeliveries;
    
    public SubmitMultiResult(String messageId,
            UnsuccessDelivery... unsuccessDeliveries) {
        this.messageId = messageId;
        this.unsuccessDeliveries = unsuccessDeliveries;
    }

    public String getMessageId() {
        return messageId;
    }

    public UnsuccessDelivery[] getUnsuccessDeliveries() {
        return unsuccessDeliveries;
    }
    
    
}
