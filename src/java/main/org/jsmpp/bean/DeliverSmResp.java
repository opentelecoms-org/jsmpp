package org.jsmpp.bean;

/**
 * This class represent SMPP deliver short message command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class DeliverSmResp extends Command {
    private String messageId;

    /**
     * Default constructor.
     */
    public DeliverSmResp() {
        super();
    }

    /**
     * It should be always set to null.
     * 
     * @param messageId is the message_id (it should be always set to null).
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * Get the message_id (It should be always null).
     * 
     * @return the message_id (it should be always set to null).
     */
    public String getMessageId() {
        return messageId;
    }
}
