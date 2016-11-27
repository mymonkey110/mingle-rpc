package com.netease.mingle.rpc.shared;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

/**
 * Http Channel Initializer
 * Created by Michael Jiang on 2016/11/27.
 */
public class HttpChannelInitializer extends ChannelInitializer<Channel> {
    private final boolean client;

    public HttpChannelInitializer(boolean client) {
        this.client = client;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        if (client) {
            pipeline.addLast("decoder", new HttpResponseDecoder());
            pipeline.addLast("encoder", new HttpRequestEncoder());
            pipeline.addLast("codec", new HttpClientCodec());
            pipeline.addLast("decompresser", new HttpContentDecompressor());
        } else {
            pipeline.addLast("decoder", new HttpRequestDecoder());
            pipeline.addLast("encoder", new HttpResponseEncoder());
            pipeline.addLast("codec", new HttpServerCodec());
            pipeline.addLast("compresser", new HttpContentCompressor());
        }
    }
}
