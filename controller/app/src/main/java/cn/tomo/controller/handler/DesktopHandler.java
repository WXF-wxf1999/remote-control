package cn.tomo.controller.handler;

import cn.tomo.controller.proto.DataPacketProto;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {
    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        System.out.println("desktop");
    }
}
