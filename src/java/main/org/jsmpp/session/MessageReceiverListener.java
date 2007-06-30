package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessMessageException;

/**
 * This listener will listen to every incomming short message, recognized by
 * deliver_sm command. The logic on this listener should be accomplish in a
 * short time, because the deliver_sm_resp will be processed after the logic
 * executed. Normal logic will be return the deliver_sm_resp with zero valued
 * command_status, or throw {@link ProcessMessageException} that gave non-zero
 * valued command_status (in means negative response) depends on the given error
 * code specified on the {@link ProcessMessageException}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface MessageReceiverListener {

    /**
     * Event that called when an short message accepted.
     * 
     * @param deliverSm is the deliver_sm command.
     * @throws ProcessMessageException throw if there should be return Non-OK
     *         command_status for the response.
     */
    public void onAcceptDeliverSm(DeliverSm deliverSm)
            throws ProcessMessageException;
}
