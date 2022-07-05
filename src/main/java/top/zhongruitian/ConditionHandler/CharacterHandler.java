package top.zhongruitian.ConditionHandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
/**
@author ruitianzhong
@email zhongruitian2003@qq.com
@date 2022/7/5 16:43
@description 
*/
public class CharacterHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        byte b = (byte) msg;
        String s = "character:" + (char) b;
        ctx.writeAndFlush(s);
    }
}
