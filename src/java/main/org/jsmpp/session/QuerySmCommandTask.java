package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;

/**
 * @author uudashr
 *
 */
public class QuerySmCommandTask extends AbstractSendCommandTask {
    private final String messageId;
    private final TypeOfNumber sourceAddrTon;
    private final NumberingPlanIndicator sourceAddrNpi;
    private final String sourceAddr;
    
    public QuerySmCommandTask(OutputStream out, PDUSender pduSender,
            String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr) {
        super(out, pduSender);
        this.messageId = messageId;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendQuerySm(out, sequenceNumber, messageId, sourceAddrTon,
                sourceAddrNpi, sourceAddr);
    }
    
    public String getCommandName() {
        return "query_sm";
    }
}
