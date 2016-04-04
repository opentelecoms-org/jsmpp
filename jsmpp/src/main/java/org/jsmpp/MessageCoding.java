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
 * This is an enum const that specifies the message coding (see SMPP specification).
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public enum MessageCoding {
    /**
     * Coding 7-bit.
     */
    CODING_7_BIT,
    
    /**
     * Coding 8-bit.
     */
    CODING_8_BIT,
    
    /**
     * Coding 16-bit.
     */
    CODING_16_BIT;
}
