package cn.tomo.puppet.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

// now try with String as data after that take place of the protobuf object which can be used in c++, too
public class ChannelHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        // test: send to server
        Channel channel = channelHandlerContext.channel();
        System.out.println(s);
        channel.writeAndFlush("I am server");
    }
}
