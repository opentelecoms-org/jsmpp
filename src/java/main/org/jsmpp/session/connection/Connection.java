package org.jsmpp.session.connection;

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
    void close();
}
