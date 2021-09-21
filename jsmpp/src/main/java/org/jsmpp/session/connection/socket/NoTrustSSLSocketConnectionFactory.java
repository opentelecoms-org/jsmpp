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
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;

/**
 * This SSLSocketConnectionFactory accepts selfsigned, revoked, etc. certificates.
 * Only use for development and testing.
 *
 * @author pmoerenhout
 */
public class NoTrustSSLSocketConnectionFactory implements ConnectionFactory {

  private SocketFactory socketFactory;

  public NoTrustSSLSocketConnectionFactory() {
    try {
      SSLContext sslContext = SSLContext.getInstance("TLS");
      TrustManager tm = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }
      };
      sslContext.init(null, new TrustManager[]{ tm }, null);
      socketFactory = sslContext.getSocketFactory();
    } catch (NoSuchAlgorithmException | KeyManagementException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Connection createConnection(String host, int port)
      throws IOException {
    return new SocketConnection(socketFactory.createSocket(host, port));
  }

}