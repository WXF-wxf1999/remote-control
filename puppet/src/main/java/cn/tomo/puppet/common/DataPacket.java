package cn.tomo.puppet.common;

public class DataPacket {

    private int clientId;                // if high bit is true,it is controller,or,it's puppet

    private int messageType;             // type

    private byte[] dataSegment1;         // first data segment

    private byte[] getDataSegment2;      // second data segment for expansion in future

    public int getClientId() {
        return clientId;
    }

    public int getMessageType() {
        return messageType;
    }

    public byte[] getDataSegment1() {
        return dataSegment1;
    }

    public byte[] getGetDataSegment2() {
        return getDataSegment2;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public void setDataSegment1(byte[] dataSegment1) {
        this.dataSegment1 = dataSegment1;
    }

    public void setGetDataSegment2(byte[] getDataSegment2) {
        this.getDataSegment2 = getDataSegment2;
    }
}

