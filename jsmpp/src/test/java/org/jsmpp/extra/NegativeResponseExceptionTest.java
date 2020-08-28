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
package org.jsmpp.extra;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.testng.annotations.Test;

public class NegativeResponseExceptionTest {

    @Test
    public void testKnownCommandStatusValue() {
        assertThat(new NegativeResponseException(11).getMessage(), is("Negative response 0000000b (Invalid Destination Address) found"));
    }

    @Test
    public void testUnknownCommandStatusValue() {
        assertThat(new NegativeResponseException(400).getMessage(), is("Negative response 00000190 found"));
    }
}
