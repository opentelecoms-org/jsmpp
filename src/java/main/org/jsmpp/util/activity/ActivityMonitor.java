package org.jsmpp.util.activity;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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
    private final Lock lock = new ReentrantLock();
    private final Condition cond = lock.newCondition();
    private final long checkDelay;
    private long checkPeriod;
    private long inactiveThreshold;
    private long lastActivity;
    private ActivityListener activityListener;
    private ActivityChecker activityChecker;
    
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
     * Get the checkPeriod.
     * 
     * @return is the checkPeriod.
     */
    public long getCheckPeriod() {
        return checkPeriod;
    }
    
    /**
     * Set the checkPeriod.
     * 
     * @param checkPeriod is the new checkPeriod.
     */
    public void setCheckPeriod(long checkPeriod) {
        this.checkPeriod = checkPeriod;
    }
    
    /**
     * Get the inactiveThreshold.
     * 
     * @return is the inactiveThreashold.
     */
    public long getInactiveThreshold() {
        return inactiveThreshold;
    }
    
    /**
     * Set the inactiveThreshold.
     * 
     * @param inactiveThreshold is the new inactiveThreshold.
     */
    public void setInactiveThreshold(long inactiveThreshold) {
        this.inactiveThreshold = inactiveThreshold;
    }
    
    /**
     * Notify an activity.
     */
    public void notifyActivity() {
        lock.lock();
        try {
            lastActivity = System.currentTimeMillis();
            cond.signal();
            // FIXME uud: DELETE THIS
            System.out.println("Signalling");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Notify activity and change the checkPeriod.
     * 
     * @param newCheckPeriod is the new checkPeriod value.
     */
    public void notifyActivity(long newCheckPeriod) {
        lock.lock();
        try {
            lastActivity = System.currentTimeMillis();
            checkPeriod = newCheckPeriod;
            cond.signal();
        } finally {
            lock.unlock();
        }
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
        lock.lock();
        try {
            if (activityChecker == null) {
                notifyActivity();
                activityChecker = new ActivityChecker();
                activityChecker.start();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * Stop the activity monitor. If the activity monitor never been started,
     * then it won't give any effect.
     */
    public void stop() {
        lock.lock();
        try {
            if (activityChecker != null) {
                activityChecker.shutdown();
                cond.signal();
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * It's check an activity based on lastActivity and inactiveThreshold. The
     * result of checking is not returned, but we can observe via
     * {@link ActivityListener}
     */
    public void checkActivity() {
        lock.lock();
        try {
            boolean foundActivity = isFoundActivity();
            System.out.println("Found Activiry " + foundActivity);
            if (!foundActivity) {
                System.out.println("Entering block");
                fireInactivity();
            }
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * Get the activity status.
     * 
     * @return <tt>true</tt> if there is an activity found, otherwise <tt>false</tt>.
     */
    public boolean isFoundActivity() {
        lock.lock();
        try {
            final long delay = System.currentTimeMillis() - lastActivity;
            return delay < inactiveThreshold;
        } finally {
            lock.unlock();
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
    
    private class ActivityChecker extends Thread {
        private boolean exit;
        
        public void run() {
            try { Thread.sleep(checkDelay); } catch (InterruptedException e) { }
            while (!isExit()) {
                lock.lock();
                try {
                    // FIXME uud: DELETE THIS
                    System.out.println("Awaiting for " + checkPeriod);
                    long start = System.currentTimeMillis();
                    cond.await(checkPeriod, TimeUnit.MILLISECONDS);
                    long delay = System.currentTimeMillis() - start;
                    // FIXME uud: DELETE THIS
                    System.out.println("Check " + delay);;
                    checkActivity();
                } catch (InterruptedException e) {
                    checkActivity();
                } finally {
                    lock.unlock();
                }
            }
        }
        
        public synchronized boolean isExit() {
            return exit;
        }
        
        public synchronized void shutdown() {
            exit = true;
        }
    }
}
