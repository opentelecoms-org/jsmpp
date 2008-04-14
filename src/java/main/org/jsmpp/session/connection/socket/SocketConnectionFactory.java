package org.jsmpp.session.connection.socket;

import java.io.IOException;
import java.net.Socket;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;

/**
 * @author uudashr
 *
 */
public class SocketConnectionFactory implements ConnectionFactory {
    private static final SocketConnectionFactory connFactory = new SocketConnectionFactory();
    
    public static SocketConnectionFactory getInstance() {
        return connFactory;
    }
    
    private SocketConnectionFactory() {
    }
    
    public Connection createConnection(String host, int port)
            throws IOException {
        return new SocketConnection(new Socket(host, port));
    }
}
