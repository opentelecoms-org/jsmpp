package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class ESMClass {
    private byte value;

    public ESMClass() {
        value = 0;
    }

    public ESMClass(int value) {
        this.value = (byte) value;
    }

    public ESMClass(byte value) {
        this.value = value;
    }
    
    public ESMClass(MessageMode messageMode, MessageType messageType, GSMSpecificFeature specificFeature) {
        this(0);
        setMessageMode(messageMode);
        setMessageType(messageType);
        setSpecificFeature(specificFeature);
    }
    
    public byte value() {
        return value;
    }
    
    public ESMClass setMessageMode(MessageMode messageMode) {
        value = MessageMode.compose(value, messageMode);
        return this;
    }

    public ESMClass setSpecificFeature(GSMSpecificFeature specificFeature) {
        value = GSMSpecificFeature.compose(value, specificFeature);
        return this;
    }

    public ESMClass setMessageType(MessageType messageType) {
        value = MessageType.compose(value, messageType);
        return this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ESMClass other = (ESMClass)obj;
        if (value != other.value)
            return false;
        return true;
    }

    
}
