package org.jsmpp.util.activity;

/**
 * This class is an observer to an activity monitor, contains onInactive event
 * that raised when an inactivity happen.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface ActivityListener {
    
    /**
     * Raise when an inactivity happen.
     */
    public void onInactive();
}
