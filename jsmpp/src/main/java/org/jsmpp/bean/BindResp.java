/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.bean;

import java.util.Arrays;
import java.util.Objects;

import org.jsmpp.bean.OptionalParameter.Tag;

/**
 * This class represent SMPP bind response command.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class BindResp extends Command {
    private static final long serialVersionUID = 6719708830304733989L;
    
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
    
    public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass)
    {
    	return OptionalParameters.get(tagClass, optionalParameters);
    }
    
    public OptionalParameter getOptionalParameter(Tag tagEnum)
    {
    	return OptionalParameters.get(tagEnum.code(), optionalParameters);
    }
    
    public OptionalParameter[] getOptionalParameters() {
        return optionalParameters;
    }
    
    public void setOptionalParameters(OptionalParameter[] optionalParameters) {
        this.optionalParameters = optionalParameters;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final BindResp bindResp = (BindResp) o;
        return Objects.equals(systemId, bindResp.systemId) &&
            Arrays.equals(optionalParameters, bindResp.optionalParameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), systemId);
        result = 31 * result + Arrays.hashCode(optionalParameters);
        return result;
    }

}
