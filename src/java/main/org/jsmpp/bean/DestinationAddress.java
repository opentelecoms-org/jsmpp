package org.jsmpp.bean;

/**
 * Destination address can be use for submit multiple.
 * 
 * @author uudashr
 *
 */
public interface DestinationAddress {
    public static enum Flag {
        SME_ADDRESS((byte)1), DISTRIBUTION_LIST((byte)2);
        
        private final byte value;
        
        private Flag(byte value) {
            this.value = value;
        }
        
        public byte getValue() {
            return value;
        }
    }
    
    Flag getFlag();
}
