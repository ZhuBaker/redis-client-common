package com.github.bakerzhu.commons.core;

import com.github.bakerzhu.commons.config.RedisConfig;
import com.github.bakerzhu.commons.constants.RedisConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: RedisClient 对象生成工厂
 * @time: 2018年06月09日
 * @modifytime:
 */
public class RedisClientFactory {
    private static final Logger logger = LoggerFactory.getLogger(RedisClientFactory.class);
    private static Map<String , RedisClient> redisClientMap = new ConcurrentHashMap<String , RedisClient>();


    public static RedisClient getClient() {
        return getClient(null);
    }

    public static RedisClient getClient(RedisConfig redisConfig) {
        RedisClient redisClient = null;
        String redisConfigFileName = RedisConstants.DEFAULT_REDIS_FILE_NAME;

        if(null == redisConfig) {
            redisClient = redisClientMap.get(redisConfigFileName);
            if(redisClient == null) {
                redisClient = new JedisRedisClient().setRedisConfig(null);
                redisClientMap.put(redisConfigFileName , redisClient);
            }
            return redisClient;
        }

        // 获取自定义的RedisConfig配置的RedisClient对象，并缓存到redisClientMap对象中
        redisConfigFileName = redisConfig.getConfigFile();
        redisClient = redisClientMap.get(redisConfigFileName);
        if (redisClient == null) {
            redisClient = new JedisRedisClient().setRedisConfig(redisConfig);
            redisClientMap.put(redisConfigFileName,redisClient);
        }

        return redisClient;

    }


}
