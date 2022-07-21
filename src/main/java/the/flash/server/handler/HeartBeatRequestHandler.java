package the.flash.server.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import the.flash.protocol.request.HeartBeatRequestPacket;
import the.flash.protocol.response.HeartBeatResponsePacket;

/**
 * 服务端这边其实只要在收到心跳之后回复客户端，给客户端发送一个心跳响应包即可。
 * 如果在一段时间之内客户端没有收到服务端发来的数据，也可以判定这条连接为假死状态。
 */
@ChannelHandler.Sharable
public class HeartBeatRequestHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {
    public static final HeartBeatRequestHandler INSTANCE = new HeartBeatRequestHandler();

    private HeartBeatRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket requestPacket) {
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
