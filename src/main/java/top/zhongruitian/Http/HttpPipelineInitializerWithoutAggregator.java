/*
 * Copyright 2022 ruitianzhong
 */
package top.zhongruitian.Http;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
@author ruitianzhong
@email zhongruitian2003@qq.com
@date 2022/7/1 8:24
@description
*/

public class HttpPipelineInitializerWithoutAggregator extends ChannelInitializer<Channel> {
    /**
     * @param ch the {@link Channel} which was registered.
     * @throws Exception
     */
    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline channelPipeline = ch.pipeline();
        channelPipeline.addLast(new HttpRequestDecoder(),new HttpHandlerWithoutAggregator(),
                new HttpResponseEncoder());

    }
}
