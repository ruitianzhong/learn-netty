package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import top.zhongruitian.ServerWithNetty.Utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class HttpGetHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static ByteBuf DefaultNotFoundText = Unpooled.copiedBuffer("<html><head><title>404 Not Found</title></head><body><h1>404 Not Found.</h1></body></html>".getBytes(CharsetUtil.UTF_8));


    /**
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        String uri;
        if (msg.method() != HttpMethod.GET) {
            NotGetMethodHandle(ctx, msg);
        } else if ((uri = msg.uri()) != null) {
            responseIfExist(uri, ctx, msg);
        }
    }


    private void NotGetMethodHandle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        System.out.println("test");
        NotFoundHandle(ctx);
    }


    private void NotFoundHandle(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.NOT_FOUND, DefaultNotFoundText))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private HttpResponse buildHttpResponseQuickly(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
        return response;
    }

    private void responseIfExist(String uri, ChannelHandlerContext ctx, FullHttpRequest request) throws URISyntaxException, IOException {
        InputStream inputStream;
        if (checkIfResourceExist(uri) && (inputStream = getInputStream(Utils.parseURIToList(new URI(uri)))) != null) {//URISyntaxException Here!
            byte[] bytes = inputStream.readAllBytes();
            ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
            inputStream.close();
            ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.OK, byteBuf))
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            NotFoundHandle(ctx);
        }

    }

    private boolean checkIfResourceExist(String uri) throws URISyntaxException {
        return true;
    }

    private InputStream getInputStream(List<String> strings) {
        return this.getClass().getClassLoader().getResourceAsStream("Hello.html");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NotFoundHandle(ctx);
        cause.printStackTrace();
    }
}
