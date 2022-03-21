package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Configure;
import cn.tomo.puppet.proto.DataPacketProto;
import io.netty.channel.Channel;

public class LoginHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, Channel channel) {

        // store the session id which allocated by server
        Configure.setSessionId(packet.getSessionId());

        // show session id information and succession
    }
}
