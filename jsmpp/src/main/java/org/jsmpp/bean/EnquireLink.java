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

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 *
 */
public class EnquireLink extends Command {
    private static final long serialVersionUID = -2906795675909484142L;

    public EnquireLink(int sequenceNumber) {
        super();
        this.commandLength = SMPPConstant.PDU_HEADER_LENGTH;
        this.commandId = SMPPConstant.CID_ENQUIRE_LINK;
        this.commandStatus = SMPPConstant.STAT_ESME_ROK;
        this.sequenceNumber = sequenceNumber;
    }

    public EnquireLink() {
        super();
    }

}
