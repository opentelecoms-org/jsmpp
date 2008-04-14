package org.jsmpp.bean;

/**
 * This class represent SMPP bind response command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class BindResp extends Command {
    private String systemId;

    /**
     * Default constructor.
     */
    public BindResp() {
        super();
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

}
