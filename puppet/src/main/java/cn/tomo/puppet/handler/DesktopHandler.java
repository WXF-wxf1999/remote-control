package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.common.Zip;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.proto.PacketBuilder;
import cn.tomo.puppet.robot.ScreenRobot;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        //byte[] screenData = Zip.compress(DeviceRobot.getScreen());
        byte[] screenData = ScreenRobot.getScreen();
        DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, screenData, null);
        ctx.channel().writeAndFlush(packet1);

    }




}
