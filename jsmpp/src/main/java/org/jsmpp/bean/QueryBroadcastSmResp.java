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
 * @author pmoerenhout
 * @since 3.0.0
 */
public class QueryBroadcastSmResp extends Command {

  private static final long serialVersionUID = -1014350676291132877L;

  private String messageId;
  private OptionalParameter[] optionalParameters;

  public QueryBroadcastSmResp() {
    super();
  }

  public String getMessageId() {
    return messageId;
  }

  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass) {
    return OptionalParameters.get(tagClass, optionalParameters);
  }

  public OptionalParameter getOptionalParameter(OptionalParameter.Tag tagEnum) {
    return OptionalParameters.get(tagEnum.code(), optionalParameters);
  }

  public OptionalParameter[] getOptionalParameters() {
    return optionalParameters;
  }

  public void setOptionalParameters(OptionalParameter[] optionalParameters) {
    this.optionalParameters = optionalParameters;
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(super.hashCode(), messageId);
    result = 31 * result + Arrays.hashCode(optionalParameters);
    return result;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof QueryBroadcastSmResp)) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    final QueryBroadcastSmResp that = (QueryBroadcastSmResp) o;
    return Objects.equals(messageId, that.messageId) && Arrays.equals(optionalParameters, that.optionalParameters);
  }
}
