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
public class SubmitMultiResult {

    private String messageId;
    private UnsuccessDelivery[] unsuccessDeliveries;
    
    public SubmitMultiResult(String messageId,
            UnsuccessDelivery... unsuccessDeliveries) {
        this.messageId = messageId;
        this.unsuccessDeliveries = unsuccessDeliveries;
    }

    public String getMessageId() {
        return messageId;
    }

    public UnsuccessDelivery[] getUnsuccessDeliveries() {
        return unsuccessDeliveries;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SubmitMultiResult that = (SubmitMultiResult) o;
        return Objects.equals(messageId, that.messageId) &&
            Arrays.equals(unsuccessDeliveries, that.unsuccessDeliveries);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(messageId);
        result = 31 * result + Arrays.hashCode(unsuccessDeliveries);
        return result;
    }

}
