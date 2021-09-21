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

/**
 * Destination address can be use for submit multiple.
 *
 * @author uudashr
 */
public interface DestinationAddress {
  enum Flag {
    SME_ADDRESS((byte) 1),
    DISTRIBUTION_LIST((byte) 2);

    private final byte value;

    Flag(byte value) {
      this.value = value;
    }

    public byte getValue() {
      return value;
    }
  }

  Flag getFlag();
}
