package top.zhongruitian.Http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HttpHandlerWithoutAggregator extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        HttpObject httpObject = (HttpObject) msg;
        System.out.println(httpObject.decoderResult().isSuccess() + httpObject.toString());


//        ctx.fireChannelRead(httpResponse);
        String message = "<html><head><title>test</title></head><body>Hello.html world!</body></html>";
        HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        ctx.writeAndFlush(httpResponse);
    }
}
