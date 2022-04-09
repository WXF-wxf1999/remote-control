package cn.tomo.controller.robot;

import java.util.Timer;
import cn.tomo.controller.common.Command;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;

public class ScreenRobot {

    private static Timer timer = null;

    public static void requestScreen() {

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, null, null);
        // send command
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.DESKTOP_CONTROL, null, null);
//                // send command
//                NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
//                Log.i(ScreenRobot.class.getName(),"request screen");
//            }
//        };
//
//        timer = new Timer();
//
//        long intervalPeriod = 1000;
//
//        // schedules the task to be run in an interval
//        timer.scheduleAtFixedRate(task, 0, intervalPeriod);
    }

    public static void cancelControl() {

        if( timer!=null ) {
            timer.cancel();
        }
    }
//    public static  synchronized void collectScreenImage(DataPacketProto.Packet packet) {
//
//        byte[] screenData = packet.getDataSegment1().toByteArray();
//
//        recvData.put(screenData);
//        recvLength += screenData.length;
//
//        Log.i("asas","recvData"+recvLength);
//        if(packet.getDataSegment2().toByteArray().length != 0) {
//
//            try {
//                Bitmap bitmap = BitmapFactory.decodeByteArray(recvData.array(),0,recvLength);
//                Configure.getMainActivity().imageShow(bitmap);
//                recvLength = 0;
//                recvData.clear();
//            } catch (Exception e) {
//                Log.d(DesktopHandler.class.getName(), e.toString());
//            }
//        }
//
//    }
}
