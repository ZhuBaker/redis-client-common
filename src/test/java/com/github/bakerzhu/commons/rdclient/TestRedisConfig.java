package com.github.bakerzhu.commons.rdclient;

import com.github.bakerzhu.commons.config.RedisConfig;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月10日
 * @modifytime:
 */
public class TestRedisConfig extends RedisConfig{

    private static final String FILE_NAME = "test_redis_config.properties";

    @Override
    public String getConfigFile() {
        return FILE_NAME;
    }
}
