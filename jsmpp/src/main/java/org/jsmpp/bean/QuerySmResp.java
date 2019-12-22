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

import org.jsmpp.util.ObjectUtil;

/**
 * @author uudashr
 * 
 */
public class QuerySmResp extends Command {
    private static final long serialVersionUID = 8491715207469144080L;
    
    private String messageId;
    private String finalDate;
    private MessageState messageState;
    private byte errorCode;

    public QuerySmResp() {
        super();
    }

    /**
     * @return the errorCode
     */
    public byte getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(byte errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the finalDate
     */
    public String getFinalDate() {
        return finalDate;
    }

    /**
     * @param finalDate the finalDate to set
     */
    public void setFinalDate(String finalDate) {
        this.finalDate = finalDate;
    }

    /**
     * @return the messageId
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the messageState
     */
    public MessageState getMessageState() {
        return messageState;
    }

    /**
     * @param messageState the messageState to set
     */
    public void setMessageState(MessageState messageState) {
        this.messageState = messageState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((finalDate == null) ? 0 : finalDate.hashCode());
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result
                + ((messageState == null) ? 0 : messageState.hashCode());
        return result;
    }
    
    private boolean hasEqualFinalDate(QuerySmResp other) {
        return ObjectUtil.equals(finalDate, other.finalDate);
    }
    
    private boolean hasEqualMessageId(QuerySmResp other) {
        return ObjectUtil.equals(messageId, other.messageId);
    }
    
    private boolean hasEqualMessageState(QuerySmResp other) {
        return ObjectUtil.equals(messageState, other.messageState);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final QuerySmResp other = (QuerySmResp)obj;
        if (errorCode != other.errorCode)
            return false;
        if (!hasEqualFinalDate(other)) {
            return false;
        }
        if (!hasEqualMessageId(other)) {
            return false;
        }
        if (!hasEqualMessageState(other)) {
            return false;
        }
        return true;
    }
    
    
}
