package cn.tomo.puppet.robot;

import cn.tomo.puppet.common.Command;
import cn.tomo.puppet.common.Log;
import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.proto.PacketBuilder;
import cn.tomo.puppet.robot.filesearch.LocalFileSearcher;
import cn.tomo.puppet.robot.filesearch.bean.SearchConfig;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class FileRobot {

    private static byte[] fileData = null;
    private static int curPosition = 0;
    private static boolean over = false;

    public static String getDirectory(String directory) {

        // get driver letter
        File[] roots = File.listRoots();

        StringBuilder data = new StringBuilder();
        for (File root : roots) {
            data.append(root.toString());
            data.append("6666666");
        }
        return data.toString();
    }

    public static String searchFile(String fileName, String driver) {

        SearchConfig searchConfig = new SearchConfig();
        searchConfig.setStartFolder(new File(driver));
        searchConfig.setThreadNum(50);
        LocalFileSearcher searcher = new LocalFileSearcher(searchConfig);

        searcher.doSearch(fileName);
        searcher.waiting();

        StringBuilder result = new StringBuilder();
        for(String i : searcher.getFilePathSet()) {
            result.append(i);
            result.append("6666666");
        }
        return result.toString();
    }

    public static String searchFile(String fileName) {

        LocalFileSearcher searcher = new LocalFileSearcher();
        searcher.doSearch(fileName);
        searcher.waiting();

        StringBuilder result = new StringBuilder();
        for(String i : searcher.getFilePathSet()) {
            result.append(i);
            result.append("6666666");
        }
        return result.toString();
    }

    public static void sendFileData(String filePath, ChannelHandlerContext ctx) {

        try {
            // support up to 2 GB file
            Path path = Paths.get(filePath);

            if(fileData == null) {
//                if(over) {
//                    over = false;
//                    return;
//                }
                fileData = Files.readAllBytes(path);
            }

            byte[] data = null;

            if(curPosition + 1024*1024 <= fileData.length) {

                data = Arrays.copyOfRange(fileData, curPosition,curPosition + 1024*1024);
                DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.FILE_REQUEST,
                        data, null);
                ctx.channel().writeAndFlush(packet1);
                curPosition += 1024*1024;
            } else {

                // send file data lastly
                data = Arrays.copyOfRange(fileData, curPosition,fileData.length);
                fileData = null;
                curPosition = 0;
                over = true;

                DataPacketProto.Packet packet1 = PacketBuilder.buildPacket(Command.FILE_REQUEST,
                        data, new byte[] {1});
                ctx.channel().writeAndFlush(packet1);
            }

        } catch (IOException e) {
            Log.info(e.toString());
        }

    }
}
