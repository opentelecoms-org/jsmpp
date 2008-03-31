package org.jsmpp.session;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ServerConnection;
import org.jsmpp.session.connection.ServerConnectionFactory;
import org.jsmpp.session.connection.socket.ServerSocketConnectionFactory;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionListener {
    private final int port;
    private final ServerConnection serverConn;
    private int initiationTimer = 5000;
    private SessionStateListener sessionStateListener;
    private ServerMessageReceiverListener messageReceiverListener;

    public SMPPServerSessionListener(int port) throws IOException {
        this(port, new ServerSocketConnectionFactory());
    }

    public SMPPServerSessionListener(int port, ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port);
    }

    public SMPPServerSessionListener(int port, int timeout, ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port, timeout);
    }

    public SMPPServerSessionListener(int port, int timeout, int backlog, ServerConnectionFactory serverConnFactory) throws IOException {
        this.port = port;
        serverConn = serverConnFactory.listen(port, timeout, backlog);
    }

    public int getTimeout(int timeout) throws IOException {
        return serverConn.getSoTimeout();
    }

    /**
     * Timeout listening. When timeout reach and connection request didn't
     * arrive then {@link SocketTimeoutException} will be thrown but the
     * listener still valid.
     * 
     * @param timeout
     * @throws IOException
     */
    public void setTimeout(int timeout) throws IOException {
        serverConn.setSoTimeout(timeout);
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

    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }

    public void setMessageReceiverListener(ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    /**
     * Accept session request from client.
     * 
     * @return the accepted {@link SMPPServerSession}.
     * @throws SocketTimeoutException
     *             if timeout reach with no session accepted.
     * @throws IOException
     *             if there is an IO error occur.
     */
    public BaseServerSession accept() throws IOException {
        Connection conn = serverConn.accept();
        conn.setSoTimeout(initiationTimer);
        return newSession(conn);
    }

    protected BaseServerSession newSession(Connection conn) {
        return new SMPPServerSession(conn, sessionStateListener, messageReceiverListener);
    }

    public void close() throws IOException {
        serverConn.close();
    }
}
