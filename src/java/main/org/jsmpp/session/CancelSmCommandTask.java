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
public class CancelSmCommandTask extends AbstractSendCommandTask {
    private String serviceType;
    private String messageId;
    private TypeOfNumber sourceAddrTon;
    private NumberingPlanIndicator sourceAddrNpi;
    private String sourceAddr;
    private TypeOfNumber destAddrTon;
    private NumberingPlanIndicator destAddrNpi;
    private String destinationAddress;
    
    public CancelSmCommandTask(PDUSender pduSender,
            String serviceType, String messageId, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddress) {
        super(pduSender);
        this.serviceType = serviceType;
        this.messageId = messageId;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
        this.destinationAddress = destinationAddress;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendCancelSm(out, sequenceNumber, serviceType, messageId, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddress);
    }
    
    public String getCommandName() {
        return "cancel_sm";
    }
}
