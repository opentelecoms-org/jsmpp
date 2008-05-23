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
        return startTime = System.currentTimeMillis();
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
