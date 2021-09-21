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
package org.jsmpp.examples.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * UCS-2 encoding / decoding tests
 * <p>
 * See https://www.oracle.com/technical-resources/articles/javase/supplementary.html
 * <p>
 * See https://en.wikipedia.org/wiki/UTF-16
 */
public class Ucs2Test {

  @Test
  public void test_is_ucs2_encodeable() throws Exception {
    assertTrue(Ucs2.isUcs2Encodable(""));
    assertTrue(Ucs2.isUcs2Encodable(" ")); // U+0020
    assertTrue(Ucs2.isUcs2Encodable("@")); // U+0040
    assertTrue(Ucs2.isUcs2Encodable("¤$£€₺₡¢"));
    assertTrue(Ucs2.isUcs2Encodable("ر.س"));
    assertTrue(Ucs2.isUcs2Encodable("ÿ")); // U+00ff
    assertTrue(Ucs2.isUcs2Encodable("Ā")); // U+0100
    assertTrue(Ucs2.isUcs2Encodable("Э")); // U+042D
    assertTrue(Ucs2.isUcs2Encodable("☕")); // U+2615
    assertTrue(Ucs2.isUcs2Encodable("Java ☕"));
  }

  @Test
  public void test_is_not_ucs2_encodeable() throws Exception {
    assertFalse(Ucs2.isUcs2Encodable("\uD801\uDC37")); // 𐐷
    assertFalse(Ucs2.isUcs2Encodable("\uD852\uDF62")); // 𤭢
    assertFalse(Ucs2.isUcs2Encodable("What is \uD852\uDF62")); // What is 𤭢
  }
}