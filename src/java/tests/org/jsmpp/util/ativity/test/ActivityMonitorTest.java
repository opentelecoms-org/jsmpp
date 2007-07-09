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
        activityMonitor = new ActivityMonitor(50, 3000);
        activityMonitor.start();
    }
    
    @Override
    protected void tearDown() throws Exception {
        activityMonitor.stop();
    }
    
    /**
     * Test the inactivity never pass from the given threshold.
     */
    public void testActive() {
        
        activityMonitor.setActivityListener(new ActivityListener() {
            public void onInactive() {
                fail("This event should never raised since always notify for an activity before reach the inactivity thrshold");
            }
        });
        
        for (int i = 0; i < 5; i++) {
            activityMonitor.notifyActivity();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
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
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        }
        
        activityMonitor.notifyActivity();
        try {
            Thread.sleep(3001);
        } catch (InterruptedException e) {
        }
        activityMonitor.checkActivity();
        assertTrue("The inactivity event should be raised", inactivityOccur.get());
    }
    
}
