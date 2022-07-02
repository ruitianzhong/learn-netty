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
    private static String Default_Content_Type = ContentType.HTML;

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
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.NOT_FOUND, byteBuf, ContentType.HTML))
                .addListener(ChannelFutureListener.CLOSE);
    }

    private void InternalServerHandle(ChannelHandlerContext ctx) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(DefaultInternalServerError.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.INTERNAL_SERVER_ERROR, byteBuf, ContentType.HTML))
                .addListener(ChannelFutureListener.CLOSE);
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
            List<String> list = Utils.parseURIToList(new URI(uri));
            InputStream inputStream = getInputStream(list);
            if (inputStream != null) {
                byte[] bytes = inputStream.readAllBytes();
                ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
                inputStream.close();
                ctx.writeAndFlush(buildHttpResponseQuickly(HttpResponseStatus.OK, byteBuf,
                                ContentType.getContentType(list.size() == 0 ? null : list.get(list.size() - 1))))
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


    private InputStream getInputStream(List<String> list) throws FileNotFoundException, URISyntaxException {

        String path = getFilteredPathName(list);

        File file = getResource(path);

        if (file != null && file.exists() && file.isFile()) {
            return new FileInputStream(file);
        }
        return this.getClass().getClassLoader().getResourceAsStream(STATIC_DIRECTORY_NAME + "hello.html");
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
