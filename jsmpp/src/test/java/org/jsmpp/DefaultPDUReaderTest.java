package org.jsmpp;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import org.jsmpp.bean.Command;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DefaultPDUReaderTest {

  DefaultPDUReader defaultPDUReader;

  @BeforeMethod
  public void setUp() throws Exception {
    defaultPDUReader = new DefaultPDUReader();
  }

  @Test
  public void testPDUHeader() throws Exception {

    DataInputStream in = new DataInputStream(
        new ByteArrayInputStream(
            new byte[]{ 0x00, 0x00, 0x00, 0x10, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c }));

    Command command = defaultPDUReader.readPDUHeader(in);

    assertEquals(command.getCommandLength(), 16, "Command length differs");
    assertEquals(command.getCommandId(), 0x01020304, "Command id differs");
    assertEquals(command.getCommandStatus(), 0x05060708, "Command status differs");
    assertEquals(command.getSequenceNumber(), 0x090a0b0c, "Sequence number differs");
  }

  @Test(groups = "checkintest", expectedExceptions = InvalidCommandLengthException.class)
  public void testInvalidPDUHeaderWithCommandLengthZero() throws Exception {

    DataInputStream in = new DataInputStream(
        new ByteArrayInputStream(
            new byte[]{ 0x00, 0x00, 0x00, 0x00 }));
    defaultPDUReader.readPDUHeader(in);
  }

  @Test(groups = "checkintest", expectedExceptions = InvalidCommandLengthException.class)
  public void testInvalidPDUHeaderWithCommandLengthFour() throws Exception {

    DataInputStream in = new DataInputStream(
        new ByteArrayInputStream(
            new byte[]{ 0x00, 0x00, 0x00, 0x04 }));

    defaultPDUReader.readPDUHeader(in);
  }

  @Test(groups = "checkintest", expectedExceptions = InvalidCommandLengthException.class)
  public void testInvalidPDUHeaderWithCommandLengthEigth() throws Exception {

    DefaultPDUReader defaultPDUReader = new DefaultPDUReader();

    DataInputStream in = new DataInputStream(
        new ByteArrayInputStream(
            new byte[]{ 0x00, 0x00, 0x00, 0x08, 0x01, 0x02, 0x03, 0x04 }));
    defaultPDUReader.readPDUHeader(in);
  }

  @Test(groups = "checkintest", expectedExceptions = InvalidCommandLengthException.class)
  public void testInvalidPDUHeaderWithCommandLengthFifteen() throws Exception {

    DefaultPDUReader defaultPDUReader = new DefaultPDUReader();

    DataInputStream in = new DataInputStream(
        new ByteArrayInputStream(
            new byte[]{ 0x00, 0x00, 0x00, 0x0f, 0x01, 0x02, 0x03, 0x04, 0x00, 0x00, 0x00, 0x00, 0x40, 0x41, 0x42, 0x43 }));
    defaultPDUReader.readPDUHeader(in);
  }
}