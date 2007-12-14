package org.jsmpp.session.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author uudashr
 *
 */
public class SocketConnection implements Connection {
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    
    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }
    
    @Override
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
        }
    }
    
    @Override
    public boolean isOpen() {
        return !socket.isClosed();
    }
    
    @Override
    public InputStream getInputStream() {
        return in;
    }
    
    @Override
    public OutputStream getOutputStream() {
        return out;
    }
}
