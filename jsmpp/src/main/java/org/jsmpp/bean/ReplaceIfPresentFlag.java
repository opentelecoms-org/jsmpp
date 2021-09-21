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
public class ReplaceIfPresentFlag {
    public static final ReplaceIfPresentFlag DEFAULT = new ReplaceIfPresentFlag(0);
    public static final ReplaceIfPresentFlag DONT_REPLACE = DEFAULT;
    public static final ReplaceIfPresentFlag REPLACE = new ReplaceIfPresentFlag(1);

    private byte value;

    public ReplaceIfPresentFlag(int value) {
        this.value = (byte)value;
    }

    public ReplaceIfPresentFlag(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (!(o instanceof ReplaceIfPresentFlag)) {
            return false;
        }
        final ReplaceIfPresentFlag that = (ReplaceIfPresentFlag) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
