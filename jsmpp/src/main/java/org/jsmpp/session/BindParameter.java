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
package org.jsmpp.session;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.util.ObjectUtil;

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
    private InterfaceVersion interfaceVersion;

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
    public BindParameter(BindType bindType, String systemId, String password,
            String systemType, TypeOfNumber addrTon,
            NumberingPlanIndicator addrNpi, String addressRange) {
    	this(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange, InterfaceVersion.IF_34);
    }
    
    public BindParameter(BindType bindType, String systemId, String password,
            String systemType, TypeOfNumber addrTon,
            NumberingPlanIndicator addrNpi, String addressRange, InterfaceVersion interfaceVersion) {
        this.bindType = bindType;
        this.systemId = systemId;
        this.password = password;
        this.systemType = systemType;
        this.addrTon = addrTon;
        this.addrNpi = addrNpi;
        this.addressRange = addressRange;
        this.interfaceVersion = interfaceVersion;
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
        result = prime * result
                + ((addressRange == null) ? 0 : addressRange.hashCode());
        result = prime * result
                + ((bindType == null) ? 0 : bindType.hashCode());
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((systemId == null) ? 0 : systemId.hashCode());
        result = prime * result
                + ((systemType == null) ? 0 : systemType.hashCode());
        result = prime * result
                + ((interfaceVersion == null) ? 0 : interfaceVersion.hashCode());
        
        return result;
    }

    private boolean hasEqualAddrNpi(BindParameter other) {
        return ObjectUtil.equals(addrNpi, other.addrNpi);
    }

    private boolean hasEqualAddrTon(BindParameter other) {
        return ObjectUtil.equals(addrTon, other.addrTon);
    }

    private boolean hasEqualAddressRange(BindParameter other) {
        return ObjectUtil.equals(addressRange, other.addressRange);
    }

    private boolean hasEqualBindType(BindParameter other) {
        return ObjectUtil.equals(bindType, other.bindType);
    }

    private boolean hasEqualPassword(BindParameter other) {
        return ObjectUtil.equals(password, other.password);
    }

    private boolean hasEqualSystemId(BindParameter other) {
        return ObjectUtil.equals(systemId, other.systemId);
    }

    private boolean hasEqualSystemType(BindParameter other) {
        return ObjectUtil.equals(systemType, other.systemType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final BindParameter other = (BindParameter)obj;
        if (!hasEqualAddrNpi(other)) {
            return false;
        }
        if (!hasEqualAddrTon(other)) {
            return false;
        }
        if (!hasEqualAddressRange(other)) {
            return false;
        }
        if (!hasEqualBindType(other)) {
            return false;
        }
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

	public InterfaceVersion getInterfaceVersion() {
		return interfaceVersion;
	}

}
