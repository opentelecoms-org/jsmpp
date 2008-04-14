package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class QuerySmResp extends Command {
    private String _messageId;
    // TODO uud: change using Date instead of String.
    private String _finalDate;
    private MessageState _messageState;
    private byte _errorCode;

    public QuerySmResp() {
        super();
    }

    /**
     * @return the errorCode
     */
    public byte getErrorCode() {
        return _errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(byte errorCode) {
        _errorCode = errorCode;
    }

    /**
     * @return the finalDate
     */
    public String getFinalDate() {
        return _finalDate;
    }

    /**
     * @param finalDate the finalDate to set
     */
    public void setFinalDate(String finalDate) {
        _finalDate = finalDate;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return _messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        _messageId = messageId;
    }

    /**
     * @return the messageState
     */
    public MessageState getMessageState() {
        return _messageState;
    }

    /**
     * @param messageState the messageState to set
     */
    public void setMessageState(MessageState messageState) {
        _messageState = messageState;
    }
}
