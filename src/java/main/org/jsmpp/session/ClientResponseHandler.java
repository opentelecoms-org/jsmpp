package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;

/**
 * <tt>ResponseHandler</tt> provide interface to handle response of the
 * session routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface ClientResponseHandler {

    public void processDeliverSm(ClientSession session, DeliverSm deliverSm);

}
