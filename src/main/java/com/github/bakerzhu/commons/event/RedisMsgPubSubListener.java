package com.github.bakerzhu.commons.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPubSub;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: 发布订阅 - 消息监听统一处理
 * 注意要点：
 * 1、消息可靠性不强，如果在订阅方断线，那么他将会丢失所有在短线期间发布者发布的消息。
 * 2、如果一个客户端订阅了频道，但自己读取消息的速度却不够快的话，那么不断积压的消息会使
 * redis输出缓冲区的体积变得越来越大，这可能使得redis本身的速度变慢，甚至直接崩溃。
 * 3、适合场景：使用较简单，但是需要容忍存在消息丢失的情况。
 *
 *
 * @time: 2018年06月11日
 * @modifytime:
 */
public class RedisMsgPubSubListener extends JedisPubSub {

    private static Logger logger = LoggerFactory.getLogger(RedisMsgPubSubListener.class);

    private List<RedisListener> redisListenerList = new ArrayList<RedisListener>();
    private boolean isListen = false;
    private String channel = null;
    private int messageCount = 0;
    public RedisMsgPubSubListener(String channel) {
        this.channel = channel;
    }

}
