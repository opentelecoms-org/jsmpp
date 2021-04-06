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

/**
 * @author uudashr
 * 
 */
public class SubmitSmResp extends AbstractSmRespCommand {

    private static final long serialVersionUID = 7838196972774489155L;
    // SMPP 5.0
    private OptionalParameter[] optionalParameters;

    public SubmitSmResp() {
        super();
    }

    public <U extends OptionalParameter> U getOptionalParameter(Class<U> tagClass)
    {
        return OptionalParameters.get(tagClass, optionalParameters);
    }

    public OptionalParameter getOptionalParameter(OptionalParameter.Tag tagEnum)
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
        if (!(o instanceof SubmitSmResp)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final SubmitSmResp that = (SubmitSmResp) o;
        return Arrays.equals(optionalParameters, that.optionalParameters);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(optionalParameters);
        return result;
    }
}
