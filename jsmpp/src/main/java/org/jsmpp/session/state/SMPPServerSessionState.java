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
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
public interface SMPPServerSessionState extends GenericSMPPSessionState {

    SMPPServerSessionState OPEN = new SMPPServerSessionOpen();
    SMPPServerSessionState BOUND_RX = new SMPPServerSessionBoundRX();
    SMPPServerSessionState BOUND_TX = new SMPPServerSessionBoundTX();
    SMPPServerSessionState BOUND_TRX = new SMPPServerSessionTRX();
    SMPPServerSessionState UNBOUND = new SMPPServerSessionUnbound();
    SMPPServerSessionState CLOSED = new SMPPServerSessionClosed();
    
    /**
     * Process the bind request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an IO error occur.
     */
    void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;

    /**
     * Process the submit short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if an input or output error occured.
     */
    void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    void processSubmitMulti(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the cancel short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;

    /**
     * Process the replace short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processReplaceSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException;

    void processBroadcastSm(Command pduHeader, byte[] pdu,
                         ServerResponseHandler responseHandler) throws IOException;

    void processCancelBroadcastSm(Command pduHeader, byte[] pdu,
                            ServerResponseHandler responseHandler) throws IOException;

    void processQueryBroadcastSm(Command pduHeader, byte[] pdu,
                            ServerResponseHandler responseHandler) throws IOException;

}
