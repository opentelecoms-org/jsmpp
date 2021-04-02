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
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.OutboundServerResponseHandler;
import org.jsmpp.session.SessionContext;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is unbound state implementation of {@link SMPPSessionState}. All
 * this method is throw {@link IOException} since when the state is unbound we
 * should not give any positive response.
 * 
 * @author pmoerenhout
 * @version 1.0
 * @since 2.3
 * 
 */
class SMPPOutboundServerSessionUnbound implements SMPPOutboundServerSessionState {
    private static final Logger logger = LoggerFactory.getLogger(SMPPOutboundServerSessionOutbound.class);
    private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();
    private static final String INVALID_PROCESS_FOR_UNBOUND_SESSION = "Invalid process for unbound session state";

    @Override
    public SessionState getSessionState() {
        return SessionState.UNBOUND;
    }

    @Override
    public void processOutbind(Command pduHeader, byte[] pdu,
                            OutboundServerResponseHandler responseHandler) throws IOException
    {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processBindResp(SessionContext sessionContext, Command pduHeader, byte[] pdu,
            OutboundServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
                                 OutboundServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendEnquireLinkResp(pduHeader.getSequenceNumber());
    }

    @Override
    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
            .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for {}", pduHeader);
        }
    }

    @Override
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

}
