package org.jsmpp.bean;

import java.util.Arrays;

/**
 * @author uudashr
 *
 */
public class DataSmResp extends AbstractSmRespCommand {
    private OptionalParameter[] optionalParameters;
    
    public DataSmResp() {
        super();
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
        final DataSmResp other = (DataSmResp)obj;
        if (!Arrays.equals(optionalParameters, other.optionalParameters))
            return false;
        return true;
    }
    
    
}
