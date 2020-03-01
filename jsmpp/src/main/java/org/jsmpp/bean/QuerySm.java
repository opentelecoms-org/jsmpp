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
public class QuerySm extends Command {
    private static final long serialVersionUID = 393203012792088078L;
    
    private String messageId;
    private byte sourceAddrTon;
    private byte sourceAddrNpi;
    private String sourceAddr;
    
    public QuerySm() {
        super();
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
        final QuerySm querySm = (QuerySm) o;
        return sourceAddrTon == querySm.sourceAddrTon &&
            sourceAddrNpi == querySm.sourceAddrNpi &&
            Objects.equals(messageId, querySm.messageId) &&
            Objects.equals(sourceAddr, querySm.sourceAddr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
    }

}
