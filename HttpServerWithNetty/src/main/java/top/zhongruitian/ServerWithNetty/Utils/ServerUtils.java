/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.Utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class ServerUtils {
    public static final String DefaultInternalServerError = "<html><head><title>500 Internal Server Error</title>" +
            "</head><body><h1>500 Internal server error.</h1></body></html>";
    public static final String DefaultNotFoundText = "<html><head><title>404 Not Found</title></head>" +
            "<body><h1>404 Not Found.</h1></body></html>";

    public static void processRedirect(String redirectURI, ChannelHandlerContext ctx) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TEMPORARY_REDIRECT);
        fullHttpResponse.headers().set("Location", redirectURI);
        fullHttpResponse.headers().set(HttpHeaderNames.SERVER, Server.SERVER_NAME);
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

    public static void NotFoundHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultNotFoundText.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.NOT_FOUND, byteBuf, ContentType.HTML)).addListener(ChannelFutureListener.CLOSE);
    }

    public static void buildHttpResponseHeaders(HttpHeaders headers, String contentType) {
        headers.set(HttpHeaderNames.SERVER, Server.SERVER_NAME);
        headers.set(HttpHeaderNames.CONTENT_TYPE, contentType);
        headers.set(HttpHeaderNames.DATE, new Date().toString());
    }

    public static HttpResponse buildHttpResponseQuickly(HttpResponseStatus status, ByteBuf content, String contentType) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        buildHttpResponseHeaders(response.headers(), contentType);
        return response;
    }

    public static void InternalServerHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultInternalServerError.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf, ContentType.HTML)).addListener(ChannelFutureListener.CLOSE);
    }

}
