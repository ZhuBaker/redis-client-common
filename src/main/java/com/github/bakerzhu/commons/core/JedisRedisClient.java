package com.github.bakerzhu.commons.core;

import com.github.bakerzhu.commons.config.RedisConfig;
import com.github.bakerzhu.commons.constants.RedisConstants;
import com.github.bakerzhu.commons.exception.AssertHelper;
import com.github.bakerzhu.commons.exception.RedisClientException;
import com.github.bakerzhu.commons.queue.IAtom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月09日
 * @modifytime:
 */
public class JedisRedisClient implements RedisClient<Jedis> {

    private static Logger           logger                    = LoggerFactory.getLogger(JedisRedisClient.class);
    private RedisConfig             redisConfig               = null;

    /**
     * <b>setRedisConfig</b> <br/>
     * <p>
     * 设置RedisConfig配置  <br/>
     *
     * @param redisConfig
     * @return
     */
    @Override
    public RedisClient<Jedis> setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
        return this;
    }

    /**
     * <b>getJedis</b> <br/>
     * <p>
     * 获取redis的操作对象 <br/>
     *
     * @return
     */
    @Override
    public Jedis getJedis() {
        if(null == redisConfig) {
            return JedisPoolFactory.getClient();
        }
        return JedisPoolFactory.getClient(redisConfig);
    }


    /**
     * @param pattern
     * @return
     */
    @Override
    public Set<String> keys(String pattern) {
        AssertHelper.notBlank(pattern,"The patternKey is Not allow blank.");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis,"The Jedis Object is Not allow null .");
        Set<String> set = null;
        try{
            set = jedis.keys(pattern);
        } catch (Exception e){
            logger.warn("Read redis in error:" + e);
            throw new RedisClientException(e);
        } finally {
            release(jedis);
        }
        return set;
    }

    /**
     * 按模式删除多个Key
     *
     * @param patternKey
     * @return
     */
    @Override
    public Long deleteByPattern(String patternKey) {
        Long count = 0L;
        AssertHelper.notBlank(patternKey,"The patternKey is Not allow blank.");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis,"The Jedis Object is Not allow null .");
        try {
            Set<String> keys = jedis.keys(patternKey);
            if (keys != null) {
                for (String key : keys) {
                    count += jedis.del(key);
                }
            }
        }catch (Exception e) {
            logger.warn("Delete redis pattern keys in error:" + e);
            throw new RedisClientException(e);
        }finally {
            release(jedis);
        }
        return count;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(String key, String value) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis,"The Jedis Object is Not allow null .");
        try {
            key = jedis.set(key, value);
            return "OK".equals(key);
        }catch (Exception e){
            logger.error("Write String Value To Redis Error:" + e);
            throw new RedisClientException(e);
        }finally {
            release(jedis);
        }
    }

    /**
     * @param key            缓存 KEY
     * @param value          缓存 VALUE
     * @param expiredSeconds 多长时间(秒)过期
     * @return
     */
    @Override
    public boolean set(String key, String value, int expiredSeconds) {
        AssertHelper.isTrue(expiredSeconds > 0 , "The expiredSeconds is not less then 0 .");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis,"The Jedis Object is Not allow null .");
        try {
            Transaction ts = jedis.multi();
            ts.set(key,value);
            ts.expire(key,expiredSeconds);
            ts.exec();
            return true;
        }catch (Exception e){
            logger.error("Write String Value To Redis Error:" + e);
            throw new RedisClientException(e);
        }finally {
            release(jedis);
        }
    }

    /**
     * String 字符串读取操作
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis,"The Jedis Object is Not allow null .");
        String value = null;
        try {
            value = jedis.get(key);
        } catch (Exception e){
            logger.error("Get String Value To Redis Error:" + e);
            throw new RedisClientException(e);
        }finally {
            release(jedis);
        }
        return value;

    }

    /**
     * Hash 写操作
     *
     * @param key
     * @param hashMap
     * @return boolean
     */
    @Override
    public boolean hmset(String key, Map<String, String> hashMap) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            key = jedis.hmset(key, hashMap);
            return "OK".equalsIgnoreCase(key);
        }
        catch (Exception e) {
            logger.error("Execute hmset in Redis Error:" + e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * @param key
     * @param hashMap
     * @param expiredSeconds
     * @return
     */
    @Override
    public boolean hmset(String key, Map<String, String> hashMap, int expiredSeconds) {
        AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
        try {
            Transaction ts = jedis.multi();
            ts.hmset(key, hashMap);
            ts.expire(key, expiredSeconds);
            key = (String) ts.execGetResponse().get(0).get();// 提交事务并返回"ts.hmset(key, hashMap)"的执行结果
            return "OK".equalsIgnoreCase(key);
        }
        catch (Exception e) {
            logger.error("Execute hmset in Redis Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * Hash读操作
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.hgetAll(key);
        }
        catch (Exception e) {
            logger.error("Get all value from hash Error:" + e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * Hash写操作
     *
     * @param key
     * @param hashKey
     * @param hashValue
     * @return f the field already exists, and the HSET just produced an update of the value, 0 is
     * returned, otherwise if a new field is created 1 is returned.
     */
    @Override
    public long hset(String key, String hashKey, String hashValue) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            long result = jedis.hset(key, hashKey, hashValue);
            return result;
        }
        catch (Exception e) {
            logger.error("Write Hash Value To Redis Error:" + e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * Hash 读操作
     * <p>
     * If key holds a hash, retrieve the value associated to the specified field.
     * <p>
     * If the field is not found or the key does not exist, a special 'nil' value is returned. <br/>
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public String hget(String key, String hashKey) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.hget(key, hashKey);
        }
        catch (Exception e) {
            logger.error("Get value from Hash Error:" + e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }


    /**
     * <b>List写操作</b> <br/>
     * <br/>
     * <p>
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param values
     * @return Integer reply, specifically, the number of elements inside the list after the push
     *         operation.
     */
    @Override
    public boolean lpush(String key, String... values) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            long result = jedis.lpush(key, values);
            return result > 0;
        }
        catch (Exception e) {
            logger.error("Write List Value To Redis Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * <b>List写操作（先进先出，可用于队列）</b> <br/>
     * <br/>
     * <p>
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param expiredSeconds
     * @param values
     * @return boolean
     */
    @Override
    public boolean lpush(String key, int expiredSeconds, String... values) {
        AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
        try {
            Transaction ts = jedis.multi();
            ts.lpush(key, values);
            ts.expire(key, expiredSeconds);
            long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));// 提交事务并返回"ts.setnx(key,value)"的执行结果
            return result > 0;
        }
        catch (Exception e) {
            logger.error("Write List Value To Redis Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * <b>List写操作</b> <br/>
     * <br/>
     * <p>
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param values
     * @return boolean
     */
    @Override
    public boolean rpush(String key, String... values) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            // 返回值为 添加元素后 队列中元素的总长度
            long result = jedis.rpush(key, values);
            return result > 0;
        }
        catch (Exception e) {
            logger.error("Write List Value To Redis Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * <b>List写操作</b> <br/>
     * <br/>
     * <p>
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param expiredSeconds
     * @param values
     * @return boolean
     */
    @Override
    public boolean rpush(String key, int expiredSeconds, String... values) {
        AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");
        try {
            Transaction ts = jedis.multi();
            ts.rpush(key, values);
            ts.expire(key, expiredSeconds);
            long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));
            return result > 0;
        } catch (Exception e) {
            logger.error("Write List Value To Redis Error:", e);
            throw new RedisClientException(e);
        } finally {
            release(jedis);
        }
    }

    /**
     * <b>List读操作（先进后出读取-类似栈）</b> <br/>
     * <br/>
     * <p>
     * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
     * if the list contains the elements "a","b","c" LPOP will return "a" and the list will become
     * "b","c".<br/>
     * <br/>
     * <p>
     * If the key does not exist or the list is already empty the special value 'nil' is returned.
     *
     * @param key
     * @return String
     */
    @Override
    public String lpop(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.lpop(key);
        }
        catch (Exception e) {
            logger.error("Lpop value from list Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * <b>List读操作（先进先出读取-类似队列）</b> <br/>
     * <br/>
     * <p>
     * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
     * if the list contains the elements "a","b","c" RPOP will return "c" and the list will become
     * "a","b". <br/>
     * <br/>
     * <p>
     * If the key does not exist or the list is already empty the special value 'nil' is returned.
     * <br/>
     *
     * @param key
     * @return String
     */
    @Override
    public String rpop(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.rpop(key);
        }
        catch (Exception e) {
            logger.error("Rpop value from list Error:", e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }


    /**
     * List 操作 保证绝对的事务性
     * <p>
     * 先进先出 OR 先进后出 取决于数据打入时采用的是lpush OR rpush <br/>
     *
     * @param key
     * @param atom
     * @return string
     */
    @Override
    public String popQueue(String key, IAtom atom) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");

        String dstkey = RedisConstants.DEFAULT_REDIS_TEMP_QUEUE_TIMEP_NAME + "-" + key;
        try {
            // 事务1，从临时队列中消费数据并继续存回队列中
            Transaction ts = jedis.multi();
            ts.rpoplpush(dstkey,dstkey);
            String result = (String)ts.execGetResponse().get(0).get();

            // 事务2，从临时队列中消费数据
            ts = jedis.multi();
            ts.rpop(dstkey);
            if (atom.run(result)) {
                ts.exec();
            }
            // 事务3，当临时队列为空时，从目标队列取出数据到临时队列中 供以下使用
            if (result == null) {
                ts = jedis.multi();
                ts.rpoplpush(key,dstkey);
                result = (String) ts.execGetResponse().get(0).get();
            }
            return result;
        } catch (Exception e) {
            logger.error("Execute lpopQueue in Redis Error:",e);
            throw new RedisClientException(e);
        } finally {
            release(jedis);
        }
    }


    /**
     * List读操作
     * <p>
     * Return the specified elements of the list stored at the specified key. Start and end are
     * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element
     * and so on.
     * <p>
     * For example LRANGE foobar 0 2 will return the first three elements of the list.
     * <p>
     * start and end can also be negative numbers indicating offsets from the end of the list. For
     * example -1 is the last element of the list, -2 the penultimate element and so on. <br/>
     *
     * @param key
     * @param start
     * @param end
     * @return List<String>
     */
    @Override
    public List<String> lrange(String key, long start, long end) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.lrange(key, start, end);
        }catch (Exception e) {
            logger.error("Get range value from list Error:" + e);
            throw new RedisClientException(e);
        }finally {
            release(jedis);
        }
    }

    /**
     * setnx
     * <p>
     * SETNX works exactly like SET with the only difference that if the key already exists no
     * operation is performed. SETNX actually means "SET if Not eXists". <br/>
     *
     * @param key
     * @param value
     * @param expiredSeconds
     * @return
     */
    @Override
    public long setnx(String key, String value, int expiredSeconds) {
        AssertHelper.isTrue(expiredSeconds > 0, "The expiredSeconds is not less then 0 .");
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not allow null .");

        try {
            int oldExporeSeconds = Integer.parseInt(jedis.ttl(key).toString());
            Transaction ts = jedis.multi();
            ts.setnx(key, value);
            ts.expire(key,oldExporeSeconds > 0 ? oldExporeSeconds : expiredSeconds);
            long result = Long.parseLong(String.valueOf(ts.execGetResponse().get(0).get()));  // 提交事务并返回"ts.setnx(key,value)"的执行结果
            return result;
        }catch (Exception e) {
            logger.error("Execute setnx in Redis Error:", e);
            throw new RedisClientException(e);
        } finally {
            release(jedis);
        }
    }


    /**
     * incr
     * <p>
     * Increment the number stored at key by one. If the key does not exist or contains a value of a
     * wrong type, set the key to the value of "0" before to perform the increment operation.
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key after the
     * increment.
     */
    @Override
    public long incr(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.incr(key);
        }
        catch (Exception e) {
            logger.error("The Redis incr {} Error:", key, e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * decr
     * <p>
     * Decrement the number stored at key by one. If the key does not exist or contains a value of a
     * wrong type, set the key to the value of "0" before to perform the decrement operation.
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key after the
     * increment.
     */
    @Override
    public long decr(String key) {
        Jedis jedis = getJedis();
        AssertHelper.notNull(jedis, "The Jedis Object is Not Null .");
        try {
            return jedis.decr(key);
        }
        catch (Exception e) {
            logger.error("The Redis decr {} Error:", key, e);
            throw new RedisClientException(e);
        }
        finally {
            release(jedis);
        }
    }

    /**
     * 释放 Jedis 资源
     *
     * @param jedis
     */
    protected void release(Jedis jedis) {
        if (jedis == null) {
            return;
        }
        try {
            jedis.close();
        }
        catch (Exception e) {
            logger.error("Release jedis Error: ", e);
            throw new RedisClientException(e);
        }
    }


}
