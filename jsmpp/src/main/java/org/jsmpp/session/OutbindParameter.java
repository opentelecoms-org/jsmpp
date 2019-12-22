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
package org.jsmpp.session;

import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.util.ObjectUtil;

/**
 * This class is wraps all bind parameter that will be send as PDU.
 *
 * @author pmoerenhout
 *
 */
public class OutbindParameter {

    private String systemId;
    private String password;
    private InterfaceVersion interfaceVersion;

    /**
     * Construct with all mandatory parameters.
     *
     * @param systemId
     *            is the system id.
     * @param password
     *            is the password.
     */
    public OutbindParameter(String systemId,
                            String password)
    {
        this(systemId, password, InterfaceVersion.IF_34);
    }

    public OutbindParameter(String systemId,
                            String password,
                            InterfaceVersion interfaceVersion)
    {

        this.systemId = systemId;
        this.password = password;
        this.interfaceVersion = interfaceVersion;
    }


    public String getSystemId() {
        return this.systemId;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result)
                + ((this.password == null) ? 0 : this.password.hashCode());
        result = (prime * result)
                + ((this.systemId == null) ? 0 : this.systemId.hashCode());
        result = (prime * result)
                + ((this.interfaceVersion == null) ? 0 : this.interfaceVersion.hashCode());

        return result;
    }


    private boolean hasEqualPassword(OutbindParameter other) {
        return ObjectUtil.equals(password, other.password);
    }

    private boolean hasEqualSystemId(OutbindParameter other) {
        return ObjectUtil.equals(systemId , other.systemId);
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final OutbindParameter other = (OutbindParameter)obj;

        if (!hasEqualPassword(other)) {
            return false;
        }
        if (!hasEqualSystemId(other)) {
            return false;
        }
        return true;
    }

    public InterfaceVersion getInterfaceVersion() {
        return this.interfaceVersion;
    }

}
