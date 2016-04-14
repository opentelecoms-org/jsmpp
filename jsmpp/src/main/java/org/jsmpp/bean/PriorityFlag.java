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
 * @author pmoerenhout
 */
public class PriorityFlag {

  public enum GsmSms {
    NORMAL((byte) 0x00),
    PRIORITY((byte) 0x01);

    private final byte value;

    GsmSms(byte value) {
      this.value = value;
    }

    public byte value() {
      return value;
    }
  }

  public enum GsmCbs {
    NORMAL((byte) 0x00),
    IMMEDIATE_BROADCAST((byte) 0x01),
    HIGH_PRIORITY((byte) 0x02),
    RESERVED((byte) 0x03),
    PRIORITY_BACKGROUND((byte) 0x04);

    private final byte value;

    GsmCbs(byte value) {
      this.value = value;
    }

    public byte value() {
      return value;
    }
  }

  public enum Is95 {
    NORMAL((byte) 0x00),
    INTERACTIVE((byte) 0x01),
    URGENT((byte) 0x02),
    EMERGENCY((byte) 0x03);

    private final byte value;

    Is95(byte value) {
      this.value = value;
    }

    public byte value() {
      return value;
    }
  }


  public enum Ansi41 {
    NORMAL((byte) 0x00),
    INTERACTIVE((byte) 0x01),
    URGENT((byte) 0x02),
    EMERGENCY((byte) 0x03);

    private final byte value;

    Ansi41(byte value) {
      this.value = value;
    }

    public byte value() {
      return value;
    }

  }

  public enum Ansi136 {
    BULK((byte) 0x00),
    NORMAL((byte) 0x01),
    URGENT((byte) 0x02),
    VERY_URGENT((byte) 0x03);

    private final byte value;

    Ansi136(byte value) {
      this.value = value;
    }

    public byte value() {
      return value;
    }
  }

}
