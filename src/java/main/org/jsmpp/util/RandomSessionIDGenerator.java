package org.jsmpp.util;

import java.util.Random;

/**
 * Session id generator.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class RandomSessionIDGenerator implements SessionIDGenerator<String> {
    private Random _random = new Random();

    /**
     * Generate new session id. Max 64 char.
     */
    public String newSessionId() {
        byte[] b = new byte[32];
        _random.nextBytes(b);
        return HexUtil.conventBytesToHexString(b);
    }
}
