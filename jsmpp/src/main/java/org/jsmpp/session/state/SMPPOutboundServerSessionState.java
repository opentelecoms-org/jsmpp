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
import org.jsmpp.session.OutboundServerResponseHandler;
import org.jsmpp.session.SessionContext;

/**
 * This class is provide interface to response to every incoming SMPP Commands.
 * How the the response behavior is depends to it's states, or the
 * implementation of this class.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.3
 */
public interface SMPPOutboundServerSessionState extends GenericSMPPSessionState {

    SMPPOutboundServerSessionState OPEN = new SMPPOutboundServerSessionOpen();
    SMPPOutboundServerSessionState OUTBOUND = new SMPPOutboundServerSessionOutbound();
    SMPPOutboundServerSessionState BOUND_RX = new SMPPOutboundServerSessionBoundRX();
    SMPPOutboundServerSessionState BOUND_TX = new SMPPOutboundServerSessionBoundTX();
    SMPPOutboundServerSessionState BOUND_TRX = new SMPPOutboundServerSessionBoundTRX();
    SMPPOutboundServerSessionState UNBOUND = new SMPPOutboundServerSessionUnbound();
    SMPPOutboundServerSessionState CLOSED = new SMPPOutboundServerSessionClosed();

    void processOutbind(Command pduHeader, byte[] pdu, OutboundServerResponseHandler responseHandler)
        throws IOException;

    /**
     * Process the bind response command.
     *
     * @param sessionContext is the session context
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException if an input or output error occurred
     */
    void processBindResp(SessionContext sessionContext, Command pduHeader, byte[] pdu, OutboundServerResponseHandler responseHandler)
        throws IOException;

    void processDeliverSm(Command pduHeader, byte[] pdu, OutboundServerResponseHandler responseHandler)
        throws IOException;
}
