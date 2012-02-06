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


/**
 * Sequence number that able to rolling.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class Sequence {
    private int value;

    /**
     * @param start is the start/init value of the sequence.
     */
    public Sequence(int start) {
        value = start;
    }

    /**
     * Return the next value.
     * 
     * @return the next value.
     */
    public synchronized int nextValue() {
        int curValue = value++;
        if (curValue == Integer.MAX_VALUE) {
            value = 1;
        }
        return curValue;
    }

    /**
     * @return the current value.
     */
    public synchronized int currentValue() {
        return value;
    }
}
