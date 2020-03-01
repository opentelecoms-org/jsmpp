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
import org.jsmpp.util.MessageId;

/**
 * Result of broadcast short message.
 *
 * @author pmoerenhout
 * @version 3.0
 * @since 3.0
 */
public class BroadcastSmResult {

  private final String messageId;
  private final OptionalParameter[] optionalParameters;

  public BroadcastSmResult(final MessageId messageId, final OptionalParameter[] optionalParameters) {
    this.messageId = messageId.getValue();
    this.optionalParameters = optionalParameters;
  }

  public String getMessageId() {
    return messageId;
  }

  public OptionalParameter[] getOptionalParameters() {
    return optionalParameters;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final BroadcastSmResult that = (BroadcastSmResult) o;
    return Objects.equals(messageId, that.messageId) &&
        Arrays.equals(optionalParameters, that.optionalParameters);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(messageId);
    result = 31 * result + Arrays.hashCode(optionalParameters);
    return result;
  }
}
