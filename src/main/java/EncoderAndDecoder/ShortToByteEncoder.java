/*
 * Copyright 2022 ruitianzhong
 */
package EncoderAndDecoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
/**
@author ruitianzhong
@email zhongruitian2003@qq.com
@date 2022/6/30 22:41
@description 
*/

public class ShortToByteEncoder extends MessageToByteEncoder<Short> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Short msg, ByteBuf out) throws Exception {
        out.writeShort(msg);
    }
}
