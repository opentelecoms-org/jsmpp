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
package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.BroadcastSm;
import org.jsmpp.bean.CancelBroadcastSm;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.QueryBroadcastSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;

/**
 * @author uudashr
 * @author pmoerenhout
 *
 */
class DummyResponseHandler implements ServerResponseHandler {

    private boolean connectionClosed;

    @Override
    public void notifyUnbonded() {
    }

    @Override
    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
        return null;
    }

    @Override
    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
    }

    @Override
    public void sendGenericNack(int commandStatus, int sequenceNumber)
            throws IOException {
    }

    @Override
    public void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    @Override
    public void sendUnbindResp(int sequenceNumber) throws IOException {
    }

    @Override
    public void processBind(Bind bind) {
    }

    @Override
    public QuerySmResult processQuerySm(QuerySm querySm)
            throws ProcessRequestException {
        return null;
    }

    @Override
    public SubmitMultiResult processSubmitMulti(SubmitMulti submitMulti)
            throws ProcessRequestException {
        return null;
    }

    @Override
    public void sendSubmitMultiResponse(SubmitMultiResult submitMultiResult,
            int sequenceNumber) throws IOException {
    }

    @Override
    public SubmitSmResult processSubmitSm(SubmitSm submitSm)
            throws ProcessRequestException {
        return null;
    }

    @Override
    public void sendBindResp(String systemId, InterfaceVersion interfaceVersion, BindType bindType, int sequenceNumber)
            throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    @Override
    public void sendSubmitSmResponse(SubmitSmResult submitSmResult, int sequenceNumber)
            throws IOException {
    }

    @Override
    public DataSmResult processDataSm(DataSm dataSm)
            throws ProcessRequestException {
        return null;
    }

    @Override
    public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
            throws IOException {
    }

    @Override
    public void processCancelSm(CancelSm cancelSm)
            throws ProcessRequestException {
    }

    @Override
    public void sendCancelSmResp(int sequenceNumber) throws IOException {
    }

    @Override
    public void sendQuerySmResp(String messageId, String finalDate,
            MessageState messageState, byte errorCode, int sequenceNumber)
            throws IOException {
    }

    @Override
    public void processReplaceSm(ReplaceSm replaceSm)
            throws ProcessRequestException {
    }

    @Override
    public void sendReplaceSmResp(int sequenceNumber) throws IOException {
    }

    @Override
    public BroadcastSmResult processBroadcastSm(final BroadcastSm broadcastSm) throws ProcessRequestException {
        return null;
    }

    @Override
    public void sendBroadcastSmResp(BroadcastSmResult broadcastSmResult, int sequenceNumber)
        throws IOException {
    }

    @Override
    public void processCancelBroadcastSm(final CancelBroadcastSm cancelBroadcastSm) throws ProcessRequestException {

    }

    @Override
    public void sendCancelBroadcastSmResp(int sequenceNumber)
        throws IOException {
    }

    @Override
    public QueryBroadcastSmResult processQueryBroadcastSm(final QueryBroadcastSm queryBroadcastSm) throws ProcessRequestException {
        return null;
    }

    @Override
    public void sendQueryBroadcastSmResp(final QueryBroadcastSmResult queryBroadcastSmResult, final int sequenceNumber)
        throws IOException {
    }

    @Override
    public void processEnquireLink(final EnquireLink enquireLink) {

    }

    void closeConnection() {
        connectionClosed = true;
    }

}