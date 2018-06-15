package com.github.bakerzhu.commons.core;

import com.github.bakerzhu.commons.config.RedisConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: JedisPool 容器类 类 ,仅仅对外提供如下两个方法
 * {@link #getClient()}  从默认redis链接池中取出 Jedis 信息
 * {@link #getClient(RedisConfig)} 根据config信息取出相关的JedisPool 中 Jedis信息
 *
 * @time: 2018年06月09日
 * @modifytime:
 */
public class JedisPoolFactory {

    private static Logger logger = LoggerFactory.getLogger(JedisPoolFactory.class);
    private static RedisConfig configDefault = new RedisConfig();
    private static Map<String,JedisPool> mapConfigPool = new ConcurrentHashMap<String , JedisPool>();

    private JedisPoolFactory(){
    }

    /**
     * 根据 Key 获取链接池信息
     *
     * @param key fileName 信息
     * @return
     */
    private static JedisPool getConfigPool(String key){
        JedisPool pool = mapConfigPool.get(key);
        return pool;
    }

    /**
     * 根据 RedisConfig 配置信息来初始化 JedisPool(redis链接池) 并存入{@link #mapConfigPool} 对象
     * @param config
     * @return
     */
    private synchronized static boolean initConfigPool(RedisConfig config) {
        if(config == null) {
            return false;
        }
        String key = config.getConfigFile();
        JedisPool jedisPool = getConfigPool(key);
        if(jedisPool != null){
            return true;
        }
        String server = config.getServerIP();
        int port = config.getServerPort();
        String auth = config.getServerAuth();
        boolean isAuth = false;
        if(StringUtils.isNotBlank(auth)) {
            isAuth = true;
        }
        logger.warn("The redis server : " + server + " : " + port);
        if (StringUtils.isBlank(server)) {
            return false;
        }
        // 池基本配置
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(config.getPoolMax());
        poolConfig.setMaxIdle(config.getPoolIdle());
        poolConfig.setMaxWaitMillis(config.getTryTimeout());
        poolConfig.setTestOnBorrow(config.getTestOnBorrwo());
        int timeout = config.getTryTimeout();// msec

        if(isAuth){
            jedisPool = new JedisPool(poolConfig,server,port,timeout,auth);
        } else {
            jedisPool = new JedisPool(poolConfig,server,port,timeout);
        }
        if (jedisPool != null) {
            mapConfigPool.put(key , jedisPool);
        }
        return jedisPool != null;
    }

    /**
     * 根据默认的链接池获取Jedis链接对象
     *
     * @return
     */
    public static Jedis getClient() {
        return getClient(configDefault);
    }

    /**
     * 根据 config 对象，获取对应对象的 Jedis 对象
     * 1.不存在就加载
     * 2.存在就返回
     * 3.加载失败就返回null
     *
     * @param config
     * @return
     */
    public static Jedis getClient(RedisConfig config) {
        if(config == null) {
            return null;
        }
        String key = config.getConfigFile();
        JedisPool jedisPool = getConfigPool(key);
        if(jedisPool == null) {
            initConfigPool(config);
            jedisPool = getConfigPool(key);
        }
        if(jedisPool == null) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        }catch (Exception e) {
            logger.warn("获取指定配置的redis链接失败",e);
        }
        if(jedis == null){
            logger.warn("Cannot get Jedis object from the pool !" );
        }
        return jedis;
    }



}
