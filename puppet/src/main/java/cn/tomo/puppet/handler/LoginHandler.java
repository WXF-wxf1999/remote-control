package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.ui.PuppetUi;
import io.netty.channel.ChannelHandlerContext;

public class LoginHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        int sessionId = packet.getSessionId();
        // store the session id which allocated by server
        Configure.setSessionId(sessionId);

        // show session id information and succession
        PuppetUi.setSessionId(sessionId);

        PuppetUi.setState("state: connect with server successfully...");

    }
}
