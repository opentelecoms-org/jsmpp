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

import java.util.Objects;

import org.jsmpp.bean.InterfaceVersion;

/**
 * This class is wraps all outbind parameter that will be send as PDU.
 *
 * @author pmoerenhout
 */
public class OutbindParameter {

  private String systemId;
  private String password;
  private InterfaceVersion interfaceVersion;

  /**
   * Construct with all mandatory parameters.
   *
   * @param systemId is the system id.
   * @param password is the password.
   */
  public OutbindParameter(String systemId,
                          String password) {
    this(systemId, password, InterfaceVersion.IF_34);
  }

    /**
     * Construct with all mandatory parameters.
     *
     * @param systemId is the system id
     * @param password is the password.
     * @param interfaceVersion is the interface version
     */
  public OutbindParameter(String systemId,
                          String password,
                          InterfaceVersion interfaceVersion) {

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

  public InterfaceVersion getInterfaceVersion() {
    return this.interfaceVersion;
  }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OutbindParameter that = (OutbindParameter) o;
        return Objects.equals(systemId, that.systemId) &&
            Objects.equals(password, that.password) &&
            interfaceVersion == that.interfaceVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(systemId, password, interfaceVersion);
    }

}
