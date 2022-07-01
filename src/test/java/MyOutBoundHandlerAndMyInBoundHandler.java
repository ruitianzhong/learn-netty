import OutBoundHandler.MyInboundHandler;
import OutBoundHandler.MyOutboundHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MyOutBoundHandlerAndMyInBoundHandler {
    public static int count =10;
    @Test
    public void MyOutBoundHandlerAndMyInBoundHandlerTest() {
        for (int i = 0; i < count; i++) {
            EmbeddedChannel embeddedChannel = new EmbeddedChannel(new MyInboundHandler(), new MyOutboundHandler());
            String uuid = UUID.randomUUID().toString();
            ByteBuf byteBuf = Unpooled.buffer();
            byteBuf.writeBytes(uuid.getBytes());
//         assertTrue(embeddedChannel.writeInbound(byteBuf));
            ByteBuf temp = byteBuf.duplicate();
            embeddedChannel.writeInbound(temp);
            String s = embeddedChannel.readOutbound();
            assertTrue(uuid.equals(s));   //Note the embeddedChannel.finish()
            embeddedChannel.writeOutbound(s);
            String result = "header " + uuid;
            assertEquals(result, embeddedChannel.readOutbound());
        }
    }
}
