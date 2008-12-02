package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class DataCodingTest {
    public static void main(String[] args) {
        byte dc = (byte)(0xff & 245);
        DataCoding dataCoding = DataCodings.newInstance(dc);
        System.out.println(0xff & dataCoding.toByte());
        System.out.println(dataCoding);
    }
}
