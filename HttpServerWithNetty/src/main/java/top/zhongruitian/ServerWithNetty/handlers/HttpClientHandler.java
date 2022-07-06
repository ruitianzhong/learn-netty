package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

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

    public HttpClientHandler(HttpMethod method, String url, ByteBuf byteBuf) {

    }

    public HttpClientHandler(FullHttpRequest request) {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        System.out.println(msg.toString());
        String content = msg.content().toString(StandardCharsets.UTF_8);
        ByteBuf byteBuf = Unpooled.copiedBuffer(content.getBytes(CharsetUtil.UTF_8));
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
        if (msg.status() == HttpResponseStatus.OK) {
            this.ctx.writeAndFlush(fullHttpResponse);
        } else {
            //do nothing.
        }

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) {

        FullHttpRequest fullHttpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, url);
        ctx.writeAndFlush(fullHttpRequest);
    }


}
