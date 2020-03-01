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
        final QuerySmResp that = (QuerySmResp) o;
        return errorCode == that.errorCode &&
            Objects.equals(messageId, that.messageId) &&
            Objects.equals(finalDate, that.finalDate) &&
            messageState == that.messageState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), messageId, finalDate, messageState, errorCode);
    }

}
