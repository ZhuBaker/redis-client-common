package com.github.bakerzhu.commons.rdclient;

import com.github.bakerzhu.commons.core.RedisClient;
import com.github.bakerzhu.commons.core.RedisClientFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月11日
 * @modifytime:
 */
public class RedisClientNxTest {

    private static Logger logger = LoggerFactory.getLogger(RedisClientNxTest.class);


    @Test
    public void test1() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        Jedis jedis = client.getJedis();
        Long setnx = jedis.setnx("aaaa", "aaaa");
        System.out.println(setnx);
    }

    @Test
    public void test2() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        Jedis jedis = client.getJedis();
        Long setnx = jedis.setnx("aaaa", "aaaa"); // 成功返回1 失败返回0
        Long setnx2 = jedis.setnx("bbbb", "bbbb");// 成功返回1 失败返回0
        System.out.println(setnx + "" + setnx2);

        Long msetnx = jedis.msetnx("aaaa", "aaaa", "bbbbd", "bbbb");   // 成功都成功 ， 失败都失败 成功返回1 失败返回0
        System.out.println(msetnx);
        jedis.close();
    }

    @Test
    public void test3() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "list-key";
        List<String> lrange1 = client.lrange(key, 0, 0);
        System.out.println(lrange1);

    }

    @Test
    public void test4() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        Jedis jedis = client.getJedis();
        String key = "qqq";
        Long incr = jedis.incr(key); // 返回自增后的值
        System.out.println(incr);


    }


}
