package top.zhongruitian.utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
    private ChannelHandler[] handlers;
    private int port = 8080;
    private ChannelInitializer<Channel> channelInitializer;

    public Server(ChannelHandler... channelInboundHandlerAdapter) {
        this.handlers = channelInboundHandlerAdapter;
    }

    public Server(int port, ChannelHandler... channelInboundHandlerAdapters) {
        this.handlers = channelInboundHandlerAdapters;
        this.port = port;
    }

    public Server(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public void run() {
        EventLoopGroup workerLoop = new NioEventLoopGroup(), bossLoop = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {

            b.group(bossLoop, workerLoop).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            if (channelInitializer == null) {
                b.childHandler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        if (handlers.length == 0) {
                            throw new IllegalArgumentException("There must be at least on ChannelHandler.");
                        }
                        for (ChannelHandler channelInboundHandlerAdapter : handlers) {
                            ch.pipeline().addLast(channelInboundHandlerAdapter);
                        }
                    }
                });
            } else {
                b.childHandler(this.channelInitializer);
            }
            ChannelFuture f = b.bind(this.port).sync();
            f.channel().closeFuture().sync();

        } catch (Exception ex) {

        } finally {
            bossLoop.shutdownGracefully();
            workerLoop.shutdownGracefully();
        }
    }

}
