import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import top.zhongruitian.ConditionHandler.JudgingHandler;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class ConditionHandlerTest {
    @Test
    public void conditionHandlerTest() {
        EmbeddedChannel channel = new EmbeddedChannel(new JudgingHandler());
        ByteBuf byteBuf = Unpooled.copiedBuffer("abcdefg123456".getBytes(StandardCharsets.UTF_8));
        channel.writeInbound(byteBuf);
        String s = channel.readOutbound();
        assertEquals(s, "character:a");
        s = channel.readOutbound();
        assertEquals(s, "character:b");
    }
}
