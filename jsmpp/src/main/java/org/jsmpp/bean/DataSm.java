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
 * The data_sm operation is similar to the submit_sm in that it provides a means to submit a mobile-terminated message.
 * However, data_sm is intended for packet-based applications such as WAP in that it features a reduced PDU body
 * containing fields relevant to WAP or packet-based applications.
 *
 * @author uudashr
 */
public class DataSm extends AbstractSmCommand {
    public DataSm() {
        super();
    }
}
