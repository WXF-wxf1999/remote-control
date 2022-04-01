package cn.tomo.puppet.proto;

import com.google.protobuf.ByteString;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.common.Configure;

public class PacketBuilder {

    public static DataPacketProto.Packet buildPacket(Command command, byte[] segment1, byte[] segment2) {

        DataPacketProto.Packet.Builder builder = DataPacketProto.Packet.newBuilder();

        // get session id from configure
        builder.setSessionId(Configure.getSessionId());
        builder.setMessageType(command.ordinal());

        if(segment1 != null) {
            builder.setDataSegment1(ByteString.copyFrom(segment1));
        }
        if(segment2 != null ) {
            builder.setDataSegment2(ByteString.copyFrom(segment2));
        }

        return builder.build();
    }
}

