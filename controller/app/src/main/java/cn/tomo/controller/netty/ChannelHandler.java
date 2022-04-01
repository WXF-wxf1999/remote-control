package cn.tomo.controller.netty;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.handler.AbstractHandler;
import cn.tomo.controller.handler.HandlerContainer;
import cn.tomo.controller.proto.PacketBuilder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cn.tomo.controller.proto.DataPacketProto;

public class ChannelHandler extends SimpleChannelInboundHandler<DataPacketProto.Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacketProto.Packet packet) throws Exception {

        int i = packet.getMessageType();
        // dispatch message
        Command command = Command.values()[packet.getMessageType()];
        AbstractHandler handler = HandlerContainer.getHandler(command);
        // dynamic
        handler.handleIo(packet, channelHandlerContext);

    }
}