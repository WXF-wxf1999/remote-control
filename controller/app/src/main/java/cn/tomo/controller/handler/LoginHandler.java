package cn.tomo.controller.handler;

import android.app.AlertDialog;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.ui.MainActivity;
import io.netty.channel.ChannelHandlerContext;

public class LoginHandler extends AbstractHandler {
    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        String message = null;
        int messageType = packet.getMessageType();

        MainActivity.shutDownLoginWindow();

        // close login window and store the ctx so that we can send message to puppet on our own initiative
        if(messageType == Command.CONTROLLER_LOGIN.ordinal()) {

            // set ChannelHandlerContext ,we can send message on our own initiative
            NettyClient.setChannelHandlerContext(ctx);
            message = "login successfully!";

        }
        else if(messageType == Command.DEVICE_NOT_ONLINE.ordinal()) {
            message = "device is not online!";
        }
        else {
            message = "some unknown error occurs! ";
        }
        AlertDialog alertDialog1 = new AlertDialog.Builder(Configure.getMainActivity())
                .setTitle("tips:")//标题
                .setMessage(message)//内容
                .create();
        alertDialog1.show();
    }
}
