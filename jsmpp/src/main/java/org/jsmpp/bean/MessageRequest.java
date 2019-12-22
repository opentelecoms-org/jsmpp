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
public class MessageRequest extends AbstractSmCommand {
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
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime
                * result
                + ((scheduleDeliveryTime == null) ? 0 : scheduleDeliveryTime
                        .hashCode());
        result = prime * result + Arrays.hashCode(shortMessage);
        result = prime * result
                + ((validityPeriod == null) ? 0 : validityPeriod.hashCode());
        return result;
    }
    
    private boolean hasEqualScheduleDeliveryTime(MessageRequest other) {
        return ObjectUtil.equals(scheduleDeliveryTime, other.scheduleDeliveryTime);
    }
    
    private boolean hasEqualValidityPeriod(MessageRequest other) {
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
        final MessageRequest other = (MessageRequest)obj;
        if (priorityFlag != other.priorityFlag)
            return false;
        if (protocolId != other.protocolId)
            return false;
        if (replaceIfPresent != other.replaceIfPresent)
            return false;
        if (!hasEqualScheduleDeliveryTime(other)) {
            return false;
        }
        if (!Arrays.equals(shortMessage, other.shortMessage))
            return false;
        if (smDefaultMsgId != other.smDefaultMsgId)
            return false;
        if (!hasEqualValidityPeriod(other)) {
            return false;
        }
        return true;
    }
    
    
}
