/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.ServerWithNetty.Utils;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import top.zhongruitian.ServerWithNetty.exceptions.BadRequestException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<String> parseURIToList(URI uri) {

        List<String> list = new ArrayList<>();
        String path = uri.getPath();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(0) != '/') {
                throw new BadRequestException("The first character of uri is not /");
            }

            char temp = path.charAt(i);
            if (temp == '/') {
                if (!sb.isEmpty() && sb.length() > 0) {
                    list.add(sb.toString());
                    sb.delete(0, sb.length());
                }
                continue;
            }
            sb.append(temp);
        }
        if (!sb.isEmpty()) {
            list.add(sb.toString());
        }

        return list;

    }
    public static void processRedirect(String redirectURI, ChannelHandlerContext ctx) {
        FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.TEMPORARY_REDIRECT);
        fullHttpResponse.headers().set("Location", redirectURI);
        ctx.writeAndFlush(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
    }

}
