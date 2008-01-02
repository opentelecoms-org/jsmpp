package org.jsmpp.session.connection;

import java.io.IOException;

/**
 * @author uudashr
 *
 */
public interface ServerConnectionFactory {
    ServerConnection listen(int port) throws IOException;
    ServerConnection listen(int port, int timeout) throws IOException;
    public ServerConnection listen(int port, int timeout, int backlog) throws IOException;
}
