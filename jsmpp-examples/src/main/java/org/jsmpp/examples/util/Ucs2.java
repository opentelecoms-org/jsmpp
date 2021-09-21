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

/**
 * Support functions for UCS-2 encodings.
 * <p>
 * UCS-2 is subset of the UTF-16BE charset, only the Basic Multilingual Plane codepoints are encodable as UCS-2.
 */
public class Ucs2 {

  /**
   * Verify is the java string consists of UCS-2 characters, i.e. in the Basic Multilingual Plane of Unicode
   */
  public static boolean isUcs2Encodable(String s) {
    for (int i = 0; i < s.length(); i++) {
      if (!Character.isBmpCodePoint(s.codePointAt(i))) {
        return false;
      }
    }
    return true;
  }

}
