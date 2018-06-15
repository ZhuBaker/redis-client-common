package com.github.bakerzhu.commons.rdclient;

import com.github.bakerzhu.commons.core.RedisClient;
import com.github.bakerzhu.commons.core.RedisClientFactory;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月10日
 * @modifytime:
 */
public class RedisClientStringTest {

    @Test
    public void test1() {
        RedisClient client = RedisClientFactory.getClient();
        System.out.println(client);
    }

    @Test
    public void test2() {
        RedisClient client = RedisClientFactory.getClient(new TestRedisConfig());
        System.out.println(client);
    }

    @Test
    public void test3() {
        RedisClient<Jedis> client = RedisClientFactory.getClient();
        Jedis jedis = client.getJedis();
        System.out.println(jedis);
        jedis.close();
    }

    @Test
    public void test4() {
        TestRedisConfig config = new TestRedisConfig();
        RedisClient<Jedis> client = RedisClientFactory.getClient(config);
        boolean set = client.set("234234", "234234234");
        System.out.println(set);
        Set<String> keys = client.keys("23*");
        for (String key :
                keys) {
            System.out.println(key);
        }
        Long aLong = client.deleteByPattern("23*");
        System.out.println(aLong);
    }


}
