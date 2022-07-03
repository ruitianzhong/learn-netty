package top.zhongruitian.EcoServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String s;
        int i = 5;
        {
            System.out.println("Please enter the word!");
            Scanner scanner = new Scanner(System.in);
            s = scanner.nextLine();
            ctx.writeAndFlush(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("echo: " + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("Please enter the word!");
        String s;
        Scanner scanner = new Scanner(System.in);
        s = scanner.nextLine();
        ctx.writeAndFlush(Unpooled.copiedBuffer(s, CharsetUtil.UTF_8));
    }
}
