package cn.tomo.controller.handler;

import android.widget.Toast;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.ui.MainActivity;
import io.netty.channel.Channel;

public class LoginHandler extends AbstractHandler {
    @Override
    public void handleIo(DataPacketProto.Packet packet, Channel channel) {

        int messageType = packet.getMessageType();
        if(messageType == Command.CONTROLLER_LOGIN.ordinal()) {
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
