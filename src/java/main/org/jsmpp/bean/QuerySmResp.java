package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class QuerySmResp extends Command {
    private String messageId;
    private String finalDate;
    private MessageState messageState;
    private byte errorCode;

    public QuerySmResp() {
        super();
    }

    /**
     * @return the errorCode
     */
    public byte getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the finalDate
     */
    public String getFinalDate() {
        return finalDate;
    }

    /**
     * @param finalDate the finalDate to set
     */
    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the messageState
     */
    public MessageState getMessageState() {
        return messageState;
    }

    /**
     * @param messageState the messageState to set
     */
    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((finalDate == null) ? 0 : finalDate.hashCode());
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result
                + ((messageState == null) ? 0 : messageState.hashCode());
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
        final QuerySmResp other = (QuerySmResp)obj;
        if (errorCode != other.errorCode)
            return false;
        if (finalDate == null) {
            if (other.finalDate != null)
                return false;
        } else if (!finalDate.equals(other.finalDate))
            return false;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (messageState == null) {
            if (other.messageState != null)
                return false;
        } else if (!messageState.equals(other.messageState))
            return false;
        return true;
    }
    
    
}
