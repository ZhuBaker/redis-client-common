package com.github.bakerzhu.commons.constants;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: redis 链接默认配置
 * @time: 2018年06月09日
 * @modifytime:
 */
public class RedisConstants {

    /**
     * redis默认配置 配置文件名
     */
    public static final String DEFAULT_REDIS_FILE_NAME = "redis_config.properties";

    /**
     * redis默认配置 端口
     */
    public static final int DEFAULT_REDIS_PORT = 6379;

    /**
     * redis默认配置 最大链接数
     */
    public static final int DEFAULT_REDIS_POOL_MAX = 10;

    /**
     * redis默认配置 每次连接数增加数
     */
    public static final int DEFAULT_REDIS_POOL_IDLE = 1;

    /**
     * redis默认配置 链接超时时间
     */
    public static final int DEFAULT_REDIS_POOL_TIMEOUT = 10000;

    /**
     * redis默认配置 临时队列名
     */
    public static final String DEFAULT_REDIS_TEMP_QUEUE_TIMEP_NAME = "tmp_queue";
}
