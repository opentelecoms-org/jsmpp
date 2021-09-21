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
import java.util.concurrent.TimeoutException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class AcceptingConnectionAndBindExample {
    private static final Logger log = LoggerFactory.getLogger(AcceptingConnectionAndBindExample.class);
    
    public static void main(String[] args) {
        int port = 8056;
        try {
            log.info("Listening on port {} ...", port);
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(port);
            
            // accepting connection, session still in OPEN state
            SMPPServerSession session = sessionListener.accept();
            log.info("Accept connection");
            
            try {
                BindRequest request = session.waitForBind(5000);
                log.info("Received bind request");
                
                if ("test".equals(request.getSystemId()) &&
                        "test".equals(request.getPassword())) {

                    // accepting request and send bind response immediately
                    log.info("Accepting bind request, interface version is {}", request.getInterfaceVersion());
                    request.accept("sys");
                    
                    try {
                        Thread.sleep(20000);
                    }
                    catch (InterruptedException e) {
                        //re-interrupt the current thread
                        Thread.currentThread().interrupt();
                    }
                } else {
                    log.info("Rejecting bind request");
                    request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                }
            } catch (TimeoutException e) {
                log.error("No binding request received after 5000 millisecond", e);
            }
            
            if (session.getSessionState().isBound()) {
                log.info("Closing session");
                session.unbindAndClose();
            }
            log.info("Closing session listener");
            sessionListener.close();
        } catch (PDUStringException e) {
            log.error("PDUStringException", e);
        } catch (IOException e) {
            log.error("IOException", e);
        }
    }
}
