package org.jsmpp.bean;

import java.util.Arrays;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result + Arrays.hashCode(unsuccessSmes);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SubmitMultiResp other = (SubmitMultiResp)obj;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (!Arrays.equals(unsuccessSmes, other.unsuccessSmes))
            return false;
        return true;
    }
    
    
}
