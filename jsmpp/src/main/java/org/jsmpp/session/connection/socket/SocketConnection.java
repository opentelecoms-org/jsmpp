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
package org.jsmpp.session.connection.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.util.StrictBufferedInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SocketConnection implements Connection {
	private static final Logger logger = LoggerFactory.getLogger(SocketConnection.class);
    
    private final Socket socket;
    private final InputStream in;
    private final OutputStream out;
    
    public SocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new StrictBufferedInputStream(socket.getInputStream(), 65536);// 64 KB buffer
        this.out = socket.getOutputStream();
    }
    
    public void setSoTimeout(int timeout) throws IOException {
        socket.setSoTimeout(timeout);
    }
    
    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
        	logger.warn("Suppressing IOException while closing socket: " + e);
        }
    }
    
    public boolean isOpen() {
        return !socket.isClosed();
    }
    
    public InputStream getInputStream() {
        return in;
    }
    
    public OutputStream getOutputStream() {
        return out;
    }
    
    public InetAddress getInetAddress() {
        return socket.getInetAddress();
    }
}
