package cn.tomo.controller.netty;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.Semaphore;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.proto.PacketBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
    private static ChannelHandlerContext channelHandlerContext;
    private final Semaphore semaphore = new Semaphore(0,true);
    private static Context context = null;
    private EventLoopGroup group = null;

    public NettyClient(Context ctx) {
        context = ctx;
    }
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
            Toast.makeText(context, "can not connect with server!", Toast.LENGTH_LONG).show();
            Log.e(NettyClient.class.getName(), e.toString());
        }


    }

    protected void finalize() {

        //group.shutdownGracefully();
    }

    private void login(Channel channel) {

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.CONTROLLER_LOGIN, null, null);

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

    public static ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public static void setChannelHandlerContext(ChannelHandlerContext ctx) {
        channelHandlerContext = ctx;
    }
}

