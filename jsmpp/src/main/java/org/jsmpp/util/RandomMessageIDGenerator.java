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
 * Generate random alphanumeric
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class RandomMessageIDGenerator implements MessageIDGenerator {
    private Random random;
    
    public RandomMessageIDGenerator() {
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
         * use database sequence convert into hex representation or if not using
         * database using random
         */
        try {
            synchronized (random) {
                return new MessageId(Integer.toString(random.nextInt(Integer.MAX_VALUE), 16));
            }
        } catch (PDUStringException e) {
            throw new RuntimeException("Failed creating message id", e);
        }
    }
}