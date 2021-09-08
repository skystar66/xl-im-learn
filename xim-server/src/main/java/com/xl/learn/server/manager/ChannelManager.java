package com.xl.learn.server.manager;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelManager {

    private static final Map<Long, Channel> CHANNEL_MAP = new ConcurrentHashMap<>(16);


    public static void put(Long id, Channel socketChannel) {
        CHANNEL_MAP.put(id, socketChannel);
    }

    public static Channel get(Long id) {
        return CHANNEL_MAP.get(id);
    }


}
