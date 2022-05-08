package cn.tomo.controller.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.Arrays;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.common.Zip;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;
import cn.tomo.controller.robot.FileRobot;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {


    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        if(!FileRobot.isFileMode()) {
            // send command again
            DataPacketProto.Packet p = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, null, null);
            NettyClient.getChannelHandlerContext().channel().writeAndFlush(p);
        }


        byte[] screenData = packet.getDataSegment1().toByteArray();

        Bitmap bitmap = BitmapFactory.decodeByteArray(screenData,0, screenData.length);
        //Log.i("getScreen","fsfsf" + screenData.length/1024 );
        Configure.getMainActivity().imageShow(bitmap);

        // initialize the configure
        if(!Configure.getInitScreen()) {
            Configure.setPuppetScreenHeight(bitmap.getHeight());
            Configure.setPuppetScreenWidth(bitmap.getWidth());
        }

    }
}
