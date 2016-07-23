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

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * This listener will listen to every incoming short message, recognized by
 * deliver_sm command. The logic on this listener should be accomplish in a
 * short time, because the deliver_sm_resp will be processed after the logic
 * executed. Normal logic will be return the deliver_sm_resp with zero valued
 * command_status, or throw {@link ProcessRequestException} that gave non-zero
 * valued command_status (in means negative response) depends on the given error
 * code specified on the {@link ProcessRequestException}.
 *
 * @author pmoerenhout
 * @version 1.0
 * @since 2.0
 */
public interface OutboundServerMessageReceiverListener {

  /**
   * This event raised when a short message received.
   *
   * @param deliverSm is the short message.
   * @param source is the session.
   * @throws ProcessRequestException throw if there should be return Non-OK command_status for the response.
   */
  void onAcceptDeliverSm(DeliverSm deliverSm, SMPPOutboundServerSession source)
      throws ProcessRequestException;
}