/**
 * Copyright 2022 ruitianzhong
 */

import BytesToIntegerDecoder.FrameExtractionHandler;
import BytesToIntegerDecoder.FrameToIntegerHandler;
import EncoderAndDecoder.ToIntegerDecoder;
import EncoderAndDecoder.ToIntegerEncoder2;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/6/30 16:43
 * @description Test for the {@link FrameExtractionHandler }.
 */

public class BytesToIntegerTest {

    @Test
    public void BytesToIntegerBasedDirectlyOnInBoundAdapter() {
        bytesToInteger(new FrameExtractionHandler());
    }



    public void bytesToInteger(ChannelInboundHandlerAdapter channelInboundHandlerAdapter) {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(channelInboundHandlerAdapter);
        byte[] store = new byte[4];
        ByteBuf temp = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            temp.writeInt(i);//must not use temp.write(i);there are some differences.
            //We should mock the ByteBuf
        }
        ByteBuf byteBuf = temp.duplicate();
        assertTrue(embeddedChannel.writeInbound(byteBuf));
        assertTrue(embeddedChannel.finish());
        for (int i = 0; i < 9; i++) {
            byte[] bytes = embeddedChannel.readInbound();
            assertEquals(i, FrameToIntegerHandler.BytesToInteger(bytes));
        }
        assertNull(embeddedChannel.readInbound());
    }
}
