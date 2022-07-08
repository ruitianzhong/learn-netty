package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/6 16:30
 * @description
 */
public class HttpClientHandler extends SimpleChannelInboundHandler<FullHttpResponse> {
    private FullHttpRequest request;

    private String url;
    private String host;
    private int port;


    private ChannelHandlerContext ctx;

    public HttpClientHandler(FullHttpRequest request, ChannelHandlerContext ctx, String url, String host, int port) {
        this.request = request.copy();
        this.ctx = ctx;
        this.host = host;
        this.port = port;
        this.url = url;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        FullHttpResponse temp = msg.copy();
        this.ctx.writeAndFlush(temp).addListener(ChannelFutureListener.CLOSE);
        ctx.close();//critical code to ensure availability here , but I don't know why?
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String referer = request.headers().get(HttpHeaderNames.REFERER);
        if (referer != null) {
            if (port == 80) {
                request.headers().set(HttpHeaderNames.REFERER, host + url);
            } else {
                request.headers().set(HttpHeaderNames.REFERER, host + ":" + port + url);
            }
        }
        String Host = request.headers().get(HttpHeaderNames.HOST);
        if (Host != null) {
            request.headers().set("Host", host);
        }
        ctx.writeAndFlush(request);
    }
}
