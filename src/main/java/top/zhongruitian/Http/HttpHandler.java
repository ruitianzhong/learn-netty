package top.zhongruitian.Http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String message = "<html><head><title>test</title></head><body>Hello.html world!</body></html>";
        HttpResponse httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK, Unpooled.copiedBuffer(message, CharsetUtil.UTF_8));
        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        System.out.println(fullHttpRequest.toString() + "  ");
        ByteBuf byteBuf = fullHttpRequest.content();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Reference count:" + fullHttpRequest.refCnt());
        System.out.println(byteBuf.toString(StandardCharsets.UTF_8));
        channelHandlerContext.writeAndFlush(httpResponse).addListener(ChannelFutureListener.CLOSE);
    }


}
