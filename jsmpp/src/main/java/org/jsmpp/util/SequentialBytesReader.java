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
package org.jsmpp.util;

/**
 * Utility to read value from bytes sequentially.
 *
 * @author uudashr
 */
class SequentialBytesReader {
  int cursor;
  private byte[] bytes;

  public SequentialBytesReader(byte[] bytes) {
    this.bytes = bytes;
    cursor = 0;
  }

  public byte readByte() {
    return bytes[cursor++];
  }

  public int readInt() {
    int val = OctetUtil.bytesToInt(bytes, cursor);
    cursor += 4;
    return val;
  }

  public byte[] readBytesUntilNull() {
    // TODO uudashr: we can do some improvement here
    int i = cursor;
    while (bytes[i++] != (byte) 0) {
    }
    int length = i - 1 - cursor;
    if (length == 0) {
      cursor += 1 + length;
      return new byte[]{};
    }
    byte[] data = new byte[length];
    System.arraycopy(bytes, cursor, data, 0, length);
    cursor += 1 + length;
    return data;
  }

  /**
   * Read C-Octet string from bytes.
   *
   * @return {@code String} value. Nullable.
   */
  public String readCString() {
    // TODO uudashr: we can do some improvement here
    int i = cursor;
    while (bytes[i++] != (byte) 0) {
    }
    int length = i - 1 - cursor;
    if (length == 0) {
      cursor += 1 + length;
      return null;
    }
    String val = new String(bytes, cursor, length);
    cursor += 1 + length;
    return val;
  }

  public byte[] readBytes(int length) {
    if (length == 0) {
      return new byte[0];
    }
    byte[] data = new byte[length];
    System.arraycopy(bytes, cursor, data, 0, length);
    cursor += length;
    return data;
  }

  public byte[] readBytes(byte length) {
    return readBytes(length & 0xff);
  }

  /**
   * @param length The number of bytes to read.
   * @return {@code String} value. Nullable.
   */
  public String readString(int length) {
    if (length == 0) {
      return null;
    }
    String val = new String(bytes, cursor, length);
    cursor += length;
    return val;
  }

  public short readShort() {
    short value = OctetUtil.bytesToShort(bytes, cursor);
    cursor += 2;
    return value;
  }

  /**
   * @param length The number of bytes to read.
   * @return {@code String} value. Nullable.
   */
  public String readString(byte length) {
    /*
     * you have to convert the signed byte into unsigned byte (in
     * integer representation) with & operand by 0xff
     */
    return readString(length & 0xff);
  }

  public int remainBytesLength() {
    return bytes.length - cursor;
  }

  public boolean hasMoreBytes() {
    return cursor < (bytes.length - 1);
  }

  public void resetCursor() {
    cursor = 0;
  }

  public byte[] getBytes() {
    return bytes;
  }
}