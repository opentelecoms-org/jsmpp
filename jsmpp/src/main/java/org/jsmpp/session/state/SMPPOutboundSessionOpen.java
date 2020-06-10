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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is open state implementation of {@link SMPPOutboundSessionState}. When
 * the session state is open, we only give positive response to bind related intention.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.3
 */
class SMPPOutboundSessionOpen implements SMPPOutboundSessionState {
  private static final Logger logger = LoggerFactory.getLogger(SMPPOutboundSessionOpen.class);
  private static final String INVALID_PROCESS_FOR_OPEN_SESSION = "Invalid process for open session state";
  private static final PDUDecomposer pduDecomposer = new DefaultDecomposer();

  @Override
  public SessionState getSessionState() {
    return SessionState.OPEN;
  }

  @Override
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

  @Override
  public void processDeliverSmResp(Command pduHeader, byte[] pdu,
                                   OutboundResponseHandler responseHandler) throws IOException {
    throw new IOException(INVALID_PROCESS_FOR_OPEN_SESSION);
  }

  @Override
  public void processEnquireLink(Command pduHeader, byte[] pdu,
                                 BaseResponseHandler responseHandler) throws IOException {
    logger.info("Received enquire_link in OPEN state, send negative response");
    responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
        SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
  }

  @Override
  public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
                                     BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected enquire_link_resp"));
    }
  }

  @Override
  public void processGenericNack(Command pduHeader, byte[] pdu,
                                 BaseResponseHandler responseHandler) {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected generic_nack"));
    }
  }

  @Override
  public void processUnbind(Command pduHeader, byte[] pdu,
                            BaseResponseHandler responseHandler) throws IOException {
    logger.info("Received unbind in OPEN state, send negative response");
    responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
        SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
  }

  @Override
  public void processUnbindResp(Command pduHeader, byte[] pdu,
                                BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unexpected unbind_resp"));
    }
  }

  @Override
  public void processUnknownCid(Command pduHeader, byte[] pdu,
                                BaseResponseHandler responseHandler) throws IOException {
    PendingResponse<Command> pendingResp = responseHandler
        .removeSentItem(1);
    if (pendingResp != null) {
      pendingResp.doneWithInvalidResponse(new InvalidResponseException(
          "Receive unknown command_id"));
    }
  }

  @Override
  public void processDataSm(Command pduHeader, byte[] pdu,
                            BaseResponseHandler responseHandler) throws IOException {
    logger.info("Received data_sm in OPEN state, send negative response");
    responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
        SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
  }

  @Override
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
