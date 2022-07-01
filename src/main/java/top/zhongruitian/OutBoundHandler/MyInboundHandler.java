package top.zhongruitian.OutBoundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class MyInboundHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg ;
        String s = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);
        System.out.println("Reading in ChannelInboundHandler "+ s);
        ctx.writeAndFlush(s);

    }
}
