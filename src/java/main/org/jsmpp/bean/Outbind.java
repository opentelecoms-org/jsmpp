package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class Outbind extends Command {
    private String systemId;
    private String password;

    public Outbind() {
        super();
    }

    /**
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return Returns the systemId.
     */
    public String getSystemId() {
        return systemId;
    }

    /**
     * @param systemId The systemId to set.
     */
    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

}
