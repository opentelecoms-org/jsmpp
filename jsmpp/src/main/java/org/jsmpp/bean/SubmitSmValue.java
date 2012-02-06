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
public class SubmitSmValue {
    private String _serviceType;
    private byte _sourceAddrTon;
    private byte _sourceAddrNpi;
    private String _sourceAddr;
    private byte _destAddrTon;
    private byte _destAddrNpi;
    private String _destinationAddr;
    private byte _esmClass;
    private byte _protocolId;
    private byte _priorityFlag;
    private String _scheduleDeliveryTime;
    private String _validityPeriod;
    private byte _registeredDelivery;
    private byte _replaceIfPresent;
    private byte _dataCoding;
    private byte _smDefaultMsgId;
    private String _shortMessage;

    public SubmitSmValue() {

    }

    public SubmitSmValue(String serviceType, byte sourceAddrTon,
            byte sourceAddrNpi, String sourceAddr, byte destAddrTon,
            byte destAddrNpi, String destinationAddr, byte esmClass,
            byte protocolId, byte priorityFlag, String scheduleDeliveryTime,
            String validityPeriod, byte registeredDelivery,
            byte replaceIfPresent, byte dataCoding, byte smDefaultMsgId,
            String shortMessage) {
        _serviceType = serviceType;
        _sourceAddrTon = sourceAddrTon;
        _sourceAddrNpi = sourceAddrNpi;
        _sourceAddr = sourceAddr;
        _destAddrTon = destAddrTon;
        _destAddrNpi = destAddrNpi;
        _destinationAddr = destinationAddr;
        _esmClass = esmClass;
        _protocolId = protocolId;
        _priorityFlag = priorityFlag;
        _scheduleDeliveryTime = scheduleDeliveryTime;
        _validityPeriod = validityPeriod;
        _registeredDelivery = registeredDelivery;
        _replaceIfPresent = replaceIfPresent;
        _dataCoding = dataCoding;
        _smDefaultMsgId = smDefaultMsgId;
        _shortMessage = shortMessage;
    }

    /**
     * @return Returns the dataCoding.
     */
    public byte getDataCoding() {
        return _dataCoding;
    }

    /**
     * @param dataCoding The dataCoding to set.
     */
    public void setDataCoding(byte dataCoding) {
        _dataCoding = dataCoding;
    }

    /**
     * @return Returns the destAddrNpi.
     */
    public byte getDestAddrNpi() {
        return _destAddrNpi;
    }

    /**
     * @param destAddrNpi The destAddrNpi to set.
     */
    public void setDestAddrNpi(byte destAddrNpi) {
        _destAddrNpi = destAddrNpi;
    }

    /**
     * @return Returns the destAddrTon.
     */
    public byte getDestAddrTon() {
        return _destAddrTon;
    }

    /**
     * @param destAddrTon The destAddrTon to set.
     */
    public void setDestAddrTon(byte destAddrTon) {
        _destAddrTon = destAddrTon;
    }

    /**
     * @return Returns the destinationAddr.
     */
    public String getDestinationAddr() {
        return _destinationAddr;
    }

    /**
     * @param destinationAddr The destinationAddr to set.
     */
    public void setDestinationAddr(String destinationAddr) {
        _destinationAddr = destinationAddr;
    }

    /**
     * @return Returns the esmClass.
     */
    public byte getEsmClass() {
        return _esmClass;
    }

    /**
     * @param esmClass The esmClass to set.
     */
    public void setEsmClass(byte esmClass) {
        _esmClass = esmClass;
    }

    /**
     * @return Returns the priorityFlag.
     */
    public byte getPriorityFlag() {
        return _priorityFlag;
    }

    /**
     * @param priorityFlag The priorityFlag to set.
     */
    public void setPriorityFlag(byte priorityFlag) {
        _priorityFlag = priorityFlag;
    }

    /**
     * @return Returns the protocolId.
     */
    public byte getProtocolId() {
        return _protocolId;
    }

    /**
     * @param protocolId The protocolId to set.
     */
    public void setProtocolId(byte protocolId) {
        _protocolId = protocolId;
    }

    /**
     * @return Returns the registeredDelivery.
     */
    public byte getRegisteredDelivery() {
        return _registeredDelivery;
    }

    /**
     * @param registeredDelivery The registeredDelivery to set.
     */
    public void setRegisteredDelivery(byte registeredDelivery) {
        _registeredDelivery = registeredDelivery;
    }

    /**
     * @return Returns the replaceIfPresent.
     */
    public byte getReplaceIfPresent() {
        return _replaceIfPresent;
    }

    /**
     * @param replaceIfPresent The replaceIfPresent to set.
     */
    public void setReplaceIfPresent(byte replaceIfPresent) {
        _replaceIfPresent = replaceIfPresent;
    }

    /**
     * @return Returns the scheduleDeliveryTime.
     */
    public String getScheduleDeliveryTime() {
        return _scheduleDeliveryTime;
    }

    /**
     * @param scheduleDeliveryTime The scheduleDeliveryTime to set.
     */
    public void setScheduleDeliveryTime(String scheduleDeliveryTime) {
        _scheduleDeliveryTime = scheduleDeliveryTime;
    }

    /**
     * @return Returns the serviceType.
     */
    public String getServiceType() {
        return _serviceType;
    }

    /**
     * @param serviceType The serviceType to set.
     */
    public void setServiceType(String serviceType) {
        _serviceType = serviceType;
    }

    /**
     * @return Returns the shortMessage.
     */
    public String getShortMessage() {
        return _shortMessage;
    }

    /**
     * @param shortMessage The shortMessage to set.
     */
    public void setShortMessage(String shortMessage) {
        _shortMessage = shortMessage;
    }

    /**
     * @return Returns the smDefaultMsgId.
     */
    public byte getSmDefaultMsgId() {
        return _smDefaultMsgId;
    }

    /**
     * @param smDefaultMsgId The smDefaultMsgId to set.
     */
    public void setSmDefaultMsgId(byte smDefaultMsgId) {
        _smDefaultMsgId = smDefaultMsgId;
    }

    /**
     * @return Returns the sourceAddr.
     */
    public String getSourceAddr() {
        return _sourceAddr;
    }

    /**
     * @param sourceAddr The sourceAddr to set.
     */
    public void setSourceAddr(String sourceAddr) {
        _sourceAddr = sourceAddr;
    }

    /**
     * @return Returns the sourceAddrNpi.
     */
    public byte getSourceAddrNpi() {
        return _sourceAddrNpi;
    }

    /**
     * @param sourceAddrNpi The sourceAddrNpi to set.
     */
    public void setSourceAddrNpi(byte sourceAddrNpi) {
        _sourceAddrNpi = sourceAddrNpi;
    }

    /**
     * @return Returns the sourceAddrTon.
     */
    public byte getSourceAddrTon() {
        return _sourceAddrTon;
    }

    /**
     * @param sourceAddrTon The sourceAddrTon to set.
     */
    public void setSourceAddrTon(byte sourceAddrTon) {
        _sourceAddrTon = sourceAddrTon;
    }

    /**
     * @return Returns the validityPeriod.
     */
    public String getValidityPeriod() {
        return _validityPeriod;
    }

    /**
     * @param validityPeriod The validityPeriod to set.
     */
    public void setValidityPeriod(String validityPeriod) {
        _validityPeriod = validityPeriod;
    }
}
