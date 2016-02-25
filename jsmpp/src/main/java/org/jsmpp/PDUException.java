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
 * This exception is thrown when there is a failure corresponds to PDU processing.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public class PDUException extends Exception {

    private static final long serialVersionUID = -4168434058788171394L;

    /**
     * Default constructor.
     */
    public PDUException() {
        super("PDUException found");
    }

    /**
     * Construct with specified message and cause.
     * 
     * @param message is the detail message.
     * @param cause is the parent cause.
     */
    public PDUException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Construct with specified message.
     * 
     * @param message is the detail message.
     */
    public PDUException(String message) {
        super(message);
    }

    /**
     * Construct with specified cause.
     * 
     * @param cause is the parent cause.
     */
    public PDUException(Throwable cause) {
        super(cause);
    }
}
