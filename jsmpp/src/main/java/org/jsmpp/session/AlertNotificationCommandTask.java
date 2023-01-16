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

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.TypeOfNumber;

/**
 * @author pmoerenhout
 *
 */
public class AlertNotificationCommandTask extends AbstractSendCommandTask {
    public static final String COMMAND_NAME_ALERT_NOTIFICATION= "alert_notification";
    private final TypeOfNumber sourceAddrTon;
    private final NumberingPlanIndicator sourceAddrNpi;
    private final String sourceAddr;
    private final TypeOfNumber esmeAddrTon;
    private final NumberingPlanIndicator esmeAddrNpi;
    private final String esmeAddr;
    private final OptionalParameter[] optionalParameters;

    public AlertNotificationCommandTask(PDUSender pduSender,
                                        TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
                                        TypeOfNumber esmeAddrTon, NumberingPlanIndicator esmeAddrNpi, String esmeAddr,
                                        OptionalParameter... optionalParameters) {
        super(pduSender);
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.esmeAddrTon = esmeAddrTon;
        this.esmeAddrNpi = esmeAddrNpi;
        this.esmeAddr = esmeAddr;
        this.optionalParameters = optionalParameters;
    }

    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendAlertNotification(out, sequenceNumber,
            sourceAddrTon, sourceAddrNpi, sourceAddr,
            esmeAddrTon, esmeAddrNpi, esmeAddr,
            optionalParameters);
    }

    public String getCommandName() {
        return COMMAND_NAME_ALERT_NOTIFICATION;
    }
}
