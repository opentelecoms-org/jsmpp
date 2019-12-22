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

import java.util.Arrays;

import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.util.ObjectUtil;

/**
 * @author uudashr
 *
 */
public class AlertNotification extends Command {
    private static final long serialVersionUID = 1566936459989108566L;
    
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

    public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass)
    {
    	return OptionalParameters.get(tagClass, optionalParameters);
    }
    
    public OptionalParameter getOptionalParameter(Tag tagEnum)
    {
    	return OptionalParameters.get(tagEnum.code(), optionalParameters);
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
    
    private boolean hasEqualSourceAddr(AlertNotification other) {
        return ObjectUtil.equals(sourceAddr, other.sourceAddr);
    }
    
    private boolean hasEqualEsmeAddr(AlertNotification other) {
        return ObjectUtil.equals(esmeAddr, other.esmeAddr);
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
        if (!hasEqualEsmeAddr(other)) {
            return false;
        }
        if (esmeAddrNpi != other.esmeAddrNpi)
            return false;
        if (esmeAddrTon != other.esmeAddrTon)
            return false;
        if (!Arrays.equals(optionalParameters, other.optionalParameters))
            return false;
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
