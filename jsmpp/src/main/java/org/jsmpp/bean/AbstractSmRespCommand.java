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
public class AbstractSmRespCommand extends Command {
    private static final long serialVersionUID = 4829782792374754685L;
    
    private String messageId;

    public AbstractSmRespCommand() {
        super();
    }

    /**
     * @return Returns the messageId.
     */
    public String getMessageId() {
        return messageId;
    }

    /**
     * @param messageId The messageId to set.
     */
    public void setMessageId(String messageId) {
        this.messageId = messageId;
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
        final AbstractSmRespCommand that = (AbstractSmRespCommand) o;
        return Objects.equals(messageId, that.messageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), messageId);
    }

}
