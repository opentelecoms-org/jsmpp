package org.jsmpp.session;

import org.jsmpp.BindType;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;

/**
 * This class is wraps all bind parameter that will be send as PDU.
 * 
 * @author uudashr
 *
 */
public class BindParameter {
    private BindType bindType;
    private String systemId;
    private String password;
    private String systemType;
    private TypeOfNumber addrTon;
    private NumberingPlanIndicator addrNpi;
    private String addressRange;

    /**
     * Construct with all mandatory parameters.
     * 
     * @param bindType is the bind type.
     * @param systemId is the system id.
     * @param password is the password.
     * @param systemType is the system type.
     * @param addrTon is the address TON.
     * @param addrNpi is the address NPI.
     * @param addressRange is the address range.
     */
    public BindParameter(BindType bindType, String systemId, String password, String systemType, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange) {
        this.bindType = bindType;
        this.systemId = systemId;
        this.password = password;
        this.systemType = systemType;
        this.addrTon = addrTon;
        this.addrNpi = addrNpi;
        this.addressRange = addressRange;
    }

    public BindType getBindType() {
        return bindType;
    }

    public String getSystemId() {
        return systemId;
    }

    public String getPassword() {
        return password;
    }

    public String getSystemType() {
        return systemType;
    }

    public TypeOfNumber getAddrTon() {
        return addrTon;
    }

    public NumberingPlanIndicator getAddrNpi() {
        return addrNpi;
    }

    public String getAddressRange() {
        return addressRange;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addrNpi == null) ? 0 : addrNpi.hashCode());
        result = prime * result + ((addrTon == null) ? 0 : addrTon.hashCode());
        result = prime * result + ((addressRange == null) ? 0 : addressRange.hashCode());
        result = prime * result + ((bindType == null) ? 0 : bindType.hashCode());
        result = prime * result + ((password == null) ? 0 : password.hashCode());
        result = prime * result + ((systemId == null) ? 0 : systemId.hashCode());
        result = prime * result + ((systemType == null) ? 0 : systemType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BindParameter other = (BindParameter) obj;
        if (addrNpi == null) {
            if (other.addrNpi != null)
                return false;
        } else if (!addrNpi.equals(other.addrNpi))
            return false;
        if (addrTon == null) {
            if (other.addrTon != null)
                return false;
        } else if (!addrTon.equals(other.addrTon))
            return false;
        if (addressRange == null) {
            if (other.addressRange != null)
                return false;
        } else if (!addressRange.equals(other.addressRange))
            return false;
        if (bindType == null) {
            if (other.bindType != null)
                return false;
        } else if (!bindType.equals(other.bindType))
            return false;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (systemId == null) {
            if (other.systemId != null)
                return false;
        } else if (!systemId.equals(other.systemId))
            return false;
        if (systemType == null) {
            if (other.systemType != null)
                return false;
        } else if (!systemType.equals(other.systemType))
            return false;
        return true;
    }

}
