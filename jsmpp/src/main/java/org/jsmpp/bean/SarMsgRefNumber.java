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

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 *
 */
public class SarMsgRefNumber {
    public static final int MAX_VALUE = 65535;
    private short value;

    public SarMsgRefNumber(short value) {
        this.value = value;
    }

    public SarMsgRefNumber(int value) {
        this((short)value);
    }

    public short getTag() {
        return SMPPConstant.TAG_SAR_MSG_REF_NUM;
    }

    public short getLength() {
        return 2;
    }

    public short getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SarMsgRefNumber)) {
            return false;
        }
        final SarMsgRefNumber that = (SarMsgRefNumber) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
