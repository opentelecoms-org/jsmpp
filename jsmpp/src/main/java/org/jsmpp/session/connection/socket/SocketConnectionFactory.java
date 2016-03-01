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
import java.net.Socket;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;

/**
 * @author uudashr
 *
 */
public class SocketConnectionFactory implements ConnectionFactory {
    private static final SocketConnectionFactory connFactory = new SocketConnectionFactory();
    
    private SocketConnectionFactory() {
    }
    
    public static SocketConnectionFactory getInstance() {
        return connFactory;
    }

    @Override
    public Connection createConnection(String host, int port)
            throws IOException {
        return new SocketConnection(new Socket(host, port));
    }
}
