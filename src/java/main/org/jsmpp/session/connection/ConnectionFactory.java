package org.jsmpp.session.connection;

import java.io.IOException;

/**
 * @author uudashr
 *
 */
public interface ConnectionFactory {
    Connection createConnection(String host, int port) throws IOException;
}
