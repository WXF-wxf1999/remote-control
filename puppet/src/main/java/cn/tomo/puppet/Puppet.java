package cn.tomo.puppet;
import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.common.Log;
import cn.tomo.puppet.netty.NettyClient;

public class Puppet {

    public static void main(String[] args) {

        Log.info("puppet start");

        final NettyClient nettyClient = new NettyClient();

        nettyClient.start();

    }
}


