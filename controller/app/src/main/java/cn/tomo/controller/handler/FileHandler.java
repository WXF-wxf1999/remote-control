package cn.tomo.controller.handler;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.robot.FileRobot;
import io.netty.channel.ChannelHandlerContext;

public class FileHandler extends AbstractHandler {

    @Override
    public void handleIo(DataPacketProto.Packet packet, ChannelHandlerContext ctx) {

        int type = packet.getMessageType();
        byte[] data = packet.getDataSegment1().toByteArray();

        if(type == Command.DRIVER_REQUEST.ordinal()) {

            Configure.getMainActivity().updateSpinner(new String(data));
        } else if(type == Command.FILE_RESEARCH.ordinal()) {

            Configure.getMainActivity().insertFileItem(new String(data));
        } else if(type == Command.FILE_REQUEST.ordinal()) {

            FileRobot.writeFileData(packet.getDataSegment1().toByteArray());

            if(packet.getDataSegment2().size() == 0) {
                FileRobot.requestFileData(FileRobot.getFileName());
            } else {

                Configure.showInformation("Receive file successfully");
            }

        }

    }
}
