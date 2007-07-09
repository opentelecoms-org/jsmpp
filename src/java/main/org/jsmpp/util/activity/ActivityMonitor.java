package org.jsmpp.util.activity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Activity monitor will monitor an inactivity on each period. The activity will
 * be marked with executing {@link #notifyActivity()}. An inactivity will be
 * notify to {@link ActivityListener}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class ActivityMonitor {
    private final long checkDelay;
    private final long checkPeriod;
    private final long inactiveThreshold;
    private long lastActivity;
    private ActivityListener activityListener;
    private TimerTask monitorTask;
    private Timer timer = new Timer();
    
    /**
     * Construct with specified checkDelay, checkPeriod, and inactiveThreshold.
     * 
     * @param checkDelay is the delay before the first activity checking occur.
     * @param checkPeriod is the delay between activity checking.
     * @param inactiveThreshold is the threshold of an inactivity.
     */
    public ActivityMonitor(long checkDelay, long checkPeriod,
            long inactiveThreshold) {
        this.checkDelay = checkDelay;
        this.checkPeriod = checkPeriod;
        this.inactiveThreshold = inactiveThreshold;
    }
    
    /**
     * Construct with specified checkPeriod and inactiveThreshold.
     * 
     * @param checkPeriod is the delay between activity checking.
     * @param inactiveThreshold is the threshold of an inactivity.
     */
    public ActivityMonitor(long checkPeriod, long inactiveThreshold) {
        this(checkPeriod, checkPeriod, inactiveThreshold);
    }

    /**
     * Notify an activity.
     */
    public void notifyActivity() {
        lastActivity = System.currentTimeMillis();
    }

    /**
     * Set an {@link ActivityListener} to observe an actvity.
     * 
     * @param l is the listener.
     */
    public void setActivityListener(ActivityListener l) {
        activityListener = l;
    }

    /**
     * Start the activity monitor. If the activity monitor already started then
     * it won't give any effect.
     */
    public void start() {
        if (monitorTask == null) {
            lastActivity = System.currentTimeMillis();
            monitorTask = new TimerTask() {
                @Override
                public void run() {
                    checkActivity();
                }
            };
            timer.scheduleAtFixedRate(monitorTask, checkDelay, checkPeriod);
        }
    }

    /**
     * Stop the activity monitor. If the activity monitor never been started,
     * then it won't give any effect.
     */
    public void stop() {
        if (monitorTask != null) {
            timer.cancel();
            monitorTask = null;
        }
    }
    
    /**
     * It's check an activity based on lastActivity and inactiveThreshold. The
     * result of checking is not returned, but we can observe via
     * {@link ActivityListener}
     */
    public void checkActivity() {
        final long delay = System.currentTimeMillis() - lastActivity;
        if (delay > inactiveThreshold) {
            fireInactivity();
        }
    }
    
    /**
     * Raise inactive event.
     */
    private void fireInactivity() {
        if (activityListener != null) {
            activityListener.onInactive();
        }
    }
}
