package com.xl.learn.client.handler;

import com.xl.learn.common.msg.SendMsgReqVO;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
@ChannelHandler.Sharable
public class XIMClientHandle extends SimpleChannelInboundHandler<SendMsgReqVO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(XIMClientHandle.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendMsgReqVO msg) throws Exception {
        System.err.println("["+msg.getTo()+"] 收到来自 ["+msg.getFrom()+"] 的消息 ["+msg.getMsg()+"]");
    }

}
