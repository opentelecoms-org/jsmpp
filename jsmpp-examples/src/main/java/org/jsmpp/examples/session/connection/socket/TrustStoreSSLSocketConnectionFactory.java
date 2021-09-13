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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnection;

/**
 * This SSLSocketConnectionFactory trust certificates found in the key store.
 *
 * @author pmoerenhout
 */
public class TrustStoreSSLSocketConnectionFactory implements ConnectionFactory {

  private static final String KEY_STORE_PATH = "ssl/keystore.p12";
  private static final char[] KEY_STORE_PASSWORD = "password".toCharArray();

  private SSLSocketFactory sslSocketFactory;

  public TrustStoreSSLSocketConnectionFactory() {
    try {
      KeyStore keyStore = KeyStore.getInstance("PKCS12");
      keyStore.load(this.getClass().getResourceAsStream(KEY_STORE_PATH), KEY_STORE_PASSWORD);
      TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
      trustManagerFactory.init(keyStore);
      SSLContext sslContext = SSLContext.getInstance("TLS");
      sslContext.init(null, trustManagerFactory.getTrustManagers(), null);
      sslSocketFactory = sslContext.getSocketFactory();
    } catch (GeneralSecurityException | IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Connection createConnection(String host, int port)
      throws IOException {
    return new SocketConnection(sslSocketFactory.createSocket(host, port));
  }
}