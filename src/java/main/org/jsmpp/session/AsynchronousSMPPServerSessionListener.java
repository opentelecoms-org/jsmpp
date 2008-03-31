package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ServerConnectionFactory;

public class AsynchronousSMPPServerSessionListener extends SMPPServerSessionListener {

    public AsynchronousSMPPServerSessionListener(int port, int timeout, int backlog, ServerConnectionFactory serverConnFactory) throws IOException {
        super(port, timeout, backlog, serverConnFactory);
    }

    @Override
    protected BaseServerSession newSession(Connection conn) {
        return new AsynchronousSMPPServerSession(conn, getSessionStateListener(), getMessageReceiverListener());
    }
}
