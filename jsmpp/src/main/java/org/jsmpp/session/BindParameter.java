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

import java.util.Objects;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;

/**
 * This class wraps all bind parameters that will be send as PDU.
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

    public InterfaceVersion getInterfaceVersion() {
		    return interfaceVersion;
	  }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BindParameter that = (BindParameter) o;
        return bindType == that.bindType &&
            Objects.equals(systemId, that.systemId) &&
            Objects.equals(password, that.password) &&
            Objects.equals(systemType, that.systemType) &&
            addrTon == that.addrTon &&
            addrNpi == that.addrNpi &&
            Objects.equals(addressRange, that.addressRange) &&
            interfaceVersion == that.interfaceVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange, interfaceVersion);
    }

}
