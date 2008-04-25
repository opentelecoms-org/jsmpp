package org.jsmpp.session;

import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public class DataSmResult {
    private final String messageId;
    private final OptionalParameter[] optionalParameters;
    
    
    DataSmResult(String messageId, OptionalParameter[] optionalParameters) {
        this.messageId = messageId;
        this.optionalParameters = optionalParameters;
    }
    
    public DataSmResult(MessageId messageId, OptionalParameter[] optionalParameters) {
        this(messageId.getValue(), optionalParameters);
    }

    public String getMessageId() {
        return messageId;
    }

    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }
}
