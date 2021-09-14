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
package org.jsmpp.examples;

import java.io.IOException;

import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.examples.session.connection.socket.TrustStoreSSLSocketConnectionFactory;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 */
public class OpenAndBindExample {
  private static final Logger log = LoggerFactory.getLogger(OpenAndBindExample.class);

  public static void main(String[] args) throws Exception {

    boolean useSsl = true;
    String host = "localhost";
    int port = 8056;
    /*
     * For SSL, use NoTrustSSLSocketConnectionFactory to accept all selfsigned certs, use SSLSocketConnectionFactory otherwise.
     */
    try (SMPPSession session = useSsl ? new SMPPSession(new TrustStoreSSLSocketConnectionFactory()) : new SMPPSession()) {
      session.setEnquireLinkTimer(30000);
      session.setTransactionTimer(2000);
      try {
        log.info("Connect and bind to {} port {}{}", host, port, useSsl ? " (SSL)" : "");
        String systemId = session
            .connectAndBind(host, port, new BindParameter(BindType.BIND_TRX, "j", "jpwd", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        log.info("Connected with SMSC with system id {}", systemId);

        log.info("Session interface version: {}", session.getInterfaceVersion());
        log.info("Local port: {}", session.getLocalPort());

        try {
          Thread.sleep(60000);
        } catch (InterruptedException e) {
          log.debug("Interrupted");
          //re-interrupt the current thread
          Thread.currentThread().interrupt();
        }
      } catch (IOException e) {
        // Failed connect and bind to SMSC
        log.error("Failed connect and bind to host", e);
      }
      log.debug("Unbind and close session");
      session.unbindAndClose();
    }
  }
}
