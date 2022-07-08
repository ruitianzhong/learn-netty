package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpClientChannelInitializer extends ChannelInitializer<Channel> {

    private HttpClientHandler handler;

    public HttpClientChannelInitializer(FullHttpRequest request, ChannelHandlerContext ctx, String host, int port, String url) {
        handler = new HttpClientHandler(request, ctx, url, host, port);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 1024 * 10));
        ch.pipeline().addLast(handler);
    }
}
