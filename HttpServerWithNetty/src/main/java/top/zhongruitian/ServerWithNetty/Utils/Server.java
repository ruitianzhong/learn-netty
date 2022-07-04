package top.zhongruitian.ServerWithNetty.Utils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Server {
    public static final String SERVER_NAME = "Http Server With Netty";
    private ChannelHandler[] handlers;
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private int port = 10086;
    private ChannelInitializer<Channel> channelInitializer;
    private ServerConfiguration configuration = null;

    public Server(ChannelHandler... channelInboundHandlerAdapter) {
        this.handlers = channelInboundHandlerAdapter;
    }

    public Server(int port, ChannelHandler... channelInboundHandlerAdapters) {
        this.handlers = channelInboundHandlerAdapters;
        this.port = port;
    }


    public Server(ServerConfiguration configuration, ChannelHandler... channelHandlers) {
        this.handlers = channelHandlers;
        this.port = 10086;
        this.configuration = configuration;
    }

    public Server(ChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

    public Server(ChannelInitializer channelInitializer, ServerConfiguration configuration) {
        this.configuration = configuration;
        this.port = configuration.getPort();
        this.channelInitializer = channelInitializer;//bug here
    }

    public Server(ChannelInitializer channelInitializer, int port) {
        this.port = port;
        this.channelInitializer = channelInitializer;
    }

    public void run() {

        LOGGER.debug("Server start on port {}", port);
        System.out.println("Server start on port " + port);
        EventLoopGroup workerLoop = new NioEventLoopGroup(), bossLoop = new NioEventLoopGroup();
        ServerBootstrap b = new ServerBootstrap();
        try {

            b.group(bossLoop, workerLoop).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

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
            LOGGER.debug("Server starting...");
            ChannelFuture f = b.bind(this.port).sync();
            f.channel().closeFuture().sync();

        } catch (Exception ex) {

        } finally {
            bossLoop.shutdownGracefully();
            workerLoop.shutdownGracefully();
        }
    }

}
