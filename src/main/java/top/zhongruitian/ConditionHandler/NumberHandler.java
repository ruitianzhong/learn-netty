package top.zhongruitian.ConditionHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NumberHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte b = (byte) msg;
        String s = "number:" + (char) b;
        ctx.writeAndFlush(s);
    }
}
