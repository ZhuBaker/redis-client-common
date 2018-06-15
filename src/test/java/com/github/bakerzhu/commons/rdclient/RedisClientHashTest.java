package com.github.bakerzhu.commons.rdclient;

import com.github.bakerzhu.commons.core.RedisClient;
import com.github.bakerzhu.commons.core.RedisClientFactory;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月10日
 * @modifytime:
 */
public class RedisClientHashTest {


    @Test
    public void test1() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "root-key";
        Map<String,String> hashMap = new HashMap<String,String>(){
            {
                put("abc","abc");
            }
        };
        int expiredTime = 5 * 60;
        boolean hmset = client.hmset(key, hashMap, expiredTime);
        System.out.println(hmset);

        String hashKey = "hash-key";
        String hashValue = "hash-value";
        client.hset(key,hashKey,hashValue);

    }

    @Test
    public void test2() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "root-key";
        String hashKey = "hash-key";
        String hashValue = "hash-value";
        long hset = client.hset(key, hashKey, hashValue);
        System.out.println(hset);

    }

    @Test
    public void test3() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "root-key";
        Map<String, String> stringStringMap = client.hgetAll(key);
        for(String resultKey : stringStringMap.keySet()) {
            System.out.println(stringStringMap.get(resultKey));
        }

    }





}
