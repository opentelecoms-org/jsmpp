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

import org.jsmpp.util.StringParameter;

/**
 * Thrown if there is an invalid constraint for the PDU String parameter.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class PDUStringException extends PDUException {
    private static final long serialVersionUID = 5456303478921567516L;
    private final StringParameter parameter;

    /**
     * Construct with specified message and parameter.
     * 
     * @param message is the detail message.
     * @param parameter is the constraint parameter.
     */
    public PDUStringException(String message, StringParameter parameter) {
        super(message);
        this.parameter = parameter;
    }

    /**
     * Get the constraint parameter.
     * 
     * @return the constraint parameter.
     */
    public StringParameter getParameter() {
        return parameter;
    }

    /**
     * Get the error code of the broken constraint.
     * 
     * @return the command status should be returned.
     */
    public int getErrorCode() {
        return parameter.getErrCode();
    }
}
