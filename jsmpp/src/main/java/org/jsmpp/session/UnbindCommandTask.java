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

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;

/**
 * @author uudashr
 *
 */
public class UnbindCommandTask extends AbstractSendCommandTask {

    public static final String COMMAND_NAME_UNBIND = "unbind";
    
    public UnbindCommandTask(PDUSender pduSender) {
        super(pduSender);
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws IOException {
        pduSender.sendUnbind(out, sequenceNumber);
    }
    
    public String getCommandName() {
        return COMMAND_NAME_UNBIND;
    }
}
