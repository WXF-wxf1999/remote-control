package cn.tomo.puppet.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import cn.tomo.puppet.proto.DataPacketProto;
import cn.tomo.puppet.common.Log;

public class Encoder extends MessageToByteEncoder {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {

        // we should add a judgement
        if(!(o instanceof DataPacketProto.Packet)) {
            Log.error("need a DataPacketProto.Packet type");
            return;
        }

        // object o is a proto object,before this step,we have transformed the DataPacket to DataPacketProto
        byte[] data = ((DataPacketProto.Packet) o).toByteArray();
        int length = data.length;
        // write into byteBUf and send message

        // data length -- object data
        byteBuf.writeIntLE(length);
        byteBuf.writeBytes(data);
    }
}