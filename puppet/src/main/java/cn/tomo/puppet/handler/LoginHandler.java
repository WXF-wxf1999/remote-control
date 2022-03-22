package cn.tomo.puppet.handler;

import cn.tomo.puppet.Puppet;
import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.proto.DataPacketProto;
import io.netty.channel.Channel;
import cn.tomo.puppet.ui.PuppetUi;

public class LoginHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, Channel channel) {

        int sessionId = packet.getSessionId();
        // store the session id which allocated by server
        Configure.setSessionId(sessionId);

        // show session id information and succession
        PuppetUi.setSessionId(sessionId);

        PuppetUi.setState("state: connect with server successfully...");

    }
}
