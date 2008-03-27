package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * <tt>ResponseHandler</tt> provide interface to handle response of the
 * session routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface ClientResponseHandler extends ResponseHandler {

    /**
     * Process the deliver
     * 
     * @param deliverSm
     * @throws ProcessRequestException
     */
    public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException;

}
