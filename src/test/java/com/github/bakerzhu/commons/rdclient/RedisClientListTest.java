package com.github.bakerzhu.commons.rdclient;

import com.github.bakerzhu.commons.core.RedisClient;
import com.github.bakerzhu.commons.core.RedisClientFactory;
import com.github.bakerzhu.commons.queue.IAtom;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月10日
 * @modifytime:
 */
public class RedisClientListTest {

    private static Logger logger = LoggerFactory.getLogger(RedisClientListTest.class);


    @Test
    public void test1() {
        RedisClient client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "list-key";
        String value = "list-value";
        int expireSeconds = 5 * 60;
        Long aLong = client.deleteByPattern("*");
        System.out.println(aLong);
        for (int i = 1 ; i <= 50 ; i++) {
            client.lpush(key,expireSeconds,value + i);
        }
        System.out.println(client.lpop(key));
        System.out.println(client.rpop(key));
    }

    @Test
    public void test11() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "list-key";
        Jedis jedis = client.getJedis();
        Long rpush1 = jedis.rpush(key, "a", "b", "c", "d", "e");  // 返回结果值是 添加元素后 队列的总长度
        System.out.println(rpush1);


        /*RedisClient client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "list-key";
        boolean rpush = client.rpush(key, "a", "b", "c", "d", "e");
        System.out.println(rpush);*/
    }

    @Test
    public void test22() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        Jedis jedis = client.getJedis();
        Transaction ts = jedis.multi();
        ts.set("sss","ddd");
        List<Response<?>> responses = ts.execGetResponse();
        responses.get(0);
    }

    @Test
    public void test2() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        String key = "list-key";
        Jedis jedis = client.getJedis();
        Transaction ts = jedis.multi();
        ts.rpoplpush(key,key);
        // 返货操作的List中的元素（从List右边 取出的元素）
        String result = (String)ts.execGetResponse().get(0).get();
        System.out.println(result);
    }


    @Test
    public void test3() {
        RedisClient<Jedis> client = RedisClientFactory.getClient(new TestRedisConfig());
        client.popQueue("", new IAtom() {
            @Override
            public boolean run(String message) {
                logger.info("取出队列信息,VALUE = [" + message + "]");
                return true;
            }
        });
    }



}
