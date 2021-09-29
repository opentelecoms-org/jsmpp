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
public class MessageRequest extends AbstractSmCommand {

    private static final long serialVersionUID = 5020181832372374307L;

    protected byte protocolId;
    protected byte priorityFlag;
    protected String scheduleDeliveryTime;
    protected String validityPeriod;
    protected byte replaceIfPresent;
    protected byte smDefaultMsgId;
    protected byte[] shortMessage;
    
    public MessageRequest() {
        super();
    }

    /**
     * @return the priorityFlag
     */
    public byte getPriorityFlag() {
        return priorityFlag;
    }

    /**
     * @param priorityFlag the priorityFlag to set
     */
    public void setPriorityFlag(byte priorityFlag) {
        this.priorityFlag = priorityFlag;
    }

    /**
     * @return the protocolId
     */
    public byte getProtocolId() {
        return protocolId;
    }

    /**
     * @param protocolId the protocolId to set
     */
    public void setProtocolId(byte protocolId) {
        this.protocolId = protocolId;
    }

    /**
     * @return the replaceIfPresent
     */
    public byte getReplaceIfPresent() {
        return replaceIfPresent;
    }

    /**
     * @param replaceIfPresent the replaceIfPresent to set
     */
    public void setReplaceIfPresent(byte replaceIfPresent) {
        this.replaceIfPresent = replaceIfPresent;
    }

    /**
     * @return the scheduleDeliveryTime
     */
    public String getScheduleDeliveryTime() {
        return scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime the scheduleDeliveryTime to set
     */
    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        this.scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return the shortMessage
     */
    public byte[] getShortMessage() {
        return shortMessage;
    }

    /**
     * @param shortMessage the shortMessage to set
     */
    public void setShortMessage(byte[] shortMessage) {
        this.shortMessage = shortMessage;
    }
    
    /**
     * @return the smDefaultMsgId
     */
    public byte getSmDefaultMsgId() {
        return smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId the smDefaultMsgId to set
     */
    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        this.smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return the validityPeriod
     */
    public String getValidityPeriod() {
        return validityPeriod;
    }

    /**
     * @param validityPeriod the validityPeriod to set
     */
    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
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
        final MessageRequest that = (MessageRequest) o;
        return protocolId == that.protocolId &&
            priorityFlag == that.priorityFlag &&
            replaceIfPresent == that.replaceIfPresent &&
            smDefaultMsgId == that.smDefaultMsgId &&
            Objects.equals(scheduleDeliveryTime, that.scheduleDeliveryTime) &&
            Objects.equals(validityPeriod, that.validityPeriod) &&
            Arrays.equals(shortMessage, that.shortMessage);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), protocolId, priorityFlag, scheduleDeliveryTime, validityPeriod, replaceIfPresent, smDefaultMsgId);
        result = 31 * result + Arrays.hashCode(shortMessage);
        return result;
    }
}
