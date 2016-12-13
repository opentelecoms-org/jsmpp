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

/**
 * This interface provides all operation that the client session can do. It
 * doesn't distinct the operation of specific session type (Transmitter,
 * Receiver) it's just like Transceiver. The distinction might should be
 * recognized in a different way, such as by user code when they do a binding or
 * by throwing exception when invoking illegal operation.
 * 
 * @author uudashr
 * 
 */
public interface OutboundServerSession extends Session {

  /**
   * Get the current message receiver listener that is currently registered for this smpp session.
   * @return The current message receiver listener
   */
  OutboundServerMessageReceiverListener getOutboundServerMessageReceiverListener();

  /**
   * Sets a message receiver listener for this smpp session.
   * @param outboundServerMessageReceiverListener is the new listener
   */
  void setOutboundServerMessageReceiverListener(
      OutboundServerMessageReceiverListener outboundServerMessageReceiverListener);
}
