package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;

public class HttpClientChannelInitializer extends ChannelInitializer<Channel> {

    private HttpClientHandler handler;

    public HttpClientChannelInitializer(HttpMethod method, String url, ChannelHandlerContext ctx) {
        handler = new HttpClientHandler(method, url, ctx);
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new HttpClientCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(65535));
        ch.pipeline().addLast(handler);
    }
}
