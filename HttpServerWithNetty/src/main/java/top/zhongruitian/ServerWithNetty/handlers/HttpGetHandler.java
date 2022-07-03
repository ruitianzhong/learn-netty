/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import top.zhongruitian.ServerWithNetty.Utils.ContentType;
import top.zhongruitian.ServerWithNetty.Utils.Server;
import top.zhongruitian.ServerWithNetty.Utils.URIResult;
import top.zhongruitian.ServerWithNetty.Utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/1 20:28
 * @description
 */
@Slf4j
public class HttpGetHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static final String DefaultNotFoundText = "<html><head><title>404 Not Found</title></head>" +
            "<body><h1>404 Not Found.</h1></body></html>";

    public static final String DefaultInternalServerError = "<html><head><title>500 Internal Server Error</title>" +
            "</head><body><h1>500 Internal server error.</h1></body></html>";

    /**
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) {
        String uri;
        if (msg.method() != HttpMethod.GET) {
            NotGetMethodHandle(ctx, msg);
        } else if ((uri = msg.uri()) != null) {
            responseIfExist(uri, ctx, msg);
        } else {
            NotFoundHandle(ctx);
        }
    }

    private void NotGetMethodHandle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        NotFoundHandle(ctx);
    }


    private void NotFoundHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultNotFoundText.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.NOT_FOUND, byteBuf, ContentType.HTML)).addListener(ChannelFutureListener.CLOSE);
    }

    private void InternalServerHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultInternalServerError.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf, ContentType.HTML)).addListener(ChannelFutureListener.CLOSE);
    }

    private HttpResponse buildHttpResponseQuickly(HttpResponseStatus status, ByteBuf content, String contentType) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        buildHttpResponseHeaders(response.headers(), contentType);
        return response;
    }

    private void buildHttpResponseHeaders(HttpHeaders headers, String contentType) {
        headers.set(HttpHeaderNames.SERVER, Server.SERVER_NAME);
        headers.set(HttpHeaderNames.CONTENT_TYPE, contentType);
        headers.set(HttpHeaderNames.DATE, new Date().toString());
    }

    private void responseIfExist(String uri, ChannelHandlerContext ctx, FullHttpRequest request) {
        try {
            URIResult result = new URIResult(uri);
            if (result.isSucceed()) {
                processGet(result.getInputStream(), ctx, result.getContentType(), request);
                String msg = request.headers().get(HttpHeaderNames.REFERER) + " visited " + uri;
                logWhenVisited(msg);
            } else if (result.shouldRedirect()) {
                Utils.processRedirect(result.getRedirectURI(), ctx);
            } else {
                NotFoundHandle(ctx);
            }
        } catch (IOException ex) {
            String message = "IOException:" + uri;
            log.debug(message);
            System.out.println(message);
            InternalServerHandle(ctx);
        } catch (URISyntaxException ex) {
            System.out.println("URISyntaxException:" + uri);
        }

    }

    private void logWhenVisited(String msg) {
        System.out.println(msg);
    }

    private void processGet(InputStream inputStream, ChannelHandlerContext ctx, String contentType, FullHttpRequest request) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        inputStream.close();
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.OK, byteBuf, contentType)).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        NotFoundHandle(ctx);
    }

}
