package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class AbstractSmRespCommand extends Command {
    private String messageId;

    public AbstractSmRespCommand() {
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
