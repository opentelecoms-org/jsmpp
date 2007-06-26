package org.jsmpp.util;


/**
 * @author uudashr
 *
 * @param <S> session id
 */
public interface SessionIDGenerator<S> {
	
	/**
	 * @return
	 */
	public S newSessionId();
}
