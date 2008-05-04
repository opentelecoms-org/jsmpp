package org.jsmpp.bean;



/**
 * @author uudashr
 *
 */
public class UnsuccessDelivery {
    private Address destinationAddress;
    private int errorStatusCode;
    
    public UnsuccessDelivery(byte destAddrTon, byte destAddrNpi, String destAddress, int errorStatusCode) {
        this(new Address(destAddrTon, destAddrNpi, destAddress), errorStatusCode);
    }
    
    public UnsuccessDelivery(Address destinationAddress, int errorStatusCode) {
        this.destinationAddress = destinationAddress;
        this.errorStatusCode = errorStatusCode;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public int getErrorStatusCode() {
        return errorStatusCode;
    }
}
