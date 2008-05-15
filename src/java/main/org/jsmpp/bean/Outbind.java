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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((password == null) ? 0 : password.hashCode());
        result = prime * result
                + ((systemId == null) ? 0 : systemId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Outbind other = (Outbind)obj;
        if (password == null) {
            if (other.password != null)
                return false;
        } else if (!password.equals(other.password))
            return false;
        if (systemId == null) {
            if (other.systemId != null)
                return false;
        } else if (!systemId.equals(other.systemId))
            return false;
        return true;
    }
    
    
    
}
