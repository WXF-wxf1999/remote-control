package cn.tomo.puppet.netty;
import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.common.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.Semaphore;

public class NettyClient {

    private boolean isOk = false;

    private final Semaphore semaphore = new Semaphore(0,true);

    public void start() {

        EventLoopGroup group = new NioEventLoopGroup();
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
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(new StringEncoder());
                        // add our handler
                        pipeline.addLast(new ChannelHandler());

                    }
                });

        connect(bootstrap);
    }
    private void connect(Bootstrap bootstrap) {

        // try three times
        int retry = 3;
        for (int i = 0; i < retry; i++) {
            try {
                if(isOk) {
                    return;
                }
                ChannelFuture channelFuture =bootstrap.connect(Configure.getHost(),Configure.getPort()).sync();
                channelFuture.addListener(new Listener());
                // wait for Listener
                semaphore.acquire();

            }catch (Exception e) {
                Log.error(e.getMessage());
            }
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
