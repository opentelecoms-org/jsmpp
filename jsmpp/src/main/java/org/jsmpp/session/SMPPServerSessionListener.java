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
import java.net.SocketTimeoutException;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ServerConnection;
import org.jsmpp.session.connection.ServerConnectionFactory;
import org.jsmpp.session.connection.socket.ServerSocketConnectionFactory;

/**
 * This object responsible to for new SMPP Session request from ESME. It will
 * listen on specified port.
 * 
 * <pre>
 * SMPPServerSession session = listener.accept();
 * BindRequest bindReq = session.waitForBind(5000);
 * 
 * if (checkPassword(bindReq)) {
 *     bindReq.accept(&quot;sys&quot;);
 * } else {
 *     bindReq.reject(SMPPConstant.STAT_ESME_RINVPASWD);
 * }
 * </pre>
 * 
 * <p>
 * The listening through getting the bind request should take less than the session
 * initiation timer, otherwise if there is network open has been requested, ESME
 * will close the connection. Accepting the bind request should take less than
 * transaction timer or ESME will issue timeout.
 *
 * @author uudashr
 */
public class SMPPServerSessionListener implements AutoCloseable {
    private final int port;
    private final ServerConnection serverConn;
    private int initiationTimer = 5000;
    private int pduProcessorDegree = 3;
    private int queueCapacity = 100;
    private SessionStateListener sessionStateListener;
    private ServerMessageReceiverListener messageReceiverListener;
    private ServerResponseDeliveryListener responseDeliveryListener;
    
    public SMPPServerSessionListener(int port) throws IOException {
        this(port, new ServerSocketConnectionFactory());
    }
    
    public SMPPServerSessionListener(int port,  
            ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port);
    }
    
    public SMPPServerSessionListener(int port, int timeout, 
            ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port, timeout);
    }
    
    public SMPPServerSessionListener(int port, int timeout, int backlog,
            ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port, timeout, backlog);
    }
    
    public int getTimeout() throws IOException {
        return serverConn.getSoTimeout();
    }
    
    /**
     * Timeout listening. When timeout reach and connection request didn't
     * arrive then {@link SocketTimeoutException} will be thrown but the
     * listener still valid.
     * 
     * @param timeout is the timeout in milliseconds.
     * @throws IOException if an input or output error occurred
     */
    public void setTimeout(int timeout) throws IOException {
        serverConn.setSoTimeout(timeout);
    }
    
    public void setPduProcessorDegree(int pduProcessorDegree) {
        this.pduProcessorDegree = pduProcessorDegree;
    }
    
    public int getPduProcessorDegree() {
        return pduProcessorDegree;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(final int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public int getPort() {
        return port;
    }
    
    public int getInitiationTimer() {
        return initiationTimer;
    }
    
    public void setInitiationTimer(int initiationTimer) {
        this.initiationTimer = initiationTimer;
    }
    
    public SessionStateListener getSessionStateListener() {
        return sessionStateListener;
    }
    
    public void setSessionStateListener(
            SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }
    
    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }
    
    public void setMessageReceiverListener(
            ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }
    
    public void setResponseDeliveryListener(
            ServerResponseDeliveryListener responseDeliveryListener) {
        this.responseDeliveryListener = responseDeliveryListener;
    }
    
    /**
     * Accept session request from client. The session state is still OPEN. To
     * communicate with ESME properly binding request should be accepted.
     * 
     * <pre>
     * SMPPServerSession session = listener.accept();
     * BindRequest bindReq = session.waitForBind(5000);
     * 
     * if (checkPassword(bindReq)) {
     *     bindReq.accept(&quot;sys&quot;);
     * } else {
     *     bindReq.reject(SMPPConstant.STAT_ESME_RINVPASWD);
     * }
     * </pre>
     * 
     * @return the accepted {@link SMPPServerSession}.
     * @throws SocketTimeoutException if timeout reach with no session accepted.
     * @throws IOException if there is an IO error occur.
     * @see SMPPServerSession
     * @see BindRequest
     */
    public SMPPServerSession accept() throws IOException {
        Connection conn = serverConn.accept();
        conn.setSoTimeout(initiationTimer);
        return new SMPPServerSession(conn, sessionStateListener,
                messageReceiverListener, responseDeliveryListener,
                pduProcessorDegree, queueCapacity);
    }
    
    public void close() throws IOException {
        serverConn.close();
    }
}
