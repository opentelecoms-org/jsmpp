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
package org.jsmpp.examples.session.connection.socket;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;

import org.jsmpp.session.connection.ServerConnection;
import org.jsmpp.session.connection.ServerConnectionFactory;
import org.jsmpp.session.connection.socket.ServerSocketConnection;

/**
 * @author pmoerenhout
 */
public class KeyStoreSSLServerSocketConnectionFactory implements ServerConnectionFactory {

  private static final String KEY_STORE_PATH = "keystore.p12";
  private static final char[] KEY_STORE_PASSWORD = "password".toCharArray();

  private SSLServerSocketFactory sslServerSocketFactory;

  public KeyStoreSSLServerSocketConnectionFactory() {
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(new FileInputStream(KEY_STORE_PATH), KEY_STORE_PASSWORD);
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD);
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
      sslServerSocketFactory = sslContext.getServerSocketFactory();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ServerConnection listen(int port) throws IOException {
    ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(port);
    return new ServerSocketConnection(serverSocket);
  }

  @Override
  public ServerConnection listen(int port, int timeout) throws IOException {
    ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(port);
    serverSocket.setSoTimeout(timeout);
    return new ServerSocketConnection(serverSocket);
  }

  @Override
  public ServerConnection listen(int port, int timeout, int backlog) throws IOException {
    ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(port, backlog);
    serverSocket.setSoTimeout(timeout);
    return new ServerSocketConnection(serverSocket);
  }

}
