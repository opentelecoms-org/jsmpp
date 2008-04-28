package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * <tt>ResponseHandler</tt> provide interface to handle response of the session
 * routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface ResponseHandler extends BaseResponseHandler {

    /**
     * Process the deliver
     * 
     * @param deliverSm
     * @throws ProcessRequestException
     */
    void processDeliverSm(DeliverSm deliverSm)
            throws ProcessRequestException;

    /**
     * Response by sending <b>DELIVER_SM_RESP</b> to SMSC.
     * 
     * @param sequenceNumber is the sequence number of original <b>DELIVER_SM</b> request.
     * @throws IOException if an IO error occur.
     */
    void sendDeliverSmResp(int sequenceNumber) throws IOException;
    
}
