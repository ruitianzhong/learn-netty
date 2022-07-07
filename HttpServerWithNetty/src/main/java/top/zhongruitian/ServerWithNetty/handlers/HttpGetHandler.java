/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;
import top.zhongruitian.ServerWithNetty.Utils.Client;
import top.zhongruitian.ServerWithNetty.Utils.ServerUtils;
import top.zhongruitian.ServerWithNetty.Utils.URIResult;
import top.zhongruitian.ServerWithNetty.configuration.HostAndPortList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/7/1 20:28
 * @description
 */
@Slf4j
public class HttpGetHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    /**
     * @param ctx the {@link ChannelHandlerContext} which this {@link SimpleChannelInboundHandler}
     *            belongs to
     * @param msg the message to handle
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws InterruptedException {
        String uri;
        if (msg.method() != HttpMethod.GET) {
            NotGetMethodHandle(ctx, msg);
        } else if ((uri = msg.uri()) != null) {
            responseIfExist(uri, ctx, msg);
        } else {
            ServerUtils.NotFoundHandle(ctx);
        }
    }

    private void NotGetMethodHandle(ChannelHandlerContext ctx, FullHttpRequest msg) {
        ServerUtils.NotFoundHandle(ctx);
    }

    private void responseIfExist(String uri, ChannelHandlerContext ctx, FullHttpRequest request) {
        try {
            URIResult result = new URIResult(uri);
            if (result.isMatched()) {
                HostAndPortList list = result.getHostAndPortList();
                Client client = new Client(list.getServerHostAndPort(), new HttpClientChannelInitializer(HttpMethod.GET
                        , uri, ctx));
                client.start();
            } else if (result.isSucceed()) {
                processGet(result.getInputStream(), ctx, result.getContentType(), request);
                String msg = request.headers().get(HttpHeaderNames.FROM) + " visited " + uri;
                logWhenVisited(msg);
            } else if (result.shouldRedirect()) {
                ServerUtils.processRedirect(result.getRedirectURI(), ctx);
            } else {
                ServerUtils.NotFoundHandle(ctx);//TODO LoadBalancer
            }
        } catch (IOException ex) {
            String message = "IOException:" + uri;
            log.debug(message);
            System.out.println(message);
            ServerUtils.InternalServerHandle(ctx);
        } catch (URISyntaxException ex) {
            System.out.println("URISyntaxException:" + uri);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void logWhenVisited(String msg) {
        System.out.println(new Date() + "  " + msg);
    }

    private void processGet(InputStream inputStream, ChannelHandlerContext ctx, String contentType, FullHttpRequest request) throws IOException {
        byte[] bytes = inputStream.readAllBytes();
        ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
        inputStream.close();
        ctx.writeAndFlush(ServerUtils.buildHttpResponseQuickly(HttpResponseStatus.OK, byteBuf, contentType)).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ServerUtils.NotFoundHandle(ctx);
    }

}
