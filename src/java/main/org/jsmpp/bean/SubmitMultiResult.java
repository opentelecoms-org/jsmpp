package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class SubmitMultiResult {
    private String messageId;
    private UnsuccessDelivery[] unsuccessDeliveries;
    
    // TODO uudashr: make sure message_id and destination_addr has valid length
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
