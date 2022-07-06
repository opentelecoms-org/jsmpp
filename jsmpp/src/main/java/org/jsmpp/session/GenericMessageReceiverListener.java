/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.jsmpp.session;

import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.EnquireLink;
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
     * @param source is the session receive the data_sm command.
     * @return the data_sm result.
     * @throws ProcessRequestException throw if there should be return non-ok
     *         command_status for the response.
     */
    DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException;

    default void onAcceptEnquireLink(EnquireLink enquireLink, Session source) {
    }
}
