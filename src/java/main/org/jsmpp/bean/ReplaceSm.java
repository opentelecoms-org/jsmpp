package org.jsmpp.bean;

import java.util.Arrays;

/**
 * @author uudashr
 *
 */
public class ReplaceSm extends Command {
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private byte registeredDelivery;
    private byte smDefaultMsgId;
    private byte[] shortMessage;
    
    public ReplaceSm() {
        super();
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public byte getSourceAddrTon() {
        return sourceAddrTon;
    }

    public void setSourceAddrTon(byte sourceAddrTon) {
        this.sourceAddrTon = sourceAddrTon;
    }

    public byte getSourceAddrNpi() {
        return sourceAddrNpi;
    }

    public void setSourceAddrNpi(byte sourceAddrNpi) {
        this.sourceAddrNpi = sourceAddrNpi;
    }

    public String getSourceAddr() {
        return sourceAddr;
    }

    public void setSourceAddr(String sourceAddr) {
        this.sourceAddr = sourceAddr;
    }

    public String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    public byte[] getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(byte[] shortMessage) {
        this.shortMessage = shortMessage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime
                * result
                + ((scheduleDeliveryTime == null) ? 0 : scheduleDeliveryTime
                        .hashCode());
        result = prime * result + Arrays.hashCode(shortMessage);
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
        result = prime * result
                + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
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
        final ReplaceSm other = (ReplaceSm)obj;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (registeredDelivery != other.registeredDelivery)
            return false;
        if (scheduleDeliveryTime == null) {
            if (other.scheduleDeliveryTime != null)
                return false;
        } else if (!scheduleDeliveryTime.equals(other.scheduleDeliveryTime))
            return false;
        if (!Arrays.equals(shortMessage, other.shortMessage))
            return false;
        if (smDefaultMsgId != other.smDefaultMsgId)
            return false;
        if (sourceAddr == null) {
            if (other.sourceAddr != null)
                return false;
        } else if (!sourceAddr.equals(other.sourceAddr))
            return false;
        if (sourceAddrNpi != other.sourceAddrNpi)
            return false;
        if (sourceAddrTon != other.sourceAddrTon)
            return false;
        if (validityPeriod == null) {
            if (other.validityPeriod != null)
                return false;
        } else if (!validityPeriod.equals(other.validityPeriod))
            return false;
        return true;
    }
    
    
}
