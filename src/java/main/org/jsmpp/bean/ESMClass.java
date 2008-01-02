package org.jsmpp.bean;

/**
 * FIXME uud: create ESMClass support for Server module. 
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
    public boolean equals(Object obj) {
        if (!(obj instanceof ESMClass))
            return false;
        ESMClass other = (ESMClass) obj;
        return value == other.value;
    }
}
