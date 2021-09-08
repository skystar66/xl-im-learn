package com.xl.learn.client.server;


import com.xl.learn.client.init.XIMClientInitializer;
import com.xl.learn.common.msg.SendMsgReqVO;
import com.xl.learn.common.utils.Constants;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: xl
 * @date: 2021/8/5
 **/
public class XIMClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(XIMClient.class);

    private EventLoopGroup group = new NioEventLoopGroup(0, new DefaultThreadFactory("xim-work"));

    private SocketChannel channel;

    public void start(String ip,int port) throws Exception {

        //登录 + 获取可以使用的服务器 ip+port
        //todo 简化掉

        //启动客户端
        startClient(ip, port);
    }

    /**
     * 启动客户端
     *
     * @param
     * @throws Exception
     */
    private void startClient(String ip,int port) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new XIMClientInitializer())
        ;

        ChannelFuture future = null;
        try {
            future = bootstrap.connect(ip, port).sync();
        } catch (Exception e) {
            LOGGER.error("Connect fail!", e);
        }
        if (future.isSuccess()) {
            LOGGER.info("启动 xim client 成功!");
            LOGGER.info("连接到 xim server 成功 {}:{}",ip,port);
        }
        channel = (SocketChannel) future.channel();
    }

    /**
     * 向服务器登录
     */
    public void loginXIMServer(Long userId) {
        SendMsgReqVO login = SendMsgReqVO.builder()
                .from(userId)
                .type(Constants.CommandType.LOGIN)
                .msg("登录")
                .build();
        ChannelFuture future = channel.writeAndFlush(login);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("userId=[{}] Login xim server success!", userId)
        );
    }

    /**
     * 发送消息字符串
     *
     * @param msg
     */
    public void sendStringMsg(SendMsgReqVO msg) {
        ChannelFuture future = channel.writeAndFlush(msg);
        future.addListener((ChannelFutureListener) channelFuture ->
                LOGGER.info("客户端手动发消息成功={}", msg.getMsg()));

    }

    /**
     * 关闭
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        if (channel != null) {
            channel.close();
        }
    }
}
