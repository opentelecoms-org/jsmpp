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

import org.jsmpp.bean.OptionalParameter;

/**
 * @author uudashr
 *
 */
public class BindResult {
    private String messageId;
    private OptionalParameter[] optionalParameter;
    
    public BindResult(String messageId, OptionalParameter[] optionalParameter) {
        this.messageId = messageId;
        this.optionalParameter = optionalParameter;
    }

    public String getMessageId() {
        return messageId;
    }

    public OptionalParameter[] getOptionalParameter() {
        return optionalParameter;
    }
}
