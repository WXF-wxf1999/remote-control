package cn.tomo.controller.robot;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.ConcurrentModificationException;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;

//public class CursorRobot implements View.OnTouchListener {
//
//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                sendCursor(event.getRawX(), event.getRawY());
//                break;
//            case MotionEvent.ACTION_UP:
//                v.performClick();
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
//
//    void sendCursor(float x, float y) {
//
//        // Coordinate transformations
//        float xRatio = x / Configure.getControllerScreenWidth();
//        float yRatio = y / Configure.getControllerScreenHeight();
//
//        int xPuppet = (int)(xRatio * Configure.getPuppetScreenWidth());
//        int yPuppet = (int)(yRatio * Configure.getPuppetScreenHeight());
//
//        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.CURSOR_CONTROL_LEFT_DOWN, getBytes(xPuppet), getBytes(yPuppet));
//        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
//
//    }
//
//    public static byte[] getBytes(int data) {
//        byte[] bytes = new byte[4];
//        bytes[0] = (byte) (data & 0xff);
//        bytes[1] = (byte) ((data & 0xff00) >> 8);
//        bytes[2] = (byte) ((data & 0xff0000) >> 16);
//        bytes[3] = (byte) ((data & 0xff000000) >> 24);
//        return bytes;
//    }
//
//
//}
public class CursorRobot implements View.OnTouchListener {

    private float PosY = 0;
    private float CurPosY = 0;
    private static final int MIN_CLICK_DURATION = 1000;
    private long startClickTime = 0;
    private boolean longClickActive = false;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (!longClickActive) {

                    longClickActive = true;
                    startClickTime = Calendar.getInstance().getTimeInMillis();
                }
                PosY = event.getY();
                sendCursor(event.getRawX(), event.getRawY(), Command.CURSOR_CONTROL_LEFT_DOWN);
                break;
            case MotionEvent.ACTION_UP:
                longClickActive = false;
                v.performClick();
                if (CurPosY - PosY > 0 && (Math.abs(CurPosY - PosY) > 25)) {
                    // down
                    sendWheel(-2);

                } else if (CurPosY - PosY < 0 && (Math.abs(CurPosY - PosY) > 25)) {

                    sendWheel(2);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (longClickActive) {

                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
                    if (clickDuration >= MIN_CLICK_DURATION) {
                        // long time pressure
                        longClickActive = false;
                        sendCursor(event.getRawX(), event.getRawY(), Command.CURSOR_CONTROL_RIGHT_DOWN);
                    }
                }

                CurPosY = event.getY();
                break;
            default:
                break;
        }
        return true;
    }

    void sendCursor(float x, float y, Command command) {

        // Coordinate transformations
        float xRatio = x / Configure.getControllerScreenWidth();
        float yRatio = y / Configure.getControllerScreenHeight();

        int xPuppet = (int)(xRatio * Configure.getPuppetScreenWidth());
        int yPuppet = (int)(yRatio * Configure.getPuppetScreenHeight());

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(command, getBytes(xPuppet), getBytes(yPuppet));
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);

    }

    void sendWheel(int distance) {


        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.CURSOR_CONTROL_WHEEL, String.valueOf(distance).getBytes(), null);
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }


}

