/*
 * Copyright 2022 ruitianzhong
 */
package OutBoundHandler;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class MyOutboundHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        String s= "header "+(String)msg ;
        System.out.println("Adding the header in outbound handler");
       super.write(ctx, s, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {

        super.flush(ctx);
    }
}
