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
