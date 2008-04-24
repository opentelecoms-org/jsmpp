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
}
