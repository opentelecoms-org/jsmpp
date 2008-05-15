package org.jsmpp.bean;

import java.util.Arrays;

/**
 * This class represent SMPP bind response command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class BindResp extends Command {
    private String systemId;
    private OptionalParameter[] optionalParameters;
    
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
    
    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }
    
    public void setOptionalParameters(OptionalParameter[] optionalParameters) {
        this.optionalParameters = optionalParameters;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(optionalParameters);
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
        final BindResp other = (BindResp)obj;
        if (!Arrays.equals(optionalParameters, other.optionalParameters))
            return false;
        if (systemId == null) {
            if (other.systemId != null)
                return false;
        } else if (!systemId.equals(other.systemId))
            return false;
        return true;
    }
    
    
}
