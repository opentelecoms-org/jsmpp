package org.jsmpp.bean;

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
}
