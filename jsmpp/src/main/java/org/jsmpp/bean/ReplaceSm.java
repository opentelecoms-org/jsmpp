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

import org.jsmpp.util.ObjectUtil;

/**
 * @author uudashr
 *
 */
public class ReplaceSm extends Command {
    private static final long serialVersionUID = 3470186872335922365L;
    
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private byte registeredDelivery;
    private byte smDefaultMsgId;
    private byte[] shortMessage;

    public ReplaceSm() {
        super();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime
                * result
                + ((scheduleDeliveryTime == null) ? 0 : scheduleDeliveryTime
                        .hashCode());
        result = prime * result + Arrays.hashCode(shortMessage);
        result = prime * result
                + ((sourceAddr == null) ? 0 : sourceAddr.hashCode());
        result = prime * result
                + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
        return result;
    }

    private boolean hasEqualMessageId(ReplaceSm other) {
        return ObjectUtil.equals(messageId , other.messageId);
    }

    private boolean hasEqualScheduleDeliveryTime(ReplaceSm other) {
        return ObjectUtil.equals(scheduleDeliveryTime, other.scheduleDeliveryTime);
    }

    private boolean hasEqualSourceAddr(ReplaceSm other) {
        return ObjectUtil.equals(sourceAddr, other.sourceAddr);
    }

    private boolean hasEqualValidityPeriod(ReplaceSm other) {
        return ObjectUtil.equals(validityPeriod, other.validityPeriod);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ReplaceSm other = (ReplaceSm)obj;
        if (!hasEqualMessageId(other)) {
            return false;
        }
        if (registeredDelivery != other.registeredDelivery)
            return false;
        if (!hasEqualScheduleDeliveryTime(other)) {
            return false;
        }
        if (!Arrays.equals(shortMessage, other.shortMessage))
            return false;
        if (smDefaultMsgId != other.smDefaultMsgId)
            return false;
        if (!hasEqualSourceAddr(other)) {
            return false;
        }
        if (sourceAddrNpi != other.sourceAddrNpi)
            return false;
        if (sourceAddrTon != other.sourceAddrTon)
            return false;
        if (!hasEqualValidityPeriod(other)) {
            return false;
        }
        return true;
    }
}
