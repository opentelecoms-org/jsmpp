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
package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.session.SMPPSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is general bound state implementation of {@link SMPPSessionState}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
abstract class SMPPSessionBound extends AbstractGenericSMPPSessionBound implements SMPPSessionState {
    private static final Logger log = LoggerFactory.getLogger(SMPPSessionBound.class);

    @Override
    public void processBindResp(SMPPSessionContext sessionContext, Command pduHeader, byte[] pdu,
                                ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }

    static void processDeliverSm0(Command pduHeader, byte[] pdu,
                                  ResponseHandler responseHandler) throws IOException {
        try {
            DeliverSm deliverSm = pduDecomposer.deliverSm(pdu);
            responseHandler.processDeliverSm(deliverSm);
            responseHandler.sendDeliverSmResp(SMPPConstant.STAT_ESME_ROK, pduHeader.getSequenceNumber(), deliverSm.getId());
        } catch (PDUStringException e) {
            log.error("Failed decomposing deliver_sm", e);
            responseHandler.sendGenericNack(e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessRequestException e) {
            log.error("Failed processing deliver_sm", e);
            responseHandler.sendDeliverSmResp(e.getErrorCode(), pduHeader.getSequenceNumber(), null);
        }
    }
}
