package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerChannelHandlerInitializer extends ChannelInitializer<Channel> {

    /**
     * @param ch the {@link Channel} which was registered.
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(65535));
        ch.pipeline().addLast(new HttpGetHandler());
    }
}
