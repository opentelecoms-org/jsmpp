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
import java.util.Objects;

/**
 * @author pmoerenhout
 *
 */
public class CancelBroadcastSm extends Command {

    private static final long serialVersionUID = 222074085818858942L;

    private String serviceType;
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private OptionalParameter[] optionalParameters;

    public CancelBroadcastSm() {
        super();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
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

    public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass) {
        return OptionalParameters.get(tagClass, optionalParameters);
    }

    public OptionalParameter getOptionalParameter(OptionalParameter.Tag tagEnum) {
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
        int result = Objects.hash(super.hashCode(), serviceType, messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        result = 31 * result + Arrays.hashCode(optionalParameters);
        return result;
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
        final CancelBroadcastSm that = (CancelBroadcastSm) o;
        return sourceAddrTon == that.sourceAddrTon &&
            sourceAddrNpi == that.sourceAddrNpi &&
            Objects.equals(serviceType, that.serviceType) &&
            Objects.equals(messageId, that.messageId) &&
            Objects.equals(sourceAddr, that.sourceAddr) &&
            Arrays.equals(optionalParameters, that.optionalParameters);
    }
}
