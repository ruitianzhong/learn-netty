package top.zhongruitian.ServerWithNetty.Utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    private String host;
    private int count = 0;
    private int maxTryTimes = 1000000;
    private ChannelHandler[] handlers;
    private int port;

    private int ConnectedTimeOutMills = 10000;

    private EventLoopGroup eventExecutors;

    private ChannelFuture channelFuture;

    private SocketChannel socketChannel;

    public Client(String host, int port, ChannelHandler... handler) {
        if (handler.length == 0) {
            throw new IllegalArgumentException("Need at least handler.");
        }
        this.host = host;
        this.port = port;
        this.handlers = handler;
    }

    public void start() throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        eventExecutors = eventLoopGroup;
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<>() {
            @Override
            protected void initChannel(Channel ch) {
                ch.pipeline().addLast(handlers);
            }
        }).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, ConnectedTimeOutMills);
        channelFuture = bootstrap.connect(host, port);
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {

            } else {
                if (count <= maxTryTimes) {
                    final EventLoop eventLoop = future.channel().eventLoop();
                    eventLoop.execute(() -> {
                        try {
                            start();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    count++;
                }
            }
        });
        channelFuture.channel().closeFuture().sync();
        eventExecutors.shutdownGracefully();
    }
}
