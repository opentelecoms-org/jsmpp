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
 * Simple utility class for watching performance of method invocation.
 * 
 * @author uudashr
 *
 */
public class StopWatch {
    private long startTime;
    
    /**
     * Start the stop watch.
     * 
     * @return the current time in millisecond.
     */
    public long start() {
        startTime = System.currentTimeMillis();
        return startTime;
    }
    
    /**
     * Done watching the delay and return the delay between start time to
     * current time.
     * 
     * @return the delay between start time to current time
     */
    public long done() {
        return System.currentTimeMillis() - startTime;
    }
    
}
