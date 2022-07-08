package top.zhongruitian.ConditionHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class JudgingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        while (byteBuf.readableBytes() >= 1) {
            byte b;
            b = byteBuf.readByte();
            if (b >= 'a' && b <= 'z') {
                ctx.pipeline().addLast("character", new CharacterHandler());
                ctx.fireChannelRead(b);
                ctx.pipeline().remove("character");
            } else if (b >= '0' && b <= '9') {
                ctx.pipeline().addLast("number", new NumberHandler());
                ctx.fireChannelRead(b);
                ctx.pipeline().remove("number");
            }
        }
    }
}
