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


/**
 * @author uudashr
 *
 */
public class CancelSm extends Command {
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((destinationAddress == null) ? 0 : destinationAddress
                        .hashCode());
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result
                + ((serviceType == null) ? 0 : serviceType.hashCode());
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
        return result;
    }
    
    private boolean hasEqualDestAddress(CancelSm other) {
        if (destinationAddress == null) {
            if (other.destinationAddress != null)
                return false;
        } 
        return destinationAddress.equals(other.destinationAddress);
    }
    
    private boolean hasEqualServiceType(CancelSm other) {
        if (serviceType == null) {
            if (other.serviceType != null) {
                return false;
            }
        }
        return serviceType.equals(other.serviceType);
    }
    
    private boolean hasEqualSourceAddr(CancelSm other) {
        if (sourceAddr == null) {
            if (other.sourceAddr != null) {
                return false;
            }
        }
        return sourceAddr.equals(other.sourceAddr);
    }
    
    private boolean hasEqualMessageId(CancelSm other) {
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } 
        return messageId.equals(other.messageId);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CancelSm other = (CancelSm)obj;
        if (destAddrNpi != other.destAddrNpi)
            return false;
        if (destAddrTon != other.destAddrTon)
            return false;
        if (!hasEqualDestAddress(other)) {
            return false;
        }
        if (!hasEqualMessageId(other)) { 
            return false;
        }
        if (!hasEqualServiceType(other)) {
            return false;
        }
        if (sourceAddrNpi != other.sourceAddrNpi)
            return false;
        if (sourceAddrTon != other.sourceAddrTon)
            return false;
        if (!hasEqualSourceAddr(other)) {
            return false;
        }
        return true;
    }
    
    
}
