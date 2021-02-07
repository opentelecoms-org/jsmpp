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
import org.jsmpp.session.ResponseHandler;
import org.jsmpp.session.SMPPSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is closed state implementation of {@link SMPPSessionState}. This
 * session state on SMPP specification context, implemented on since version 5.0,
 * but we can also use this.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.0
 *
 */
class SMPPSessionClosed implements SMPPSessionState {
    private static final String INVALID_PROCESS_FOR_CLOSED_SESSION = "Invalid process for closed session state";
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionClosed.class);

    @Override
    public SessionState getSessionState() {
        return SessionState.CLOSED;
    }

    @Override
    public void processBindResp(SMPPSessionContext sessionContext, Command pduHeader, byte[] pdu,
                                ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processSubmitMultiResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processDataSm(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processDataSmResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processCancelSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processReplaceSmResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processAlertNotification(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) {
        logger.error("Receiving alert_notification while on invalid closed state");
    }

    @Override
    public void processBroadcastSmResp(Command pduHeader, byte[] pdu,
                                  ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processCancelBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

    @Override
    public void processQueryBroadcastSmResp(Command pduHeader, byte[] pdu,
                                       ResponseHandler responseHandler) throws IOException {
        throw new IOException(INVALID_PROCESS_FOR_CLOSED_SESSION);
    }

}
