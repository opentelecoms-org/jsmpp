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
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.QuerySmResult;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;
import org.jsmpp.session.ServerMessageReceiverListener;
import org.jsmpp.session.Session;
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;

/**
 * @author uudashr
 *
 */
public class ReceiveSubmittedMessageExample {
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            
            
            // prepare generator of Message ID
            final MessageIDGenerator messageIdGenerator = new RandomMessageIDGenerator();
            
            // prepare the message receiver
            ServerMessageReceiverListener messageReceiverListener = new ServerMessageReceiverListener() {
                public MessageId onAcceptSubmitSm(SubmitSm submitSm,
                        SMPPServerSession source)
                        throws ProcessRequestException {
                    System.out.println("Receiving message : " + new String(submitSm.getShortMessage()));
                    // need message_id to response submit_sm
                    return messageIdGenerator.newMessageId();
                }
                
                public QuerySmResult onAcceptQuerySm(QuerySm querySm,
                        SMPPServerSession source)
                        throws ProcessRequestException {
                    return null;
                }
                
                public SubmitMultiResult onAcceptSubmitMulti(
                        SubmitMulti submitMulti, SMPPServerSession source)
                        throws ProcessRequestException {
                    return null;
                }
                
                public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
                        throws ProcessRequestException {
                    return null;
                }
                
                public void onAcceptCancelSm(CancelSm cancelSm,
                        SMPPServerSession source)
                        throws ProcessRequestException {
                }
                
                public void onAcceptReplaceSm(ReplaceSm replaceSm,
                        SMPPServerSession source)
                        throws ProcessRequestException {
                }
            };
            
            System.out.println("Listening ...");
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(8056);
            // set all default ServerMessageReceiverListener for all accepted SMPPServerSessionListener
            sessionListener.setMessageReceiverListener(messageReceiverListener);
            
            // accepting connection, session still in OPEN state
            SMPPServerSession session = sessionListener.accept();
            // or we can set for each accepted session session.setMessageReceiverListener(messageReceiverListener)
            System.out.println("Accept connection");
            
            try {
                BindRequest request = session.waitForBind(5000);
                System.out.println("Receive bind request");
                
                if (request.getBindParameter().getSystemId().equals("test") && 
                        request.getBindParameter().getPassword().equals("test")) {
                    
                    // accepting request and send bind response immediately
                    System.out.println("Accepting bind request");
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
            
            System.out.println("Closing session");
            session.unbindAndClose();
            System.out.println("Closing session listener");
            sessionListener.close();
        } catch (PDUStringException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
