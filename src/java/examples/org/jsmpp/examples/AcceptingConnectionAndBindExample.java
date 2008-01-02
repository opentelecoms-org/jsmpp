package org.jsmpp.examples;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.SMPPConstant;
import org.jsmpp.session.BindRequest;
import org.jsmpp.session.SMPPServerSession;
import org.jsmpp.session.SMPPServerSessionListener;

/**
 * @author uudashr
 *
 */
public class AcceptingConnectionAndBindExample {
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        try {
            System.out.println("Listening ...");
            SMPPServerSessionListener sessionListener = new SMPPServerSessionListener(8056);
            
            // accepting connection, session still in OPEN state
            SMPPServerSession session = sessionListener.accept();
            System.out.println("Accept connection");
            
            try {
                BindRequest request = session.waitForBind(5000);
                System.out.println("Receive bind request");
                
                if (request.getBindParameter().getSystemId().equals("test") && 
                        request.getBindParameter().getPassword().equals("test")) {
                    
                    // accepting request and send bind response immediately
                    System.out.println("Accepting bind request");
                    request.accept();
                    
                    try { Thread.sleep(20000); } catch (InterruptedException e) {}
                } else {
                    System.out.println("Rejecting bind request");
                    request.reject(SMPPConstant.STAT_ESME_RINVPASWD);
                }
            } catch (TimeoutException e) {
                System.out.println("No binding request made after 5000 millisecond");
                e.printStackTrace();
            }
            
            
            if (session.getSessionState().isBound()) {
                System.out.println("Closing session");
                session.unbindAndClose();
            }
            System.out.println("Closing session listener");
            sessionListener.close();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
