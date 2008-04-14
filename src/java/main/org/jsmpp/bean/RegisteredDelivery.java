package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class RegisteredDelivery {
    private byte value;

    public RegisteredDelivery() {
        value = 0;
    }

    public RegisteredDelivery(int value) {
        this.value = (byte)value;
    }

    public RegisteredDelivery(byte value) {
        this.value = value;
    }
    
    public RegisteredDelivery(SMSCDeliveryReceipt smscDeliveryReceipt) {
        this();
        setSMSCDeliveryReceipt(smscDeliveryReceipt);
    }
    
    public RegisteredDelivery(SMEOriginatedAcknowledgement smeOriginatedAcknowledgement) {
        this();
        setSMEOriginatedAcknowledgement(smeOriginatedAcknowledgement);
    }
    
    public byte value() {
        return value;
    }

    public RegisteredDelivery setSMSCDeliveryReceipt(
            SMSCDeliveryReceipt smscDeliveryReceipt) {
        value = SMSCDeliveryReceipt.compose(value, smscDeliveryReceipt);
        return this;
    }
    
    public RegisteredDelivery setSMEOriginatedAcknowledgement(SMEOriginatedAcknowledgement smeOriginatedAcknowledgement) {
        value = SMEOriginatedAcknowledgement.compose(value, smeOriginatedAcknowledgement);
        return this;
    }
    
    public RegisteredDelivery setIntermediateNotification(IntermediateNotification intermediateNotification) {
        value = IntermediateNotification.compose(value, intermediateNotification);
        return this;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RegisteredDelivery))
            return false;
        RegisteredDelivery other = (RegisteredDelivery)obj;
        return value == other.value;
    }
}
