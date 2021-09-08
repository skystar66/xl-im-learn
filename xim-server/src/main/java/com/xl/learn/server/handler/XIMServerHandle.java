package com.xl.learn.server.handler;


import com.xl.learn.common.msg.SendMsgReqVO;
import com.xl.learn.common.utils.Constants;
import com.xl.learn.server.manager.ChannelManager;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
@ChannelHandler.Sharable
public class XIMServerHandle extends SimpleChannelInboundHandler<SendMsgReqVO> {

    private final static Logger LOGGER = LoggerFactory.getLogger(XIMServerHandle.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SendMsgReqVO msg) throws Exception {
        LOGGER.info("received msg=[{}]", msg.toString());

        if (msg.getType() == Constants.CommandType.LOGIN) {
            //保存客户端与 Channel 之间的关系
            ChannelManager.put(msg.getFrom(), ctx.channel());
            LOGGER.info("client [{}] online success!!", msg.getFrom());
            return;
        }
        //获取对方channel
        Channel channel = ChannelManager.get(msg.getTo());
        channel.writeAndFlush(msg).addListeners((ChannelFutureListener) future -> {
            if (!future.isSuccess()) {
                LOGGER.error("消息发送失败！");
                future.channel().close();
            }
        });
    }
}
