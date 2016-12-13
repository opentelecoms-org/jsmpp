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

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.OutboundResponseHandler;

/**
 * This class is bound_trx state implementation of {@link SMPPOutboundSessionState}.
 * Response both to transmit and receive related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.3
 * 
 */
class SMPPOutboundSessionBoundTRX extends SMPPOutboundSessionBoundTX implements SMPPOutboundSessionState {
    
    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TRX;
    }

    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
                                     OutboundResponseHandler responseHandler) throws IOException {
        SMPPOutboundSessionBoundRX.processDeliverSmResp0(pduHeader, pdu, responseHandler);
    }
}
