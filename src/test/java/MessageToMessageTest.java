/*
 * Copyright 2022 ruitianzhong
 */

import top.zhongruitian.EncoderAndDecoder.IntegerToStringDecoder;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author ruitianzhong
 * @email zhongruitian2003@qq.com
 * @date 2022/6/30 21:38
 * @description
 */
public class MessageToMessageTest {
    @Test
    public void IntegerToStringTest() {
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new IntegerToStringDecoder());
        for (int i = 0; i < 100; i++) {
            assertTrue(embeddedChannel.writeInbound(Integer.valueOf(i)));

        }
        assertTrue(embeddedChannel.finish());
        for (int i = 0; i < 100; i++) {
            String s = embeddedChannel.readInbound();
            assertTrue(s.equals(Integer.valueOf(i).toString()));
        }

    }
}
