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

import org.jsmpp.session.connection.ServerConnection;
import org.jsmpp.session.connection.ServerConnectionFactory;

/**
 * @author uudashr
 *
 */
public class ServerSocketConnectionFactory implements ServerConnectionFactory {
    
    public ServerConnection listen(int port) throws IOException {
        return new ServerSocketConnection(new ServerSocket(port));
    }
    
    public ServerConnection listen(int port, int timeout) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        ss.setSoTimeout(timeout);
        return new ServerSocketConnection(ss);
    }
    
    public ServerConnection listen(int port, int timeout, int backlog) throws IOException {
        ServerSocket ss = new ServerSocket(port, backlog);
        ss.setSoTimeout(timeout);
        return new ServerSocketConnection(ss);
    }
}
