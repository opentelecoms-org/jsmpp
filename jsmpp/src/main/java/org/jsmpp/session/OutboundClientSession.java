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

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;

/**
 * This interface provides all operation that the outbound client session can do. It
 * doesn't distinct the operation of specific session type (Transmitter,
 * Receiver) it's just like Transceiver. The distinction might should be
 * recognized in a different way, such as by user code when they do a binding or
 * by throwing exception when invoking illegal operation.
 *
 * @author uudashr
 */
public interface OutboundClientSession extends Session {

  /**
   * Open connection and outbind immediately. The default
   * timeout is 1 minute.
   *
   * @param host     is the ESME host address.
   * @param port     is the ESME listen port.
   * @param systemId is the system id.
   * @param password is the password.
   * @return the received bind request
   * @throws IOException if there is an IO error found.
   */
  BindRequest connectAndOutbind(String host, int port,
                                String systemId, String password) throws IOException;

  /**
   * Open connection and outbind immediately with specified timeout. The default
   * timeout is 1 minutes.
   *
   * @param host     is the ESME host address.
   * @param port     is the ESME listen port.
   * @param systemId is the system id.
   * @param password is the password.
   * @param timeout  is the timeout.
   * @return the received bind request
   * @throws IOException if there is an IO error found.
   */
  BindRequest connectAndOutbind(String host, int port,
                                String systemId, String password, long timeout) throws IOException;

  /**
   * Open connection and outbind immediately.
   *
   * @param host         is the ESME host address.
   * @param port         is the ESME listen port.
   * @param outbindParam is the outbind parameters.
   * @return the SMSC system id.
   * @throws IOException if there is an IO error found.
   */
  BindRequest connectAndOutbind(String host, int port,
                                OutbindParameter outbindParam)
      throws IOException;

  /**
   * Open connection and outbind immediately.
   *
   * @param host         is the ESME host address.
   * @param port         is the ESME listen port.
   * @param outbindParam is the outbind parameters.
   * @param timeout      is the timeout.
   * @return the SMSC system id.
   * @throws IOException if there is an IO error found.
   */
  BindRequest connectAndOutbind(String host, int port,
                                OutbindParameter outbindParam, long timeout)
      throws IOException;

  /**
   * Get the current message receiver listener that is currently registered for this smpp session.
   *
   * @return The current message receiver listener
   */
  MessageReceiverListener getMessageReceiverListener();

  /**
   * Sets a message receiver listener for this smpp session.
   *
   * @param messageReceiverListener is the new listener
   */
  void setMessageReceiverListener(
      MessageReceiverListener messageReceiverListener);

  void deliverShortMessage(String serviceType,
                           TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                           String sourceAddr, TypeOfNumber destAddrTon,
                           NumberingPlanIndicator destAddrNpi, String destinationAddr,
                           ESMClass esmClass, byte protocolId, byte priorityFlag,
                           RegisteredDelivery registeredDelivery, DataCoding dataCoding,
                           byte[] shortMessage, OptionalParameter... optionalParameters)
      throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException;
}
