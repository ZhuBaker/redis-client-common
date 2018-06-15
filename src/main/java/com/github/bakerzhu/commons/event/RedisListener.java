package com.github.bakerzhu.commons.event;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: 自定义的发布订阅接口类
 * @time: 2018年06月11日
 * @modifytime:
 */
public interface RedisListener {

    /**
     * onMessage
     * 订阅消息
     *
     * @param channel
     * @param message
     */
    void onMessage(String channel, String message);


}
