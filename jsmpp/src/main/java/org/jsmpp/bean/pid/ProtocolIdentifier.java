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
 */
package org.jsmpp.bean.pid;

/**
 * TP-Protocol Identifier
 * <p>
 * The Protocol-Identifier is the information element by which the SM-TL either refers to the higher layer protocol being
 * used, or indicates interworking with a certain type of telematic device.
 * The Protocol-Identifier information element makes use of a particular field in the message types SMS-SUBMIT,
 * SMS-SUBMIT-REPORT for RP-ACK, SMS-DELIVER DELIVER, SMS-DELIVER-REPORT for RP-ACK,
 * SMS_STATUS_REPORT and SMS-COMMAND TP-Protocol-Identifier (TP-PID).
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme (kohme@gigsky.com)</a>
 */
public interface ProtocolIdentifier {

    /**
     * Get the byte representation of Protocol Identifier.
     *
     * @return the byte representation of Protocol Identifier.
     */
    byte toByte();
}
