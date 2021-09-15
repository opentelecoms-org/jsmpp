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
 * The listening trough getting the bind request should take less than session
 * initiation timer, otherwise if there is network open has been requested, ESME
 * will close the connection. Accepting the bind request should take less than
 * transaction timer or ESME will issue a timeout.
 *
 *
 * @author uudashr
 *
 */
public class OutboundSMPPServerSessionListener {
    private final int port;
    private final ServerConnection serverConn;
    private int initiationTimer = 5000;
    private int pduProcessorDegree = 3;
    private SessionStateListener sessionStateListener;
    private GenericMessageReceiverListener messageReceiverListener;
    private OutboundServerMessageReceiverListener outboundServerMessageReceiverListener;

    public OutboundSMPPServerSessionListener(int port) throws IOException {
        this(port, new ServerSocketConnectionFactory());
    }

    public OutboundSMPPServerSessionListener(int port, ServerConnectionFactory serverConnFactory)
        throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port);
    }

    public OutboundSMPPServerSessionListener(int port, int timeout, ServerConnectionFactory serverConnFactory)
        throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port, timeout);
    }

    public OutboundSMPPServerSessionListener(int port, int timeout, int backlog, ServerConnectionFactory serverConnFactory)
        throws IOException {
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
     * @param timeout the timeout in milliseconds
     * @throws IOException if an input or output error occurs.
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

    public void setSessionStateListener(SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }

    public GenericMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }

    public void setMessageReceiverListener(GenericMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    public OutboundServerMessageReceiverListener getOutboundServerMessageReceiverListener() {
        return outboundServerMessageReceiverListener;
    }

    public void setOutboundServerMessageReceiverListener(OutboundServerMessageReceiverListener outboundServerMessageReceiverListener) {
        this.outboundServerMessageReceiverListener = outboundServerMessageReceiverListener;
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
     * @return the accepted {@link SMPPOutboundServerSession}.
     * @throws SocketTimeoutException if timeout reach with no session accepted.
     * @throws IOException if there is an input or output error.
     * @see SMPPServerSession
     * @see BindRequest
     */
    public SMPPOutboundServerSession accept() throws IOException {
        Connection conn = serverConn.accept();
        conn.setSoTimeout(initiationTimer);
        return new SMPPOutboundServerSession(conn, sessionStateListener,
                messageReceiverListener, outboundServerMessageReceiverListener,
                pduProcessorDegree);
    }

    public void close() throws IOException {
        serverConn.close();
    }
}
