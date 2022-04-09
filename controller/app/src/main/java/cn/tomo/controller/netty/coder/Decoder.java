package cn.tomo.controller.netty.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

import cn.tomo.controller.proto.DataPacketProto;
public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        int recvLength = byteBuf.readableBytes();

        // read from begin
        byteBuf.resetReaderIndex();

        int length = byteBuf.readIntLE();

        // if the received data is too long, netty will call decode some times,
        // so, we verify the data length,if length is not enough,return
//        if(recvLength != length + 4) {
//            byteBuf.resetReaderIndex();
//            return;
//        }

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
