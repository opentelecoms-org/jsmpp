package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;

public class ReplaceSmCommandTask extends AbstractSendCommandTask {
    private String messageId;
    private TypeOfNumber sourceAddrTon;
    private NumberingPlanIndicator sourceAddrNpi;
    private String sourceAddr;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private RegisteredDelivery registeredDelivery;
    private byte smDefaultMsgId;
    private byte[] shortMessage;
    
    public ReplaceSmCommandTask(PDUSender pduSender, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage) {
        super(pduSender);
        this.messageId = messageId;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.scheduleDeliveryTime = scheduleDeliveryTime;
        this.validityPeriod = validityPeriod;
        this.registeredDelivery = registeredDelivery;
        this.smDefaultMsgId = smDefaultMsgId;
        this.shortMessage = shortMessage;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendReplaceSm(out, sequenceNumber, messageId, sourceAddrTon,
                sourceAddrNpi, sourceAddr, scheduleDeliveryTime,
                validityPeriod, registeredDelivery, smDefaultMsgId,
                shortMessage);
    }
    
    public String getCommandName() {
        return "replace_sm";
    }
}
