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
        final ReplaceSm replaceSm = (ReplaceSm) o;
        return sourceAddrTon == replaceSm.sourceAddrTon &&
            sourceAddrNpi == replaceSm.sourceAddrNpi &&
            registeredDelivery == replaceSm.registeredDelivery &&
            smDefaultMsgId == replaceSm.smDefaultMsgId &&
            Objects.equals(messageId, replaceSm.messageId) &&
            Objects.equals(sourceAddr, replaceSm.sourceAddr) &&
            Objects.equals(scheduleDeliveryTime, replaceSm.scheduleDeliveryTime) &&
            Objects.equals(validityPeriod, replaceSm.validityPeriod) &&
            Arrays.equals(shortMessage, replaceSm.shortMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects
            .hash(super.hashCode(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr, scheduleDeliveryTime, validityPeriod, registeredDelivery,
                smDefaultMsgId);
        result = 31 * result + Arrays.hashCode(shortMessage);
        return result;
    }
}
