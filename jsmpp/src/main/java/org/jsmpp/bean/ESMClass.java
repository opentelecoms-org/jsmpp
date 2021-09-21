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
public class ESMClass {
    private byte value;

    public ESMClass() {
        value = 0;
    }

    public ESMClass(int value) {
        this.value = (byte) value;
    }

    public ESMClass(byte value) {
        this.value = value;
    }
    
    public ESMClass(MessageMode messageMode, MessageType messageType, GSMSpecificFeature specificFeature) {
        this(0);
        setMessageMode(messageMode);
        setMessageType(messageType);
        setSpecificFeature(specificFeature);
    }
    
    public byte value() {
        return value;
    }
    
    public ESMClass setMessageMode(MessageMode messageMode) {
        value = MessageMode.compose(value, messageMode);
        return this;
    }

    public ESMClass setSpecificFeature(GSMSpecificFeature specificFeature) {
        value = GSMSpecificFeature.compose(value, specificFeature);
        return this;
    }

    public ESMClass setMessageType(MessageType messageType) {
        value = MessageType.compose(value, messageType);
        return this;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ESMClass)) {
            return false;
        }
        final ESMClass esmClass = (ESMClass) o;
        return value == esmClass.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}
