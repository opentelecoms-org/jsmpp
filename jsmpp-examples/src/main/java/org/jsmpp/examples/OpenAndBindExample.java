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
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class OpenAndBindExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAndBindExample.class);
    
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8056;
        SMPPSession session = new SMPPSession();
        try {
            LOGGER.info("Connect and bind to {} port {}", host, port);
            String systemId = session.connectAndBind(host, port, new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
            LOGGER.info("Connected with SMSC with system id {}", systemId);
            try { Thread.sleep(10000); } catch (InterruptedException e) {}
        } catch (IOException e) {
            // Failed connect and bind to SMSC
            LOGGER.error("Failed connect and bind to host", e);
        }
        session.unbindAndClose();
    }

}
