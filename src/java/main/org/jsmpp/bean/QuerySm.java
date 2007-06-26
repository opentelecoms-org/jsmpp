package org.jsmpp.bean;

import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;

/**
 * @author uudashr
 * 
 */
public class QuerySm extends Command {
    private String _messageId;
    private TypeOfNumber _sourceAddrTon;
    private NumberingPlanIndicator _sourceAddrNpi;
    private String _sourceAddr;

    public QuerySm() {
        super();
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
     * @return the sourceAddr
     */
    public String getSourceAddr() {
        return _sourceAddr;
    }

    /**
     * @param sourceAddr the sourceAddr to set
     */
    public void setSourceAddr(String sourceAddr) {
        _sourceAddr = sourceAddr;
    }

    /**
     * @return the sourceAddrNpi
     */
    public NumberingPlanIndicator getSourceAddrNpi() {
        return _sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi the sourceAddrNpi to set
     */
    public void setSourceAddrNpi(NumberingPlanIndicator sourceAddrNpi) {
        _sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return the sourceAddrTon
     */
    public TypeOfNumber getSourceAddrTon() {
        return _sourceAddrTon;
    }

    /**
     * @param sourceAddrTon the sourceAddrTon to set
     */
    public void setSourceAddrTon(TypeOfNumber sourceAddrTon) {
        _sourceAddrTon = sourceAddrTon;
    }

}
