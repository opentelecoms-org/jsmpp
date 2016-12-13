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

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;

/**
 * @author uudashr
 */
public class OpenAndBindExampleTest {

  public static void main(String[] args) {
    BasicConfigurator.configure();
    String host = "195.8.209.155";
    int port = 8056;
    SMPPSession session = new SMPPSession();
    session.setEnquireLinkTimer(5000);
    try {
      System.out.println("Connect and bind to " + host + " port " + port);
      session.connectAndBind(host, port,
          new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN,
              NumberingPlanIndicator.UNKNOWN, null));
    }
    catch (IOException e) {
      // Failed connect and bind to SMSC
      System.err.println("Failed connect and bind to host");
      e.printStackTrace();
    }
    while (true) {
      try {
        Thread.sleep(5000);
      }
      catch (InterruptedException e) {
      }
      System.out.println("Session state " + session.getSessionState());

//    if (session.getSessionState() == SessionState.OPEN) {
//      session.close();
//    }
//    else if (session.getSessionState().isBound()) {
//      session.unbindAndClose();
//    }
//    else {
//      session.close();
//    }
      if (session.getSessionState() == SessionState.CLOSED) {
        session.close();
        break;
      }

    }
  }

}
