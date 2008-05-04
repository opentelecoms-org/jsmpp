package org.jsmpp.session;

import org.jsmpp.bean.OptionalParameter;

/**
 * @author uudashr
 *
 */
public class BindResult {
    private String messageId;
    private OptionalParameter[] optionalParameter;
    
    public BindResult(String messageId, OptionalParameter[] optionalParameter) {
        this.messageId = messageId;
        this.optionalParameter = optionalParameter;
    }

    public String getMessageId() {
        return messageId;
    }

    public OptionalParameter[] getOptionalParameter() {
        return optionalParameter;
    }
}
