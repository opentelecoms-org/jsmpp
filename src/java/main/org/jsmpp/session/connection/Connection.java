package org.jsmpp.session.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Connection object.
 * 
 * @author uudashr
 *
 */
public interface Connection {
    boolean isOpen();
    InputStream getInputStream();
    OutputStream getOutputStream();
    void setSoTimeout(int timeout) throws IOException;
    void close() throws IOException;
}
