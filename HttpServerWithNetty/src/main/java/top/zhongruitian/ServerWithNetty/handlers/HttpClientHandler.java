package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/6 16:30
 * @description
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private HttpMethod method;
    private String url;

    private ChannelHandlerContext ctx;

    public HttpClientHandler(HttpMethod method, String url, ChannelHandlerContext ctx) {
        this.method = method;
        this.url = url;
        this.ctx = ctx;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        FullHttpResponse temp = msg.copy();
        this.ctx.writeAndFlush(temp).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        ctx.writeAndFlush(fullHttpRequest);
    }
}
