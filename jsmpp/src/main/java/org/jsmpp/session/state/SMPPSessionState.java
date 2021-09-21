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
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.session.SMPPSessionContext;

/**
 * This class provides the interface to response to every incoming SMPP Command.
 * How the response behavior is, depends on its state, or the
 * implementation of this class.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.0
 */
public interface SMPPSessionState extends GenericSMPPSessionState {

    SMPPSessionState OPEN = new SMPPSessionOpen();
    SMPPSessionState BOUND_RX = new SMPPSessionBoundRX();
    SMPPSessionState BOUND_TX = new SMPPSessionBoundTX();
    SMPPSessionState BOUND_TRX = new SMPPSessionBoundTRX();
    SMPPSessionState UNBOUND = new SMPPSessionUnbound();
    SMPPSessionState CLOSED = new SMPPSessionClosed();

    /**
     * Process the bind response command.
     *
     * @param sessionContext the session context.
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException if an input or output error occurred.
     */
    void processBindResp(SMPPSessionContext sessionContext, Command pduHeader, byte[] pdu,
                         ResponseHandler responseHandler) throws IOException;

    /**
     * Process the submit short message response command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an I/O error found.
     */
    void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;
    
    /**
     * Process a submit multiple message response.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the response handler.
     * @throws IOException if there is an I/O error found.
     */
    void processSubmitMultiResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;
    
    /**
     * Process the query short message response command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

    /**
     * Process the deliver short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

    /**
     * Process the cancel short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

    /**
     * Process the replace short message request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processReplaceSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException;

    /**
     * Process the alert notification request command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete PDU.
     * @param responseHandler is the session handler.
     */
    void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler);

    /**
     * Process the broadcast short message response command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete broadcast_sm PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processBroadcastSmResp(Command pduHeader, byte[] pdu,
                                ResponseHandler responseHandler) throws IOException;


    /**
     * Process the cancel broadcast short message response command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete cancel_broadcast_sm PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processCancelBroadcastSmResp(Command pduHeader, byte[] pdu,
                                      ResponseHandler responseHandler) throws IOException;

    /**
     * Process the query broadcast short message response command.
     *
     * @param pduHeader is the PDU header.
     * @param pdu is the complete query_broadcast_sm PDU.
     * @param responseHandler is the session handler.
     * @throws IOException throw if there is an IO error occur.
     */
    void processQueryBroadcastSmResp(Command pduHeader, byte[] pdu,
                                     ResponseHandler responseHandler) throws IOException;
}
