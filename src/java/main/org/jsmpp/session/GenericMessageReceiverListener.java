package org.jsmpp.session;

import org.jsmpp.bean.DataSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * This listener will listen to every incoming short message. The logic of this
 * method event should be accomplish in a short time, because the the other
 * event will be waiting the invocation of the method. Normal logic will be
 * return the response with zero valued command_status, or throw
 * {@link ProcessRequestException} if the non-zero valued command_status (in
 * means negative response) returned.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface GenericMessageReceiverListener {

    /**
     * Event that called when a data short message accepted.
     * 
     * @param dataSm is the data_sm command.
     * @return the data_sm result.
     * @throws ProcessRequestException throw if there should be return non-ok
     *         command_status for the response.
     */
    DataSmResult onAcceptDataSm(DataSm dataSm) throws ProcessRequestException;
}
