package cn.tomo.controller.netty;

import android.util.Log;

import java.util.concurrent.Semaphore;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import cn.tomo.controller.netty.coder.*;
import cn.tomo.controller.proto.DataPacketProto;

public class NettyClient {

    private boolean isOk = false;

    private final Semaphore semaphore = new Semaphore(0,true);

    private EventLoopGroup group = null;
    public void start() {

        group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            // get pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            // must add a decoder or will not receive a message
                            // channelRead will be called in ByteToMessageDecoder, the our channelRead0 will work
                            pipeline.addLast(new Decoder());
                            pipeline.addLast(new Encoder());
                            // add our handler
                            pipeline.addLast(new ChannelHandler());

                        }
                    });

            connectWithServer(bootstrap);


        } catch (Exception e) {

            group.shutdownGracefully();

            Log.e(NettyClient.class.getName(), e.toString());
        }


    }

    protected void finalize() {

        group.shutdownGracefully();
    }

    private void login(Channel channel) {

        //channel.writeAndFlush("I am puppet");
        DataPacketProto.Packet.Builder builder = DataPacketProto.Packet.newBuilder();

        builder.setSessionId(Configure.getSessionId());
        builder.setMessageType(Command.CONTROLLER_LOGIN.ordinal());

        DataPacketProto.Packet packet = builder.build();

        // send
        channel.writeAndFlush(packet);

    }

    private void connectWithServer(Bootstrap bootstrap) throws Exception{

        // try three times
        int retry = 3;
        for (int i = 0; i < retry; i++) {

            if(isOk) {
                return;
            }
            ChannelFuture channelFuture = bootstrap.connect(Configure.getHost(),Configure.getPort()).sync();

            login(channelFuture.channel());
            channelFuture.addListener(new Listener());
            // wait for Listener
            semaphore.acquire();

        }

    }
    class Listener implements ChannelFutureListener {

        @Override
        public void operationComplete(ChannelFuture channelFuture) throws Exception {
            if(channelFuture.isSuccess()) {
                isOk = true;
            }else {
                Log.i(NettyClient.class.getName(), "fail to connect with server");
            }
            semaphore.release();
        }
    }
}
