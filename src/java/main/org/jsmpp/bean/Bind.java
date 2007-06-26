package org.jsmpp.bean;

/**
 * This class represent SMPP bind command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class Bind extends Command {
    private String systemId;
    private String password;
    private String systemType;
    private byte interfaceVersion;
    private byte addrTon;
    private byte addrNpi;
    private String addressRange;

    public Bind() {
        super();
    }

    /**
     * Get the address_range.
     * 
     * @return the address_range.
     */
    public String getAddressRange() {
        return addressRange;
    }

    /**
     * Set the address_range.
     * 
     * @param addressRange is the address_range.
     */
    public void setAddressRange(String addressRange) {
        this.addressRange = addressRange;
    }

    /**
     * Get the addr_npi.
     * 
     * @return the addr_npi.
     */
    public byte getAddrNpi() {
        return addrNpi;
    }

    /**
     * Set the addr_npi.
     * 
     * @param addrNpi is the addr_npi.
     */
    public void setAddrNpi(byte addrNpi) {
        this.addrNpi = addrNpi;
    }

    /**
     * Get the addr_ton.
     * 
     * @return the addr_ton.
     */
    public byte getAddrTon() {
        return addrTon;
    }

    /**
     * Set the addr_ton.
     * 
     * @param addrTon is the addr_ton.
     */
    public void setAddrTon(byte addrTon) {
        this.addrTon = addrTon;
    }

    /**
     * Get the interface_version.
     * 
     * @return the interface_version.
     */
    public byte getInterfaceVersion() {
        return interfaceVersion;
    }

    /**
     * Set the interface_version.
     * 
     * @param interfaceVersion is the interface_version.
     */
    public void setInterfaceVersion(byte interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    /**
     * Get the password.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set the password.
     * 
     * @param password is the password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the system_id.
     * 
     * @return the system_id.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * Set the system_id.
     * 
     * @param systemId is the system_id.
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    /**
     * Get the system_type.
     * 
     * @return the system_type.
     */
    public String getSystemType() {
        return systemType;
    }

    /**
     * Set the system_type.
     * 
     * @param systemType is the system_type.
     */
    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

}
