package cn.tomo.controller.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.common.Zip;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {


    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        DataPacketProto.Packet p = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, null, null);
        // send command
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(p);
        //byte[] screenData = Zip.Decompress(packet.getDataSegment1().toByteArray());
        byte[] screenData = packet.getDataSegment1().toByteArray();
        Configure.getMainActivity().imageShow(screenData);
    }
}
