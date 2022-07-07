package top.zhongruitian.ServerWithNetty.Utils;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import top.zhongruitian.ServerWithNetty.configuration.DefaultHostAndPortList;
import top.zhongruitian.ServerWithNetty.configuration.HostAndPort;
import top.zhongruitian.ServerWithNetty.configuration.HostAndPortList;

public class Client {
    private int count = 0;
    private int maxReTryTimes = 10;
    private ChannelHandler[] handlers;

    private int ConnectedTimeOutMills = 10000;

    private EventLoopGroup eventExecutors;

    private ChannelFuture channelFuture;

    private SocketChannel socketChannel;

    private HostAndPortList hostAndPortList;

    public Client(String host, int port, ChannelHandler... handlers) {
        this(new HostAndPort(host, port), handlers);
    }

    public Client(HostAndPort hostAndPort, ChannelHandler... handlers) {
        if (handlers == null || handlers.length == 0) {//NPE here before
            throw new IllegalArgumentException("Need at least one handler.");
        }
        HostAndPortList hostAndPortList = new DefaultHostAndPortList();
        hostAndPortList.add(hostAndPort);
        this.hostAndPortList = hostAndPortList;
        this.handlers = handlers;
    }

    public Client(HostAndPortList hostAndPortList, ChannelHandler... handlers) {
        if (handlers == null || handlers.length == 0) {
            throw new IllegalArgumentException("Need at least one handler.");
        }
        this.handlers = handlers;
        this.hostAndPortList = hostAndPortList;
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
        HostAndPort server = hostAndPortList.getServerHostAndPort();
        channelFuture = bootstrap.connect(server.getHost(), server.getPort());
        channelFuture.addListener((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                if (count <= maxReTryTimes) {
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
