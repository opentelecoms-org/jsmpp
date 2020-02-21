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

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import org.jsmpp.session.connection.ServerConnection;
import org.jsmpp.session.connection.ServerConnectionFactory;

/**
 * @author pmoerenhout
 */
public class SSLServerSocketConnectionFactory implements ServerConnectionFactory {

  /*
   * Set the following Java properties to set default SSL keystore
   *
   * -Djavax.net.ssl.keyStoreType=PKCS12
   * -Djavax.net.ssl.keyStorePassword=password
   * -Djavax.net.ssl.keyStore=/path/to/your/keystore.p12
   *
   */

  public ServerConnection listen(int port) throws IOException {
    ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
    ServerSocket serverSocket = serverSocketFactory.createServerSocket(port);
    return new ServerSocketConnection(serverSocket);
  }

  public ServerConnection listen(int port, int timeout) throws IOException {
    ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
    ServerSocket serverSocket = serverSocketFactory.createServerSocket(port);
    serverSocket.setSoTimeout(timeout);
    return new ServerSocketConnection(serverSocket);
  }

  public ServerConnection listen(int port, int timeout, int backlog) throws IOException {
    ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
    ServerSocket serverSocket = serverSocketFactory.createServerSocket(port, backlog);
    serverSocket.setSoTimeout(timeout);
    return new ServerSocketConnection(serverSocket);
  }
}
