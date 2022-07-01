/*
 * Copyright 2022 ruitianzhong
 *
 */
package top.zhongruitian.BytesToIntegerDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/6/30 15:52
 * @description
 */
public class FrameExtractionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf =(ByteBuf) msg;
        while(byteBuf.readableBytes()>=4){
            byte [] bytes = new byte[4];
            byteBuf.readBytes(bytes);//Because of direct buffer,there is no backing Array!
            ctx.fireChannelRead(bytes);

        }
    }
}
