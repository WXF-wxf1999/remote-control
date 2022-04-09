package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.proto.PacketBuilder;
import cn.tomo.puppet.robot.DeviceRobot;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        byte[] screenData = DeviceRobot.getScreen();
        DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, screenData, null);
        ctx.channel().writeAndFlush(packet1);

    }




}
