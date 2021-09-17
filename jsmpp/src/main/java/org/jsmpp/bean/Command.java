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

import java.io.Serializable;
import java.util.Objects;

import org.jsmpp.util.IntUtil;

/**
 * Class represent SMPP Command. Contains only the header of SMPP PDU.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class Command implements Serializable {
    private static final long serialVersionUID = -5226111982905878489L;
    
    protected int commandLength;
    protected int commandId;
    protected int commandStatus;
    protected int sequenceNumber;

    /**
     * Default constructor.
     */
    public Command() {

    }

    /**
     * Get the command_id.
     * 
     * @return the command_id.
     */
    public int getCommandId() {
        return commandId;
    }

    /**
     * Get the command_id as hex string value.
     * 
     * @return the hex string value of command_id.
     */
    public String getCommandIdAsHex() {
        return IntUtil.toHexString(commandId);
    }

    /**
     * Set the command_id.
     * 
     * @param commandId is the new value of command_id..
     */
    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    /**
     * Get the command_length.
     * 
     * @return the command_length.
     */
    public int getCommandLength() {
        return commandLength;
    }

    /**
     * Set the command_length.
     * 
     * @param commandLength is the new value of command_length.
     */
    public void setCommandLength(int commandLength) {
        this.commandLength = commandLength;
    }

    /**
     * Get the command_status.
     * 
     * @return the command_status.
     */
    public int getCommandStatus() {
        return commandStatus;
    }

    /**
     * Get the command_status as hex string.
     * 
     * @return the hex string value of command_status.
     */
    public String getCommandStatusAsHex() {
        return IntUtil.toHexString(commandStatus);
    }

    /**
     * Set value of command_status.
     * 
     * @param commandStatus is the new value of command_status.
     */
    public void setCommandStatus(int commandStatus) {
        this.commandStatus = commandStatus;
    }

    /**
     * Get the sequence_number.
     * 
     * @return the value of sequence_number.
     */
    public int getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * Set value of sequence_number.
     * 
     * @param sequenceNumber is the new value of sequence_number.
     */
    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    @Override
    public String toString() {
        return "PDUHeader(" + commandLength + ", " + getCommandIdAsHex() + ", " + getCommandStatusAsHex() + ", " + sequenceNumber + ")";
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return Objects.hash(commandLength, commandId, commandStatus, sequenceNumber);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Command)) {
            return false;
        }
        final Command command = (Command) o;
        return commandLength == command.commandLength && commandId == command.commandId && commandStatus == command.commandStatus && sequenceNumber == command.sequenceNumber;
    }
    
}
