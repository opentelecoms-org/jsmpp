package org.jsmpp.examples;

import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;

/**
 * @author uudashr
 *
 */
public class OpenAndBindExample {
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        String host = "localhost";
        int port = 8056;
        SMPPSession session = new SMPPSession();
        try {
            System.out.println("Connect and bind to " + host + " port " + port);
            session.connectAndBind(host, port, new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        } catch (IOException e) {
            // Failed connect and bind to SMSC
            System.err.println("Failed connect and bind to host");
            e.printStackTrace();
        }
        try { Thread.sleep(10000); } catch (InterruptedException e) {}
        session.unbindAndClose();
    }
    
    
}
