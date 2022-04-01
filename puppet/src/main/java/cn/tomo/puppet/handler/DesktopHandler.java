package cn.tomo.puppet.handler;

import cn.tomo.puppet.proto.DataPacketProto;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext channel) {

        System.out.println("desktop");
    }

}
