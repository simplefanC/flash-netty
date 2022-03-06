package the.flash;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

public class NettyServer {

    private static final int BEGIN_PORT = 8000;

    public static void main(String[] args) {
        // 监听端口，accept 新连接的线程组
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        // 处理每一条连接的数据读写的线程组
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 引导类 引导我们进行服务端的启动工作
        final ServerBootstrap serverBootstrap = new ServerBootstrap();
        final AttributeKey<Object> clientKey = AttributeKey.newInstance("clientKey");
        serverBootstrap
                // 给引导类配置两大线程组
                .group(boosGroup, workerGroup)
                // 指定 IO 模型为NIO
                // NioServerSocketChannel: 对 NIO 类型的连接的抽象(类比ServerSocket)
                .channel(NioServerSocketChannel.class)
                // 可以给服务端的channel，也就是NioServerSocketChannel指定一些自定义属性，可以通过channel.attr()取出这个属性
                // 指定我们服务端channel的一个serverName属性，属性值为nettyServer
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")
                // 给每一条连接指定自定义属性，然后后续我们可以通过channel.attr()取出该属性
                .childAttr(clientKey, "clientValue")
                // 给服务端channel设置一些属性
                // 表示系统用于临时存放已完成三次握手的请求的队列的最大长度，如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 给每条连接设置一些TCP底层相关的属性
                // 是否开启TCP底层心跳机制，true为开启
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 是否开启Nagle算法，true表示关闭
                // 如果要求高实时性，有数据发送时就马上发送，就关闭，如果需要减少发送次数减少网络交互，就开启
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 定义后续每条连接的数据读写，业务处理逻辑
                // NioSocketChannel: Netty 对 NIO 类型的连接的抽象(类比Socket)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    protected void initChannel(NioSocketChannel ch) {
                        System.out.println(ch.attr(clientKey).get());
                    }
                });

        bind(serverBootstrap, BEGIN_PORT);
    }

    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        // bind是一个异步的方法，调用之后是立即返回的，他的返回值是一个ChannelFuture
        // 给这个ChannelFuture添加一个监听器GenericFutureListener
        serverBootstrap.bind(port).addListener(future -> {
            // 监听端口是否绑定成功
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                // 自动绑定递增端口
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
