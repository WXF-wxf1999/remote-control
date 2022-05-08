package cn.tomo.puppet.handler;

import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.robot.KeyboardRobot;
import io.netty.channel.ChannelHandlerContext;

public class KeyboardHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        byte[] key = packet.getDataSegment1().toByteArray();
        if(key != null) {
            KeyboardRobot.getInstance().keyPress(key);
        }
    }
}
