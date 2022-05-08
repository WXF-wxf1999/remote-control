package cn.tomo.controller.robot;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Arrays;

import cn.tomo.controller.common.Command;
import cn.tomo.controller.common.Configure;
import cn.tomo.controller.netty.NettyClient;
import cn.tomo.controller.proto.DataPacketProto;
import cn.tomo.controller.proto.PacketBuilder;

public class FileRobot {

    private static boolean fileMode = false;

    private static String fileName = null;

    public static void setFileName(String fileName) {
        FileRobot.fileName = fileName;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void requestDirectory(String directory) {

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.DRIVER_REQUEST, directory.getBytes(), null);
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
    }

    public static void sendFileName(String fileName, String driverName) {

        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.FILE_RESEARCH,
                fileName.getBytes(), driverName.getBytes());
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
    }

    public static void requestFileData(String filePath) {
        DataPacketProto.Packet packet = PacketBuilder.buildPacket(Command.FILE_REQUEST,
                filePath.getBytes(), null);
        NettyClient.getChannelHandlerContext().channel().writeAndFlush(packet);
    }

    public static void writeFileData(byte[] fileData) {

        try
        {

            RandomAccessFile randomFile = new RandomAccessFile(Configure.getMainActivity().getExternalFilesDir(null) + "/" + fileName, "rw");

            long fileLength = randomFile.length();
            randomFile.seek(fileLength);
            randomFile.write(fileData);
            Log.i("kokokoko", "length" + fileData.length);
            randomFile.close();
        }
        catch(Exception e)
        {
            Log.i(FileRobot.class.getName(), e.toString());

        }

    }

    public static void setFileMode(boolean fileMode) {
        FileRobot.fileMode = fileMode;
    }

    public static boolean isFileMode() {
        return fileMode;
    }
}
