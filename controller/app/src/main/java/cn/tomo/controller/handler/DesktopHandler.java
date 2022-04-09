package cn.tomo.controller.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.proto.DataPacketProto;
import io.netty.channel.ChannelHandlerContext;

public class DesktopHandler extends AbstractHandler {


    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        byte[] screenData = packet.getDataSegment1().toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(screenData,0, screenData.length);
        Configure.getMainActivity().imageShow(bitmap);
    }
}
