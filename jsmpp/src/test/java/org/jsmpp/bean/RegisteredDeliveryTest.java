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

import static org.testng.Assert.*;
import org.testng.annotations.Test;


/**
 * @author uudashr
 *
 */
public class RegisteredDeliveryTest {
	
    @Test(groups="checkintest")
	public void testSMSCDeliveryReceipt() {
		RegisteredDelivery regDel = new RegisteredDelivery();
		assertTrue(SMSCDeliveryReceipt.DEFAULT.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
		assertTrue(SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.FAILURE);
		assertTrue(SMSCDeliveryReceipt.FAILURE.containedIn(regDel));
	}
}
