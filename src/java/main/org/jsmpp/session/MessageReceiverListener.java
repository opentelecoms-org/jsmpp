package org.jsmpp.session;

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * This listener will listen to every incoming short message, recognized by
 * deliver_sm command. The logic on this listener should be accomplish in a
 * short time, because the deliver_sm_resp will be processed after the logic
 * executed. Normal logic will be return the deliver_sm_resp with zero valued
 * command_status, or throw {@link ProcessRequestException} that gave non-zero
 * valued command_status (in means negative response) depends on the given error
 * code specified on the {@link ProcessRequestException}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface MessageReceiverListener {

    /**
     * This event raised when a short message received.
     * 
     * @param deliverSm is the short message.
     * @throws ProcessRequestException throw if there should be return Non-OK
     *         command_status for the response.
     */
    void onAcceptDeliverSm(DeliverSm deliverSm)
            throws ProcessRequestException;
    
    /**
     * This event raised when alert notification received.
     * 
     * @param alertNotification is the alert notification.
     */
    void onAcceptAlertNotification(AlertNotification alertNotification);
}
