package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class InterfaceVersionTest {

  @Test
  public void testInterfaceVersionValueOf() {
    InterfaceVersion interfaceVersion = InterfaceVersion.valueOf((byte) 0x34);
    assertEquals(interfaceVersion, InterfaceVersion.IF_34);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidInterfaceVersion() {
    InterfaceVersion.valueOf((byte) 0xff);
  }

  @Test
  public void testInterfaceVersionWithNull() {
    InterfaceVersion interfaceVersion = InterfaceVersion.IF_34.min(null);
    assertEquals(interfaceVersion, InterfaceVersion.IF_34);
  }

  @Test
  public void testInterfaceVersion() {
    InterfaceVersion interfaceVersion = InterfaceVersion.IF_50.min(InterfaceVersion.IF_33);
    assertEquals(interfaceVersion, InterfaceVersion.IF_33);
  }

  @Test
  public void testCompare() {
    assertTrue(InterfaceVersion.IF_50.compareTo(InterfaceVersion.IF_34) > 0);
    assertTrue(InterfaceVersion.IF_50.compareTo(InterfaceVersion.IF_50) == 0);
    assertTrue(InterfaceVersion.IF_34.compareTo(InterfaceVersion.IF_50) < 0);
  }

}