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

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public class DataSmResult {

    private int commandStatus = SMPPConstant.STAT_ESME_ROK;
    private final String messageId;
    private final OptionalParameter[] optionalParameters;

    DataSmResult(String messageId, OptionalParameter[] optionalParameters) {
        this.messageId = messageId;
        this.optionalParameters = optionalParameters;
    }
    
    public DataSmResult(MessageId messageId, OptionalParameter[] optionalParameters) {
        this(messageId.getValue(), optionalParameters);
    }

    public String getMessageId() {
        return messageId;
    }

    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }

    public int getCommandStatus() {
        return commandStatus;
    }

    /**
     * data_sm_resp allows a non zero command_status to be sent with message_id
     * and other optional parameters. This method allows such a non zero command_status
     * to be set.
     * @param commandStatus
     */
    public void setCommandStatus(int commandStatus) {
        this.commandStatus = commandStatus;
    }
}
