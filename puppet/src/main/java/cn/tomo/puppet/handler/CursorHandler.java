package cn.tomo.puppet.handler;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.robot.CursorRobot;
import io.netty.channel.ChannelHandlerContext;

import java.awt.event.InputEvent;

public class CursorHandler extends AbstractHandler{

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        byte[] xBytes = packet.getDataSegment1().toByteArray();
        byte[] yBytes = packet.getDataSegment2().toByteArray();

        /*
         *  InputEvent.BUTTON1_MASK left button
         *  InputEvent.BUTTON2_MASK
         *  InputEvent.BUTTON3_MASK right button
         */
        int msgType = packet.getMessageType();

        if(msgType == Command.CURSOR_CONTROL_LEFT_DOWN.ordinal()) {
            CursorRobot.getInstance().mouseEvent(getInt(xBytes), getInt(yBytes), InputEvent.BUTTON1_MASK);
        }
        else if(msgType == Command.CURSOR_CONTROL_RIGHT_DOWN.ordinal()) {
            CursorRobot.getInstance().mouseEvent(getInt(xBytes), getInt(yBytes), InputEvent.BUTTON3_MASK);
        }
        else if(msgType == Command.CURSOR_CONTROL_WHEEL.ordinal()) {
            // support for negative number
            CursorRobot.getInstance().mouseWheel(Integer.parseInt(new String(xBytes)));
        }

    }


    public static int getInt(byte[] bytes) {
        return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) | (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
    }
}
