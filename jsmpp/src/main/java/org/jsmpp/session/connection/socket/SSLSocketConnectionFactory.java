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

import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;

/**
 * @author pmoerenhout
 *
 * -Djavax.net.ssl.trustStore=/path/to/your/cacerts
 * -Djavax.net.ssl.trustStorePassword=password
 * -Djavax.net.ssl.trustStoreType=PKCS12
 */
public class SSLSocketConnectionFactory implements ConnectionFactory {
  private static final SSLSocketConnectionFactory connFactory = new SSLSocketConnectionFactory();

  private SocketFactory socketFactory;

  public SSLSocketConnectionFactory() {
    socketFactory = SSLSocketFactory.getDefault();
  }

  public static SSLSocketConnectionFactory getInstance() {
    return connFactory;
  }

  @Override
  public Connection createConnection(String host, int port)
      throws IOException {
    return new SocketConnection(socketFactory.createSocket(host, port));
  }
}