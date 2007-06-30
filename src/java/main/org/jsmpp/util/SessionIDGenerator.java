package org.jsmpp.util;

/**
 * The session id generator.
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 * @param <S> session id
 */
public interface SessionIDGenerator<S> {

    /**
     * @return
     */
    public S newSessionId();
}
