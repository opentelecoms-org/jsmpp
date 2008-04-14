package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class SubmitSmResp extends Command {
    private String messageId;

    public SubmitSmResp() {
        super();
    }

    /**
     * @return Returns the messageId.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId The messageId to set.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
