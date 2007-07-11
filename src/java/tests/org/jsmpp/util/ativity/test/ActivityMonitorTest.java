package org.jsmpp.util.ativity.test;

import java.util.concurrent.atomic.AtomicBoolean;

import junit.framework.TestCase;

import org.jsmpp.util.activity.ActivityListener;
import org.jsmpp.util.activity.ActivityMonitor;

/**
 * Test case of activity monitor.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 *
 */
public class ActivityMonitorTest extends TestCase {
    ActivityMonitor activityMonitor;
    
    @Override
    protected void setUp() throws Exception {
        activityMonitor = new ActivityMonitor(3000, 3000);
        activityMonitor.start();
    }
    
    @Override
    protected void tearDown() throws Exception {
        activityMonitor.stop();
    }
    
    /**
     * Test the inactivity never pass from the given threshold.
     */
    public void xxxxxxxxxxxxxxxxxxtestActive() {
        
        activityMonitor.setActivityListener(new ActivityListener() {
            public void onInactive() {
                fail("This event should never raised since always notify for an activity before reach the inactivity thrshold");
            }
        });
        
        for (int i = 0; i < 5; i++) {
            activityMonitor.notifyActivity();
            takeASleep(2000);
        }
        activityMonitor.checkActivity();
    }
    
    /**
     * Test the inactivity pass from the given threshold.
     */
    public void testInactive() {
        final AtomicBoolean inactivityOccur = new AtomicBoolean();
        activityMonitor.setActivityListener(new ActivityListener() {
            public void onInactive() {
                inactivityOccur.set(true);
            }
        });
        
        for (int i = 0; i < 5; i++) {
            activityMonitor.notifyActivity();
            takeASleep(3001);
        }
        activityMonitor.notifyActivity();
        
        assertTrue("The inactivity event should be raised", inactivityOccur.get());
    }
    
    /**
     * This make sure we have sleep for more than millis.
     * 
     * @param millis is the delay.
     * @return the delay in millis.
     */
    private long takeASleep(long millis) {
        /*
        long start = System.currentTimeMillis();
        long delay = 0;
        try {
            do {
                Thread.sleep(millis - delay);
                delay = System.currentTimeMillis() - start;
            } while (delay <= millis);
        } catch (InterruptedException e) {
        }
        return delay;
        */
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return millis;
    }
}
