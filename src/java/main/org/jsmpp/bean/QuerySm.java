package org.jsmpp.bean;


/**
 * @author uudashr
 * 
 */
public class QuerySm extends Command {
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    
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
     * @param messageId the messageId to set
     */
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
        return result;
    }
    
    private boolean hasEqualMessageId(QuerySm other) {
        if (messageId == null) {
            if (other.messageId != null) {
                return false;
            }
        }
        return messageId.equals(other.messageId);
    }
    
    private boolean hasEqualSourceAddr(QuerySm other) {
        if (sourceAddr == null) {
            if (other.sourceAddr != null) {
                return false;
            }
        }
        return sourceAddr.equals(other.sourceAddr);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final QuerySm other = (QuerySm)obj;
        if (!hasEqualMessageId(other)) {
            return false;
        }
        if (!hasEqualSourceAddr(other)) {
            return false;
        }
        if (sourceAddrNpi != other.sourceAddrNpi)
            return false;
        if (sourceAddrTon != other.sourceAddrTon)
            return false;
        return true;
    }
    
    
}
