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
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
class DummyResponseHandler implements ServerResponseHandler {
    private boolean connectionClosed;
    
    public void notifyUnbonded() {
    }

    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
        return null;
    }

    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber)
            throws IOException {
    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    public void sendUnbindResp(int sequenceNumber) throws IOException {
    }
    
    public void processBind(Bind bind) {
    }

    public QuerySmResult processQuerySm(QuerySm querySm)
            throws ProcessRequestException {
        return null;
    }
    
    public SubmitMultiResult processSubmitMulti(SubmitMulti submitMulti)
            throws ProcessRequestException {
        return null;
    }
    
    public void sendSubmitMultiResponse(SubmitMultiResult submiitMultiResult,
            int sequenceNumber) throws IOException {
    }
    
    public MessageId processSubmitSm(SubmitSm submitSm)
            throws ProcessRequestException {
        return null;
    }

    public void sendBindResp(String systemId, InterfaceVersion interfaceVersion, BindType bindType, int sequenceNumber)
            throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    public void sendSubmitSmResponse(MessageId messageId, int sequenceNumber)
            throws IOException {

    }
    
    public DataSmResult processDataSm(DataSm dataSm)
            throws ProcessRequestException {
        return null;
    }
    
    public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
            throws IOException {
    }
    
    public void processCancelSm(CancelSm cancelSm)
            throws ProcessRequestException {
    }
    
    public void sendCancelSmResp(int sequenceNumber) throws IOException {
    }
    
    public void sendQuerySmResp(String messageId, String finalDate,
            MessageState messageState, byte errorCode, int sequenceNumber)
            throws IOException {
    }
    
    public void processReplaceSm(ReplaceSm replaceSm)
            throws ProcessRequestException {
    }
    
    public void sendReplaceSmResp(int sequenceNumber) throws IOException {
    }
    public void closeConnection() {
        connectionClosed = true;
    }
}