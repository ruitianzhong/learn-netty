package top.zhongruitian.Http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

public class HttpPipelineInitializer extends ChannelInitializer<Channel> {
    private final boolean client;
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        if(client){
            pipeline.addLast("decoder",new HttpResponseDecoder());
            pipeline.addLast("encoder",new HttpRequestEncoder());
        }else {
            pipeline.addLast("decoder",new HttpServerCodec());
            pipeline.addLast(new HttpObjectAggregator(65535));
            pipeline.addLast("handler",new HttpHandler());
//            pipeline.addLast("encoder",new HttpResponseEncoder());
            HttpResponseEncoder encoder =new HttpResponseEncoder();

        }

    }
    public HttpPipelineInitializer(boolean client)
    {
     this.client=client;
    }

}
