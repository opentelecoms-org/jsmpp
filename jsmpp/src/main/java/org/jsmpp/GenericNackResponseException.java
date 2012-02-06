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
package org.jsmpp;

/**
 * @author uudashr
 *
 */
public class GenericNackResponseException extends InvalidResponseException {
    private static final long serialVersionUID = -5938563802952633189L;
    private final int commandStatus;
    
    public GenericNackResponseException(String message, int commandStatus) {
        super(message);
        this.commandStatus = commandStatus;
    }
    
    public int getCommandStatus() {
        return commandStatus;
    }
}
