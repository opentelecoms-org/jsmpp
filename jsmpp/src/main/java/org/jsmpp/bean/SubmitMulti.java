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

/**
 * @author uudashr
 *
 */
public class SubmitMulti extends Command {
    private static final long serialVersionUID = -6800953916456361473L;
    
    private String serviceType;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private DestinationAddress[] destAddresses;
    private byte esmClass;
    private byte protocolId;
    private byte priorityFlag;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private byte registeredDelivery;
    private byte replaceIfPresentFlag;
    private byte dataCoding;
    private byte smDefaultMsgId;
    private byte[] shortMessage;
    private OptionalParameter[] optionalParameters;

    public SubmitMulti() {
        super();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
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

    public DestinationAddress[] getDestAddresses() {
        return destAddresses;
    }

    public void setDestAddresses(DestinationAddress[] destAddresses) {
        this.destAddresses = destAddresses;
    }

    public byte getEsmClass() {
        return esmClass;
    }

    public void setEsmClass(byte esmClass) {
        this.esmClass = esmClass;
    }

    public byte getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(byte protocolId) {
        this.protocolId = protocolId;
    }

    public byte getPriorityFlag() {
        return priorityFlag;
    }

    public void setPriorityFlag(byte priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    public String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public byte getRegisteredDelivery() {
        return registeredDelivery;
    }

    public void setRegisteredDelivery(byte registeredDelivery) {
        this.registeredDelivery = registeredDelivery;
    }

    public byte getReplaceIfPresentFlag() {
        return replaceIfPresentFlag;
    }

    public void setReplaceIfPresentFlag(byte replaceIfPresentFlag) {
        this.replaceIfPresentFlag = replaceIfPresentFlag;
    }

    public byte getDataCoding() {
        return dataCoding;
    }

    public void setDataCoding(byte dataCoding) {
        this.dataCoding = dataCoding;
    }

    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    public byte[] getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(byte[] shortMessage) {
        this.shortMessage = shortMessage;
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
        result = prime * result + Arrays.hashCode(destAddresses);
        result = prime * result + Arrays.hashCode(optionalParameters);
        result = prime
                * result
                + ((scheduleDeliveryTime == null) ? 0 : scheduleDeliveryTime
                        .hashCode());
        result = prime * result
                + ((serviceType == null) ? 0 : serviceType.hashCode());
        result = prime * result + Arrays.hashCode(shortMessage);
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
        result = prime * result
                + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
        return result;
    }

    private boolean hasEqualSourceAddr(SubmitMulti other) {
        if (sourceAddr == null) {
            if (other.sourceAddr != null) {
                return false;
            }
        }
        return sourceAddr.equals(other.sourceAddr);
    }

    private boolean hasEqualScheduleDeliveryTime(SubmitMulti other) {
        if (scheduleDeliveryTime == null) {
            if (other.scheduleDeliveryTime != null)
                return false;
        }
        return scheduleDeliveryTime.equals(other.scheduleDeliveryTime);
    }

    private boolean hasEqualValidityPeriod(SubmitMulti other) {
        if (validityPeriod == null) {
            if (other.validityPeriod != null) {
                return false;
            }
        }
        return validityPeriod.equals(other.validityPeriod);
    }

    private boolean hasEqualSystemType(SubmitMulti other) {
        if (serviceType == null) {
            if (other.serviceType != null)
                return false;
        }
        return serviceType.equals(other.serviceType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SubmitMulti other = (SubmitMulti)obj;
        if (!Arrays.equals(destAddresses, other.destAddresses)) {
            return false;
        }
        if (!hasEqualSourceAddr(other)) {
            return false;
        }
        if (sourceAddrNpi != other.sourceAddrNpi) {
            return false;
        }
        if (sourceAddrTon != other.sourceAddrTon) {
            return false;
        }
        if (dataCoding != other.dataCoding)
            return false;
        if (esmClass != other.esmClass)
            return false;
        if (!Arrays.equals(optionalParameters, other.optionalParameters))
            return false;
        if (priorityFlag != other.priorityFlag)
            return false;
        if (protocolId != other.protocolId)
            return false;
        if (registeredDelivery != other.registeredDelivery)
            return false;
        if (replaceIfPresentFlag != other.replaceIfPresentFlag)
            return false;
        if (!hasEqualScheduleDeliveryTime(other)) {
            return false;
        }
        if (!hasEqualValidityPeriod(other)) {
            return false;
        }
        if (!hasEqualSystemType(other)) {
            return false;
        }
        if (!Arrays.equals(shortMessage, other.shortMessage))
            return false;
        if (smDefaultMsgId != other.smDefaultMsgId)
            return false;
        return true;
    }
}
