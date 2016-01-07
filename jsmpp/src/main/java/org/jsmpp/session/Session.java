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
import org.jsmpp.extra.SessionState;


/**
 * Session describe all abilities of the session. Each created session will be
 * identified with session id and has a state (open, bound, unbound, closed).
 * Every changes of state will be notified and the event can be captured by
 * implementing {@link SessionStateListener} and register it to a session
 * {@link #addSessionStateListener(SessionStateListener)}.
 * 
 * <p>
 * Commonly, every SMPP request has a response. The maximum waiting time can be
 * configured as transaction timer.
 * <ul>
 * <li>{@link #setTransactionTimer(long)}</li>
 * <li>{@link #getTransactionTimer()}</li>
 * </ul>
 * </p>
 * 
 * <p>
 * To terminate the communication with the Message Center gracefully, invoke
 * {@link #unbindAndClose()}. It will send UNBIND command and close the
 * connection. This method will wait the UNBIND_RESP but, negative response
 * will be acceptable and closing connection will be done immediately.
 * </p>
 * 
 * @see SessionState
 * 
 * @author uudashr
 * 
 */
public interface Session {
    
    /**
     * Sending a short message like SUBMIT_SM. This method will blocks until
     * response received or timeout reached. This method simplify operation of
     * sending DATA_SM and receiving DATA_SM_RESP.
     * 
     * @param serviceType is the service_type.
     * @param sourceAddrTon is the source_addr_ton.
     * @param sourceAddrNpi is the source_addr_npi.
     * @param sourceAddr is the source_addr.
     * @param destAddrTon is the dest_addr_ton.
     * @param destAddrNpi is the dest_addr_npi.
     * @param destinationAddr is the destination_address.
     * @param esmClass is the esm_class.
     * @param registeredDelivery is the registered_delivery.
     * @param dataCoding is the data_coding.
     * @param optionalParameters is the optional parameters.
     * @return the response/result of DATA_SM.
     * @throws PDUException if there is invalid PDU parameter found.
     * @throws ResponseTimeoutException if timeout has been reached.
     * @throws InvalidResponseException if response is invalid.
     * @throws NegativeResponseException if negative response received.
     * @throws IOException if there is an I/O error found.
     */
    DataSmResult dataShortMessage(String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass,
            RegisteredDelivery registeredDelivery, DataCoding dataCoding,
            OptionalParameter... optionalParameters) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException;
    
    /**
     * Get session id.
     * 
     * @return the session id.
     */
    String getSessionId();
    
    void setEnquireLinkTimer(int enquireLinkTimer);
    int getEnquireLinkTimer();
    void setTransactionTimer(long transactionTimer);
    long getTransactionTimer();
    SessionState getSessionState();
    void addSessionStateListener(SessionStateListener l);
    void removeSessionStateListener(SessionStateListener l);
    
    /**
     * Get the last reading valid PDU from remote host.
     * 
     * @return the last reading valid PDU from remote host.
     */
    long getLastActivityTimestamp();
    
    /**
     * Forced close connection without sending UNBIND command to Message Center.
     */
    void close();
    
    /**
     * Sending UNBIND and close connection immediately.
     */
    void unbindAndClose();
    
}
