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
 * @author uudashr
 *
 */
public class CancelSm extends Command {
    private static final long serialVersionUID = -6290826230167091177L;
    
    private String serviceType;
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private byte destAddrTon;
    private byte destAddrNpi;
    private String destinationAddress;
    
    public CancelSm() {
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

    public byte getDestAddrTon() {
        return destAddrTon;
    }

    public void setDestAddrTon(byte destAddrTon) {
        this.destAddrTon = destAddrTon;
    }

    public byte getDestAddrNpi() {
        return destAddrNpi;
    }

    public void setDestAddrNpi(byte destAddrNpi) {
        this.destAddrNpi = destAddrNpi;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public void setDestinationAddress(String destAddress) {
        this.destinationAddress = destAddress;
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
        final CancelSm cancelSm = (CancelSm) o;
        return sourceAddrTon == cancelSm.sourceAddrTon &&
            sourceAddrNpi == cancelSm.sourceAddrNpi &&
            destAddrTon == cancelSm.destAddrTon &&
            destAddrNpi == cancelSm.destAddrNpi &&
            Objects.equals(serviceType, cancelSm.serviceType) &&
            Objects.equals(messageId, cancelSm.messageId) &&
            Objects.equals(sourceAddr, cancelSm.sourceAddr) &&
            Objects.equals(destinationAddress, cancelSm.destinationAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), serviceType, messageId, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddress);
    }
}
