package cn.tomo.puppet.netty;

import cn.tomo.puppet.proto.DataPacketProto;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.Semaphore;
import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.common.Log;
import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.netty.coder.*;

public class NettyClient {

    private boolean isOk = false;

    private final Semaphore semaphore = new Semaphore(0,true);

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1048576))
                    .option(ChannelOption.SO_SNDBUF, 1024*1024)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {


                            // get pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                           // must add a decoder or will not receive a message
                            // channelRead will be called in ByteToMessageDecoder, the our channelRead0 will work
                            pipeline.addLast(new Decoder());
                            //pipeline.addLast(new LengthFieldPrepender(4));
                            pipeline.addLast(new Encoder());

                            // add our handler
                            pipeline.addLast(new ChannelHandler());

                        }
                    });

            connectWithServer(bootstrap);


        } catch (Exception e) {

            group.shutdownGracefully();

            Log.error(e.toString());
        }


    }

    protected void finalize() {

        // why? add this line , the function will be called at soon, but in android,
        // this will not happen
        //group.shutdownGracefully();
    }

    private void login(Channel channel) {

        DataPacketProto.Packet.Builder builder = DataPacketProto.Packet.newBuilder();
        builder.setSessionId(0);
        builder.setMessageType(Command.PUPPET_LOGIN.ordinal());
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
                Log.info("fail to connect with server");
            }
            semaphore.release();
        }
    }
}
