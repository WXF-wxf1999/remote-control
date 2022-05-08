package cn.tomo.controller.netty;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.handler.AbstractHandler;
import cn.tomo.controller.handler.HandlerContainer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cn.tomo.controller.proto.DataPacketProto;
import io.netty.util.ReferenceCountUtil;

public class ChannelHandler extends SimpleChannelInboundHandler<DataPacketProto.Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacketProto.Packet packet) throws Exception {

        // dispatch message
        Command command = Command.values()[packet.getMessageType()];
        AbstractHandler handler = HandlerContainer.getHandler(command);
        // dynamic
        handler.handleIo(packet, channelHandlerContext);

    }
}