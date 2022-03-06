package the.flash.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import the.flash.protocol.PacketCodeC;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    /**
     * 解码逻辑
     *
     * @param ctx
     * @param in  传递进来的时候就已经是 ByteBuf 类型，所以我们不再需要强转
     * @param out 通过往这个 List 里面添加解码后的结果对象，就可以自动实现结果往下一个 handler 进行传递
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        out.add(PacketCodeC.INSTANCE.decode(in));
    }
}
