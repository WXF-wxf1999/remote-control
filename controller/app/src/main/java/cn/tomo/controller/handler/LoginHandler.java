package cn.tomo.controller.handler;

import android.widget.Toast;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.ui.MainActivity;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class LoginHandler extends AbstractHandler {
    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        int messageType = packet.getMessageType();

        // close login window and store the ctx so that we can send message to puppet on our own initiative
        if(messageType == Command.CONTROLLER_LOGIN.ordinal()) {

            // set ChannelHandlerContext ,we can send message on our own initiative
            NettyClient.setChannelHandlerContext(ctx);
            MainActivity.shutDownLoginWindow();

        }
        else if(messageType == Command.DEVICE_NOT_ONLINE.ordinal()) {

            //Toast.makeText(null, "puppet is not online", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(null, "error!", Toast.LENGTH_LONG).show();
        }
    }
}
