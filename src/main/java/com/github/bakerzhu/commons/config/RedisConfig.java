package com.github.bakerzhu.commons.config;


import com.github.bakerzhu.commons.constants.RedisConstants;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 *           RedisClient 默认配置信息类 , 当调用者没有指定自定义的RedisConfig类时,则会加载使用该默认配置
 *           当有其他配置只需要重写 {@link #getConfigFile()} 方法 指定配置文件
 * @time: 2018年06月09日
 * @modifytime:
 */
public class RedisConfig extends AbstractConfig {

    private final String    FILE_NAME = RedisConstants.DEFAULT_REDIS_FILE_NAME;

    private final String    CONFIG_NAME_SERVER_IP          =       "redis.server.ip";
    private final String    CONFIG_NAME_SERVER_PORT        =       "redis.server.port";
    private final String    CONFIG_NAME_SERVER_PWD         =       "redis.server.password";

    private final String    CONFIG_NAME_POOL_MAX           =       "redis.client.pool.max";
    private final String    CONFIG_NAME_POOL_IDLE          =       "redis.client.pool.idle";
    private final String    CONFIG_NAME_TESTONBORROW       =       "redis.client.pool.TestOnBorrwo";
    private final String    CONFIG_NAME_TRY_TIMEOUT        =       "redis.client.pool.try.timeout";

    public RedisConfig() {
        reloadConfig();
    }

    @Override
    public String getConfigFile() {
        return FILE_NAME;
    }

    public String getServerIP() {
        return getProperty(CONFIG_NAME_SERVER_IP);
    }

    public int getServerPort() {
        return getPropertyToInt(CONFIG_NAME_SERVER_PORT,RedisConstants.DEFAULT_REDIS_PORT);
    }

    public String getServerAuth() {
        return getProperty(CONFIG_NAME_SERVER_PWD);
    }

    public int getPoolMax() {
        return getPropertyToInt(CONFIG_NAME_POOL_MAX,RedisConstants.DEFAULT_REDIS_POOL_MAX);
    }

    public int getPoolIdle() {
        return getPropertyToInt(CONFIG_NAME_POOL_IDLE,RedisConstants.DEFAULT_REDIS_POOL_IDLE);
    }

    public boolean getTestOnBorrwo() {
        String temp = getProperty(CONFIG_NAME_TESTONBORROW);
        return "true".equalsIgnoreCase(temp);
    }

    public int getTryTimeout() {
        return getPropertyToInt(CONFIG_NAME_TRY_TIMEOUT,RedisConstants.DEFAULT_REDIS_POOL_TIMEOUT);
    }

}
