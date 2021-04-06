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

import java.util.Objects;

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
     * Get the address_range
     * 
     * @return the address_range
     */
    public String getAddressRange() {
        return addressRange;
    }

    /**
     * Set the address_range
     * 
     * @param addressRange is the address_range
     */
    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }

    /**
     * Get the addr_npi
     * 
     * @return the addr_npi
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
     * Get the addr_ton
     * 
     * @return the addr_ton
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
     * Get the interface_version
     * 
     * @return the interface_version
     */
    public byte getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * Set the interface_version
     * 
     * @param interfaceVersion is the interface_version
     */
    public void setInterfaceVersion(byte interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    /**
     * Get the password.
     * 
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * 
     * @param password is the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the system_id
     * 
     * @return the system_id
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Set the system_id
     * 
     * @param systemId is the system_id
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * Get the system_type
     * 
     * @return the system_type
     */
    public String getSystemType() {
        return systemType;
    }

    /**
     * Set the system_type
     * 
     * @param systemType is the system_type
     */
    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Bind bind = (Bind) o;
        return interfaceVersion == bind.interfaceVersion &&
            addrTon == bind.addrTon &&
            addrNpi == bind.addrNpi &&
            Objects.equals(systemId, bind.systemId) &&
            Objects.equals(password, bind.password) &&
            Objects.equals(systemType, bind.systemType) &&
            Objects.equals(addressRange, bind.addressRange);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange);
    }

}
