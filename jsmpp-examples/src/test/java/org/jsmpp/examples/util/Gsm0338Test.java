package org.jsmpp.examples.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class Gsm0338Test {

  @Test
  public void test_basic_chars() throws Exception {
    assertTrue(Gsm0338
        .isBasicEncodeable("@£$¥èéùìòÇØøÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&\'()*+,-./0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà"));
  }

  @Test
  public void test_cr() throws Exception {
    assertTrue(Gsm0338.isBasicEncodeable("\r"));
  }

  @Test
  public void test_cr_char() throws Exception {
    assertTrue(Gsm0338.isBasicEncodeable('\r'));
  }

  @Test
  public void test_lf() throws Exception {
    assertTrue(Gsm0338.isBasicEncodeable("\n"));
  }

  @Test
  public void test_lf_char() throws Exception {
    assertTrue(Gsm0338.isBasicEncodeable('\n'));
  }

  @Test
  public void test_basic_extended_chars() throws Exception {
    assertTrue(Gsm0338.isBasicEncodeable("^{}\\[~]|€"));
  }

  @Test
  public void test_null_string() throws Exception {
    assertFalse(Gsm0338.isBasicEncodeable("\000"));
  }

  @Test
  public void test_null_char() throws Exception {
    assertFalse(Gsm0338.isBasicEncodeable('\000'));
  }

  @Test
  public void test_non_gsm_0338_basic_chars() throws Exception {
    assertFalse(Gsm0338.isBasicEncodeable("⺴⻤"));
  }

  @Test
  public void test_count_septets() {
    assertEquals(1, Gsm0338.countSeptets('a'));
    assertEquals(1, Gsm0338.countSeptets("a"));
    assertEquals(2, Gsm0338.countSeptets('€'));
    assertEquals(2, Gsm0338.countSeptets("€"));
    assertEquals(3, Gsm0338.countSeptets("abc"));
    assertEquals(7, Gsm0338.countSeptets("[abc]"));
  }

}
