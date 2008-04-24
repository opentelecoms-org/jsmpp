package org.jsmpp.session;

import org.jsmpp.bean.OptionalParameter;

/**
 * @author uudashr
 *
 */
public class DataSmResult {
    private final String messageId;
    private final OptionalParameter[] optionalParameters;
    
    public DataSmResult(String messageId, OptionalParameter[] optionalParameters) {
        this.messageId = messageId;
        this.optionalParameters = optionalParameters;
    }

    public String getMessageId() {
        return messageId;
    }

    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }
}
