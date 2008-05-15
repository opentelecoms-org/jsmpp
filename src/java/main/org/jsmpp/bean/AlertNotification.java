package org.jsmpp.bean;

import java.util.Arrays;

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((esmeAddr == null) ? 0 : esmeAddr.hashCode());
        result = prime * result + Arrays.hashCode(optionalParameters);
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
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
        final AlertNotification other = (AlertNotification)obj;
        if (esmeAddr == null) {
            if (other.esmeAddr != null)
                return false;
        } else if (!esmeAddr.equals(other.esmeAddr))
            return false;
        if (esmeAddrNpi != other.esmeAddrNpi)
            return false;
        if (esmeAddrTon != other.esmeAddrTon)
            return false;
        if (!Arrays.equals(optionalParameters, other.optionalParameters))
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
        return true;
    }
    
    
}
