package org.jsmpp.bean;

import java.util.Arrays;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result + Arrays.hashCode(unsuccessDeliveries);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SubmitMultiResult other = (SubmitMultiResult)obj;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (!Arrays.equals(unsuccessDeliveries, other.unsuccessDeliveries))
            return false;
        return true;
    }
    
    
}
