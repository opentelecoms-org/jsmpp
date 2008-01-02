package org.jsmpp.session.connection.socket;

import java.io.IOException;
import java.net.ServerSocket;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ServerConnection;

/**
 * @author uudashr
 *
 */
public class ServerSocketConnection implements ServerConnection {
    private final ServerSocket serverSocket;
    
    public ServerSocketConnection(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }
    
    public void setSoTimeout(int timeout) throws IOException {
        serverSocket.setSoTimeout(timeout);
    }
    
    public int getSoTimeout() throws IOException {
        return serverSocket.getSoTimeout();
    }
    
    public Connection accept() throws IOException {
        return new SocketConnection(serverSocket.accept());
    }
    
    public void close() throws IOException {
        serverSocket.close();
    }
}
