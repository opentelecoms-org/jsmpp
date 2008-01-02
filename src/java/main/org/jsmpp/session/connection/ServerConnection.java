package org.jsmpp.session.connection;

import java.io.IOException;

/**
 * @author uudashr
 *
 */
public interface ServerConnection {
    Connection accept() throws IOException;
    
    void setSoTimeout(int timeout) throws IOException;
    
    int getSoTimeout() throws IOException;
    
    void close() throws IOException;
}
