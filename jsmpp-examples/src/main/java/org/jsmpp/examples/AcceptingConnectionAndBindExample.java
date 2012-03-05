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

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;

/**
 * @author uudashr
 *
 */
public class AcceptingConnectionAndBindExample {
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            System.out.println("Listening ...");
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(8056);
            
            // accepting connection, session still in OPEN state
            SMPPServerSession session = sessionListener.accept();
            System.out.println("Accept connection");
            
            try {
                BindRequest request = session.waitForBind(5000);
                System.out.println("Receive bind request");
                
                if (request.getSystemId().equals("test") && 
                        request.getPassword().equals("test")) {
                    
                    // accepting request and send bind response immediately
                    System.out.println("Accepting bind request, interface version is " + request.getInterfaceVersion());
                    request.accept("sys");
                    
                    try { Thread.sleep(20000); } catch (InterruptedException e) {}
                } else {
                    System.out.println("Rejecting bind request");
                    request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                }
            } catch (TimeoutException e) {
                System.out.println("No binding request made after 5000 millisecond");
                e.printStackTrace();
            }
            
            
            if (session.getSessionState().isBound()) {
                System.out.println("Closing session");
                session.unbindAndClose();
            }
            System.out.println("Closing session listener");
            sessionListener.close();
        } catch (PDUStringException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
