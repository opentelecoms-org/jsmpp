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
package org.jsmpp.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

import org.jsmpp.PDUStringException;

/**
 * Generate random numeric message id
 * 
 * @author pmoerenhout
 * @version 1.0
 * @since 2.5
 * 
 */
public class RandomDecimalMessageIDGenerator implements MessageIDGenerator {
    private Random random;

    public RandomDecimalMessageIDGenerator() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            random = new Random();
        }
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.MessageIDGenerator#newMessageId()
     */
    @Override
    public MessageId newMessageId() {
        /*
         * use random into decimal representation
         */
        try {
            synchronized (random) {
                return new MessageId(String.format("%010d", random.nextInt(Integer.MAX_VALUE)));
            }
        } catch (PDUStringException e) {
            throw new RuntimeException("Failed creating message id", e);
        }
    }
}