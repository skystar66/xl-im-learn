package com.xl.learn.client.init;

import com.xl.learn.client.handler.XIMClientHandle;
import com.xl.learn.common.serializer.ObjectSerializerDecoder;
import com.xl.learn.common.serializer.ObjectSerializerEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
public class XIMClientInitializer extends ChannelInitializer<Channel> {

    private final XIMClientHandle ximClientHandle = new XIMClientHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline()
                .addLast(new ObjectSerializerEncoder())
                .addLast(new ObjectSerializerDecoder())
                .addLast(ximClientHandle);
    }
}
