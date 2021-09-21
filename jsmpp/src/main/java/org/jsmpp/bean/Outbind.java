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

import java.util.Objects;

/**
 * @author uudashr
 * 
 */
public class Outbind extends Command {
    private static final long serialVersionUID = -3730649847637872720L;
    
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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Outbind)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final Outbind outbind = (Outbind) o;
        return Objects.equals(systemId, outbind.systemId) && Objects.equals(password, outbind.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), systemId, password);
    }
}
