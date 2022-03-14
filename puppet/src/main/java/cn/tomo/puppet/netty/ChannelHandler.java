package cn.tomo.puppet.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cn.tomo.puppet.proto.DataPacketProto;

public class ChannelHandler extends SimpleChannelInboundHandler<DataPacketProto.Packet> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, DataPacketProto.Packet packet) throws Exception {

        // test: send to server
        Channel channel = channelHandlerContext.channel();

        // transform to DataPacket
        // packet.getClientId();

        channel.writeAndFlush("I am server");
    }
}
