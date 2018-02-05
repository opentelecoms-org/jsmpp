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

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.session.OutboundResponseHandler;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;

/**
 * This class is open state implementation of {@link SMPPOutboundSessionState}. When
 * the session state is open, we only give positive response to bind related intention.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.3
 */
class SMPPOutboundSessionOpen implements SMPPOutboundSessionState {
  private static final String INVALID_PROCESS_FOR_OPEN_SESSION = "Invalid process for open session state";
  private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();

  public SessionState getSessionState() {
    return SessionState.OPEN;
  }

  public void processBind(Command pduHeader, byte[] pdu,
                          OutboundResponseHandler responseHandler) throws IOException {
    try {
      Bind bind = pduDecomposer.bind(pdu);
      responseHandler.processBind(bind);
    } catch (PDUStringException e) {
      responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
    } catch (IllegalArgumentException e) {
      responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVCMDID, pduHeader.getSequenceNumber());
    }
  }

  public void processDeliverSmResp(Command pduHeader, byte[] pdu,
                                   OutboundResponseHandler responseHandler) throws IOException {
    throw new IOException(INVALID_PROCESS_FOR_OPEN_SESSION);
  }

  public void processEnquireLink(Command pduHeader, byte[] pdu,
                                 BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(pduHeader.getSequenceNumber());
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected enquire_link"));
    }
  }

  public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
                                     BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected enquire_link_resp"));
    }
  }

  public void processGenericNack(Command pduHeader, byte[] pdu,
                                 BaseResponseHandler responseHandler) {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected generic_nack"));
    }
  }

  public void processUnbind(Command pduHeader, byte[] pdu,
                            BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected unbind"));
    }
  }

  public void processUnbindResp(Command pduHeader, byte[] pdu,
                                BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected unbind_resp"));
    }
  }

  public void processUnknownCid(Command pduHeader, byte[] pdu,
                                BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unknown command_id"));
    }
  }

  public void processDataSm(Command pduHeader, byte[] pdu,
                            BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected data_sm"));
    }
  }

  public void processDataSmResp(Command pduHeader, byte[] pdu,
                                BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected data_sm_resp"));
    }
  }
}
