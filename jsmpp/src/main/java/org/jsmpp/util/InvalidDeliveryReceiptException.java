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
package org.jsmpp.util;

/**
 * This exception is throw if there is an invalid format on delivery receipt
 * content.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class InvalidDeliveryReceiptException extends Exception {
    private static final long serialVersionUID = 4069256615018999757L;

    public InvalidDeliveryReceiptException(String message) {
        super(message);
    }

    public InvalidDeliveryReceiptException(String message, Exception cause) {
        super(message, cause);
    }
    
}
