package cn.tomo.puppet.netty;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.handler.AbstractHandler;
import cn.tomo.puppet.handler.HandlerContainer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cn.tomo.puppet.proto.DataPacketProto;

public class ChannelHandler extends SimpleChannelInboundHandler<DataPacketProto.Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacketProto.Packet packet) throws Exception {

        // dispatch message
        Command command = Command.values()[packet.getMessageType()];
        AbstractHandler handler = HandlerContainer.getHandler(command);
        // dynamic
        handler.handleIo(packet, channelHandlerContext.channel());
    }
}
