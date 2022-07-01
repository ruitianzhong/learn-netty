package Channel;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Random;

public class FirstHandler extends ChannelInboundHandlerAdapter {
    private Random random = new Random();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Channel Active in Fist Handler!");
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("Have the Array: " + byteBuf.hasArray());
        toSeeHowByteBufWorks(byteBuf);
        String original = byteBuf.toString(CharsetUtil.UTF_8);
        String output = "Header from first handler: " + original;
        ctx.fireChannelRead(output);
    }

    private void toSeeHowByteBufWorks(ByteBuf byteBuf) {

        int offset = byteBuf.arrayOffset() + byteBuf.readerIndex();
        int length = byteBuf.readableBytes();
        System.out.println("offset:" + offset + " length: " + length);
        byte b = byteBuf.getByte(offset);
        System.out.println("first byte:" + (char) b);
        for (int i = offset; i < length; i++) {
            System.out.println((char) (byteBuf.getByte(i)));
        }
        byteBuf.writeBytes("++writeBytes".getBytes(CharsetUtil.UTF_8));
        /**
         * while (byteBuf.writableBytes() >= 4) {
         byteBuf.writeInt(random.nextInt());
         }//Just to show how to use the API of ByteBuf...
         */
        assert byteBuf.readerIndex()==byteBuf.writerIndex();
    }
}
