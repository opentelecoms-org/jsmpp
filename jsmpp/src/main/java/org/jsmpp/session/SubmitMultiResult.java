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
package org.jsmpp.session;

import java.util.Arrays;
import java.util.Objects;

import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.UnsuccessDelivery;

/**
 * @author uudashr
 *
 */
public class SubmitMultiResult {

    private final String messageId;
    /* UnsuccessDeliveries were added in SMPP 5.0 */
    private final UnsuccessDelivery[] unsuccessDeliveries;
    /* OptionalParameters were added in SMPP 5.0 */
    private final OptionalParameter[] optionalParameters;

    public SubmitMultiResult(String messageId) {
        this.messageId = messageId;
        this.unsuccessDeliveries = null;
        this.optionalParameters = null;
    }
    
    public SubmitMultiResult(String messageId,
                             UnsuccessDelivery[] unsuccessDeliveries,
                             OptionalParameter[] optionalParameters ) {
        this.messageId = messageId;
        this.unsuccessDeliveries = unsuccessDeliveries;
        this.optionalParameters = optionalParameters;
    }

    public String getMessageId() {
        return messageId;
    }

    public UnsuccessDelivery[] getUnsuccessDeliveries() {
        return unsuccessDeliveries;
    }

    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmitMultiResult)) {
            return false;
        }
        final SubmitMultiResult that = (SubmitMultiResult) o;
        return Objects.equals(messageId, that.messageId) && Arrays.equals(unsuccessDeliveries,
            that.unsuccessDeliveries) && Arrays.equals(optionalParameters, that.optionalParameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(messageId);
        result = 31 * result + Arrays.hashCode(unsuccessDeliveries);
        result = 31 * result + Arrays.hashCode(optionalParameters);
        return result;
    }
}
