/*
 * Copyright 2022 ruitianzhong
 */
import top.zhongruitian.EncoderAndDecoder.ToIntegerDecoder;
import top.zhongruitian.EncoderAndDecoder.ToIntegerEncoder2;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
/**
@author ruitianzhong
@email zhongruitian2003@qq.com
@date 2022/6/30 21:27
@description 
*/

import static org.junit.Assert.assertTrue;

public class ByteToMessageTest {
    @Test
    public void NormalIntDecoderTest() {
        bytesToInteger(new ToIntegerDecoder());
    }

    @Test
    public void ReplayingToIntDecoderTest() {
        bytesToInteger(new ToIntegerEncoder2());
    }

    public void bytesToInteger(ChannelInboundHandlerAdapter adapter) {
        EmbeddedChannel channel = new EmbeddedChannel(adapter);
        ByteBuf byteBuf = Unpooled.buffer();
        for (int i = 0; i < 100; i++) {
            byteBuf.writeInt(i);
        }
        ByteBuf temp = byteBuf.duplicate();
        assertTrue(channel.writeInbound(temp));
        assertTrue(channel.finish());//finish method has side effect!

        for (int i = 0; i < 100; i++) {
            Integer integer = channel.readInbound();
            assertTrue(i == integer.intValue());
        }
    }
}
