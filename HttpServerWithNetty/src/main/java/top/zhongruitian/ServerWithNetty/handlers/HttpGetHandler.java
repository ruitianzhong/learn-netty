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
import top.zhongruitian.ServerWithNetty.Utils.Utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/1 20:28
 * @description
 */
@Slf4j
public class HttpGetHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private static String Default_HTML_File_Name = "index.html";

    public static final String DefaultNotFoundText = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found.</h1></body></html>";

    private static String STATIC_DIRECTORY_NAME = "static" + File.separator;
    public static final String DefaultInternalServerError = "<html><head><title>500 Internal Server Error</title></head><body><h1>500 Internal server error.</h1></body></html>";

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

        } else {
            NotFoundHandle(ctx);
        }
    }


    private void NotGetMethodHandle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        NotFoundHandle(ctx);
    }


    private void NotFoundHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultNotFoundText.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.NOT_FOUND, byteBuf))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private void InternalServerHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultInternalServerError.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private HttpResponse buildHttpResponseQuickly(HttpResponseStatus status, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, content);
        buildHttpResponseHeaders(response.headers());
        return response;
    }

    private void buildHttpResponseHeaders(HttpHeaders headers) {
        headers.set(HttpHeaderNames.SERVER, "Http Server With Netty");
        headers.set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=utf-8");
        headers.set(HttpHeaderNames.DATE, new Date().toString());

    }

    private void responseIfExist(String uri, ChannelHandlerContext ctx, FullHttpRequest request) {
        try {

            InputStream inputStream = getInputStream(uri);

            if (inputStream != null) {

                byte[] bytes = inputStream.readAllBytes();
                ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                inputStream.close();
                ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.OK, byteBuf))
                        .addListener(ChannelFutureListener.CLOSE);
                String info = request.headers().get(HttpHeaderNames.REFERER) + " visited " + uri;
                System.out.println(info);
                log.info(info);
            } else {

                NotFoundHandle(ctx);
            }
        } catch (IOException ex) {
            String message = "IOException:" + uri;
            log.debug(message);
            System.out.println(message);
            InternalServerHandle(ctx);
        } catch (URISyntaxException ex) {

        }

    }


    private InputStream getInputStream(String uri) throws FileNotFoundException, URISyntaxException {

        String path = getFilteredPathName(Utils.parseURIToList(new URI(uri)));

        File file = getResource(path);

        if (file != null && file.exists() && file.isFile()) {
            return new FileInputStream(file);
        }
        return this.getClass().getClassLoader().getResourceAsStream(STATIC_DIRECTORY_NAME + "Hello.html");
    }


    private File getResource(String uri) {
        return new File(uri);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        NotFoundHandle(ctx);
    }

    private String getFilteredPathName(List<String> list) {

        if (list.size() == 0) {
            return Default_HTML_File_Name;
        }
        StringBuffer sb = new StringBuffer();
        for (String s : list) {
            sb.append(s);
            sb.append(File.separator);
        }
        return sb.toString();
    }
}
