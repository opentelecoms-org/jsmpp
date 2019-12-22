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

import static org.junit.Assert.assertEquals;

import org.testng.annotations.Test;

public class DeliverSmTest {

  @Test
  public void testDeliverSm() {
    DeliverSm deliverSm = new DeliverSm();
    deliverSm.sourceAddr = "31612345678";
    deliverSm.sourceAddrTon = TypeOfNumber.INTERNATIONAL.value();
    deliverSm.sourceAddrNpi = NumberingPlanIndicator.ISDN.value();
    deliverSm.destAddress = "33600000000";
    deliverSm.destAddrTon = TypeOfNumber.INTERNATIONAL.value();
    deliverSm.destAddrNpi = NumberingPlanIndicator.ISDN.value();
    deliverSm.setOptionalParameters(
        new OptionalParameter.Message_state((byte) 0x02),
        new OptionalParameter.Receipted_message_id("123456"));

    assertEquals(OptionalParameter.Message_state.Message_state_enum.DELIVERED,
        ((OptionalParameter.Message_state)deliverSm.getOptionalParameter(OptionalParameter.Tag.MESSAGE_STATE)).getMessageState());
    assertEquals("123456",
        ((OptionalParameter.Receipted_message_id) deliverSm.getOptionalParameter(OptionalParameter.Tag.RECEIPTED_MESSAGE_ID)).getValueAsString());
  }

}