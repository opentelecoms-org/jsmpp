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
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
public class SMPPServerSessionUnbound implements SMPPServerSessionState {
    
    private static final String INVALID_PROCESS_FOR_UNBOUND_SESSION = "Invalid process for unbound session state";

    @Override
    public SessionState getSessionState() {
        return SessionState.UNBOUND;
    }

    @Override
    public void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processQuerySm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processSubmitSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler)
            throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processSubmitMulti(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
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

    @Override
    public void processCancelSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processReplaceSm(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processBroadcastSm(final Command pduHeader, final byte[] pdu,
                                   final ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processCancelBroadcastSm(final Command pduHeader, final byte[] pdu,
                                         final ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }

    @Override
    public void processQueryBroadcastSm(final Command pduHeader, final byte[] pdu,
                                        final ServerResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_UNBOUND_SESSION);
    }
}
