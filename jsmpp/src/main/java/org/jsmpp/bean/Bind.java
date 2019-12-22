/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.jsmpp.bean;

import org.jsmpp.util.ObjectUtil;

/**
 * This class represent SMPP bind command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class Bind extends Command {
    private static final long serialVersionUID = 4232311703795097833L;
    
    private String systemId;
    private String password;
    private String systemType;
    private byte interfaceVersion;
    private byte addrTon;
    private byte addrNpi;
    private String addressRange;

    public Bind() {
        super();
    }

    /**
     * Get the address_range.
     * 
     * @return the address_range.
     */
    public String getAddressRange() {
        return addressRange;
    }

    /**
     * Set the address_range.
     * 
     * @param addressRange is the address_range.
     */
    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }

    /**
     * Get the addr_npi.
     * 
     * @return the addr_npi.
     */
    public byte getAddrNpi() {
        return addrNpi;
    }

    /**
     * Set the addr_npi.
     * 
     * @param addrNpi is the addr_npi.
     */
    public void setAddrNpi(byte addrNpi) {
        this.addrNpi = addrNpi;
    }

    /**
     * Get the addr_ton.
     * 
     * @return the addr_ton.
     */
    public byte getAddrTon() {
        return addrTon;
    }

    /**
     * Set the addr_ton.
     * 
     * @param addrTon is the addr_ton.
     */
    public void setAddrTon(byte addrTon) {
        this.addrTon = addrTon;
    }

    /**
     * Get the interface_version.
     * 
     * @return the interface_version.
     */
    public byte getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * Set the interface_version.
     * 
     * @param interfaceVersion is the interface_version.
     */
    public void setInterfaceVersion(byte interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    /**
     * Get the password.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * 
     * @param password is the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the system_id.
     * 
     * @return the system_id.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Set the system_id.
     * 
     * @param systemId is the system_id.
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * Get the system_type.
     * 
     * @return the system_type.
     */
    public String getSystemType() {
        return systemType;
    }

    /**
     * Set the system_type.
     * 
     * @param systemType is the system_type.
     */
    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((addressRange == null) ? 0 : addressRange.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((systemId == null) ? 0 : systemId.hashCode());
        result = prime * result
                + ((systemType == null) ? 0 : systemType.hashCode());
        return result;
    }
    
    private boolean hasEqualAddressRange(Bind other) {
        return ObjectUtil.equals(addressRange, other.addressRange);
    }
    
    private boolean hasEqualPassword(Bind other) {
        return ObjectUtil.equals(password, other.password);
    }
    
    private boolean hasEqualSystemId(Bind other) {
        return ObjectUtil.equals(systemId, other.systemId);
    }
    
    private boolean hasEqualSystemType(Bind other) {
        return ObjectUtil.equals(systemType, other.systemType);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Bind other = (Bind)obj;
        if (addrNpi != other.addrNpi)
            return false;
        if (addrTon != other.addrTon)
            return false;
        if (!hasEqualAddressRange(other)) {
            return false;
        }
        if (interfaceVersion != other.interfaceVersion)
            return false;
        if (!hasEqualPassword(other)) {
            return false;
        }
        if (!hasEqualSystemId(other)) {
            return false;
        }
        if (!hasEqualSystemType(other)) {
            return false;
        }
        return true;
    }
    
}
