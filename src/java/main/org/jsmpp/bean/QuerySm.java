package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class QuerySm extends MessageRequest {
    private String messageId;

    public QuerySm() {
        super();
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId
     *            the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

}
