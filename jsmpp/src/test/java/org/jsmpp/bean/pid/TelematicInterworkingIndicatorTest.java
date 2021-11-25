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
 */
package org.jsmpp.bean.pid;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class TelematicInterworkingIndicatorTest {

  @Test
  public void test_interworking() {
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x00));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x01));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x02));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x1f));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x20));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x21));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x22));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x3f));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x40));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x41));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x5f));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x60));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x61));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x7f));
    assertEquals(TelematicInterworkingIndicator.NO_INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0x80));
    assertEquals(TelematicInterworkingIndicator.INTERWORKING, TelematicInterworkingIndicator.valueOf((byte) 0xff));
  }
}