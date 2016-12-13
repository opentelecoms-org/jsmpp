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
import org.jsmpp.session.OutboundResponseHandler;

/**
 * @author uudashr
 */
public interface SMPPOutboundSessionState extends GenericSMPPSessionState {

  SMPPOutboundSessionState OPEN = new SMPPOutboundSessionOpen();
  SMPPOutboundSessionState BOUND_RX = new SMPPOutboundSessionBoundRX();
  SMPPOutboundSessionState BOUND_TX = new SMPPOutboundSessionBoundTX();
  SMPPOutboundSessionState BOUND_TRX = new SMPPOutboundSessionBoundTRX();
  SMPPOutboundSessionState UNBOUND = new SMPPOutboundSessionUnbound();
  SMPPOutboundSessionState CLOSED = new SMPPOutboundSessionClosed();

  /**
   * Process the bind command received after outbind request.
   *
   * @param pduHeader       is the PDU header.
   * @param pdu             is the complete PDU.
   * @param responseHandler is the session handler.
   * @throws IOException throw if there is an IO error occur.
   */
  void processBind(Command pduHeader, byte[] pdu,
                   OutboundResponseHandler responseHandler) throws IOException;

  void processDeliverSmResp(Command pduHeader, byte[] pdu,
                            OutboundResponseHandler responseHandler) throws IOException;
}
