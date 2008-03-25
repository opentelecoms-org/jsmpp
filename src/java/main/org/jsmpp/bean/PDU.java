package org.jsmpp.bean;

public class PDU {
    final Command header;
    final byte[] data;

    public PDU(Command header, byte[] data) {
        this.header = header;
        this.data = data;
    }

    public int getCommandId() {
        return header.getCommandId();
    }

    public Command getCommand() {
        return header;
    }

    public byte[] getData() {
        return data;
    }
}
