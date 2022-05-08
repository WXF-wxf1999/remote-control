package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.proto.PacketBuilder;
import cn.tomo.puppet.robot.FileRobot;
import io.netty.channel.ChannelHandlerContext;

public class FileHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        int type = packet.getMessageType();
        if( type == Command.DRIVER_REQUEST.ordinal()) {

            String result = FileRobot.getDirectory(new String(packet.getDataSegment1().toByteArray()));

            DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.DRIVER_REQUEST, result.getBytes(), null);
            ctx.channel().writeAndFlush(packet1);
        }
        else if(type == Command.FILE_RESEARCH.ordinal()) {

            String result = null;
            if(packet.getDataSegment2().toByteArray() != null) {
                result = FileRobot.searchFile(new String(packet.getDataSegment1().toByteArray()),
                        new String(packet.getDataSegment2().toByteArray()));
            } else {
                result = FileRobot.searchFile(new String(packet.getDataSegment1().toByteArray()));
            }
            DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.FILE_RESEARCH,
                    result.getBytes(), null);
            ctx.channel().writeAndFlush(packet1);
        }
        else if(type == Command.FILE_REQUEST.ordinal()) {

            FileRobot.sendFileData(new String(packet.getDataSegment1().toByteArray()), ctx);
        }



    }
}
