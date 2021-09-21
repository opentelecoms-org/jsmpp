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
 * Based on https://en.wikipedia.org/wiki/GSM_03.38
 */
public class Gsm0338 {

  private static final char[] BASIC_CHARS = {
      // Basic Character Set
      '@', '£', '$', '¥', 'è', 'é', 'ù', 'ì', 'ò', 'Ç', '\n', 'Ø', 'ø', '\r', 'Å', 'å',
      'Δ', '_', 'Φ', 'Γ', 'Λ', 'Ω', 'Π', 'Ψ', 'Σ', 'Θ', 'Ξ', 'Æ', 'æ', 'ß', 'É',
      ' ', '!', '"', '#', '¤', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '.', '/',
      '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ':', ';', '<', '=', '>', '?',
      '¡', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
      'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'Ä', 'Ö', 'Ñ', 'Ü', '§',
      '¿', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
      'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'ä', 'ö', 'ñ', 'ü', 'à'
  };

  private static final char[] BASIC_CHARS_EXTENSION = {
      // Basic Character Set Extension
      '\f', '^', '{', '}', '\\', '[', '~', ']', '|', '€'
  };

  /**
   * Verify is the java string consists of only GSM 03.38 characters of the Basic Character Set including the extension
   */
  public static boolean isBasicEncodeable(String javaString) {
    char[] javaChars = javaString.toCharArray();
    for (char c : javaChars) {
      if (isBasicEncodeable(c)) {
        continue;
      }
      return false;
    }
    return true;
  }

  /**
   * Verify is the java char is an GSM 03.38 character of the Basic Character Set including the extension
   */
  public static boolean isBasicEncodeable(char javaChar) {
    return (inBasicCharacterSet(javaChar) || inBasicCharacterSetExtension(javaChar));
  }

  public static boolean inBasicCharacterSet(char javaChar) {
    for (char extendedChar : BASIC_CHARS) {
      if (extendedChar == javaChar) {
        return true;
      }
    }
    return false;
  }

  public static boolean inBasicCharacterSetExtension(char javaChar) {
    for (char extendedChar : BASIC_CHARS_EXTENSION) {
      if (extendedChar == javaChar) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determine the number of GSM 03.38 Basic Character Set (included extension) septets of a Java char.
   */
  public static int countSeptets(char c) {
    if (inBasicCharacterSetExtension(c)) {
      return 2;
    }
    if (inBasicCharacterSet(c)) {
      return 1;
    }
    throw new IllegalArgumentException("Character '" + c + " is not a basic encodeable character");
  }

  /**
   * Determine the number of GSM 03.38 Basic Character Set (included extension) septets of a Java string.
   */
  public static int countSeptets(String s) {
    int i = 0;
    for( char c: s.toCharArray()){
      i += countSeptets(c);
    }
    return i;
  }

}
