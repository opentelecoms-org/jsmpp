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
package org.jsmpp.examples.gateway;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SessionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This are implementation of {@link Gateway}. This gateway will reconnect for a
 * specified interval if the session are closed.
 * 
 * @author uudashr
 * 
 */
public class AutoReconnectGateway implements Gateway {
    private static final Logger logger = LoggerFactory.getLogger(AutoReconnectGateway.class);
    private SMPPSession session = null;
    private String remoteIpAddress;
    private int remotePort;
    private BindParameter bindParam;
    private long reconnectInterval = 5000L; // 5 seconds

    /**
     * Construct auto reconnect gateway with specified ip address, port and SMPP
     * Bind parameters.
     * 
     * @param remoteIpAddress is the SMSC IP address.
     * @param remotePort is the SMSC port.
     * @param bindParam is the SMPP Bind parameters.
     * @throws IOException
     */
    public AutoReconnectGateway(String remoteIpAddress, int remotePort,
            BindParameter bindParam) throws IOException {
        this.remoteIpAddress = remoteIpAddress;
        this.remotePort = remotePort;
        this.bindParam = bindParam;
        session = newSession();
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.examples.gateway.Gateway#submitShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public String submitShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
            DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
        
        return getSession().submitShortMessage(serviceType, sourceAddrTon,
                sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi,
                destinationAddr, esmClass, protocolId, priorityFlag,
                scheduleDeliveryTime, validityPeriod, registeredDelivery,
                replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
                optionalParameters);
    }

    /**
     * Create new {@link SMPPSession} complete with the
     * {@link SessionStateListenerImpl}.
     * 
     * @return the {@link SMPPSession}.
     * @throws IOException if the creation of new session failed.
     */
    private SMPPSession newSession() throws IOException {
        SMPPSession tmpSession = new SMPPSession(remoteIpAddress, remotePort, bindParam);
        tmpSession.addSessionStateListener(new SessionStateListenerImpl());
        return tmpSession;
    }

    /**
     * Get the session. If the session still null or not in bound state, then IO
     * exception will be thrown.
     * 
     * @return the valid session.
     * @throws IOException if there is no valid session or session creation is
     *         invalid.
     */
    private SMPPSession getSession() throws IOException {
        if (session == null) {
            logger.info("Initiate session for the first time to " + remoteIpAddress + ":" + remotePort);
            session = newSession();
        } else if (!session.getSessionState().isBound()) {
            throw new IOException("We have no valid session yet");
        }
        return session;
    }
    
    /**
     * Reconnect session after specified interval.
     * 
     * @param timeInMillis is the interval.
     */
    private void reconnectAfter(final long timeInMillis) {
        new Thread() {
            @Override
            public void run() {
                logger.info("Schedule reconnect after " + timeInMillis + " millis");
                try {
                    Thread.sleep(timeInMillis);
                } catch (InterruptedException e) {
                }
                
                int attempt = 0;
                while (session == null || session.getSessionState().equals(SessionState.CLOSED)) {
                    try {
                        logger.info("Reconnecting attempt #" + (++attempt) + "...");
                        session = newSession();
                    } catch (IOException e) {
                        logger.error("Failed opening connection and bind to " + remoteIpAddress + ":" + remotePort, e);
                        // wait for a second
                        try { Thread.sleep(1000); } catch (InterruptedException ee) {}
                    }
                }
            }
        }.start();
    }

    /**
     * This class will receive the notification from {@link SMPPSession} for the
     * state changes. It will schedule to re-initialize session.
     * 
     * @author uudashr
     * 
     */
    private class SessionStateListenerImpl implements SessionStateListener {
        public void onStateChange(SessionState newState, SessionState oldState,
        		Session source) {
            if (newState.equals(SessionState.CLOSED)) {
                logger.info("Session closed");
                reconnectAfter(reconnectInterval);
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        BasicConfigurator.configure();
        Gateway gateway = new AutoReconnectGateway("localhost", 8056, 
                new BindParameter(BindType.BIND_TRX, "sms", "sms", "sms", 
                        TypeOfNumber.UNKNOWN, NumberingPlanIndicator.ISDN, "8080"));
        
        while (true) {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }
    }
}
