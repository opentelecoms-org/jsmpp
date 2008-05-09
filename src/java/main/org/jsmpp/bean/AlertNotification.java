package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class AlertNotification extends Command {
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private byte esmeAddrTon;
    private byte esmeAddrNpi;
    private String esmeAddr;
    private OptionalParameter[] optionalParameters;
    
    public AlertNotification() {
        super();
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

    public byte getEsmeAddrTon() {
        return esmeAddrTon;
    }

    public void setEsmeAddrTon(byte esmeAddrTon) {
        this.esmeAddrTon = esmeAddrTon;
    }

    public byte getEsmeAddrNpi() {
        return esmeAddrNpi;
    }

    public void setEsmeAddrNpi(byte esmeAddrNpi) {
        this.esmeAddrNpi = esmeAddrNpi;
    }

    public String getEsmeAddr() {
        return esmeAddr;
    }

    public void setEsmeAddr(String esmeAddr) {
        this.esmeAddr = esmeAddr;
    }

    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }

    public void setOptionalParameters(OptionalParameter[] optionalParameters) {
        this.optionalParameters = optionalParameters;
    }
    
}
