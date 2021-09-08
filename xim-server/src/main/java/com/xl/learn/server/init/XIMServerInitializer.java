package com.xl.learn.server.init;

import com.xl.learn.common.serializer.ObjectSerializerDecoder;
import com.xl.learn.common.serializer.ObjectSerializerEncoder;
import com.xl.learn.server.handler.XIMServerHandle;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
public class XIMServerInitializer extends ChannelInitializer<Channel> {

    private final XIMServerHandle xIMServerHandle = new XIMServerHandle();

    @Override
    protected void initChannel(Channel ch) throws Exception {

        ch.pipeline()
                .addLast(new IdleStateHandler(11, 0, 0))
                .addLast(new ObjectSerializerEncoder())
                .addLast(new ObjectSerializerDecoder())
                .addLast(xIMServerHandle);
    }
}
