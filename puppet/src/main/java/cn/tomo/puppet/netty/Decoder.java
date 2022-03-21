package cn.tomo.puppet.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

import cn.tomo.puppet.proto.DataPacketProto;

public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        if(byteBuf.readableBytes() < 4) {
            return;
        }

        // read from begin
        byteBuf.markReaderIndex();

        int length = byteBuf.readIntLE();
        if(length < 0) {
            channelHandlerContext.close();
        }

        byte[] data = new byte[length];
        byteBuf.readBytes(data);

        // change data to proto object
        // because the proto structure contains bytes type and need to process
        DataPacketProto.Packet packet = DataPacketProto.Packet.parseFrom(data);

        // pass to channelRead0
        list.add(packet);

    }
}
