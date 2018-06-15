package com.github.bakerzhu.commons.core;

import com.github.bakerzhu.commons.config.RedisConfig;
import com.github.bakerzhu.commons.queue.IAtom;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: Redis操作类接口定义
 * @time: 2018年06月09日
 * @modifytime:
 */
public interface RedisClient<T> {

    /**
     * <b>setRedisConfig</b> <br/>
     *
     * 设置RedisConfig配置  <br/>
     *
     * @param redisConfig
     * @return
     */
    RedisClient<?> setRedisConfig(RedisConfig redisConfig);


    /**
     * <b>getJedis</b> <br/>
     *
     * 获取redis的操作对象 <br/>
     *
     * @return
     */
    T getJedis();


    /**
     *
     * @param pattern
     * @return
     */
    Set<String> keys(String pattern);


    /**
     * 按模式删除多个Key
     * @param patternKey
     * @return
     */
    Long deleteByPattern(String patternKey);


    /**
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(String key , String value) ;


    /**
     *
     * @param key 缓存 KEY
     * @param value 缓存 VALUE
     * @param expiredSeconds  多长时间(秒)过期
     * @return
     */
    boolean set(String key , String value , int expiredSeconds);

    /**
     * String 字符串读取操作
     *
     * @param key
     * @return
     */
    String get(String key);


    /**
     * Hash 写操作
     *
     * @param key
     * @param hashMap
     * @return boolean
     */
    boolean hmset(String key , Map<String , String > hashMap);


    /**
     *
     * @param key
     * @param hashMap
     * @param expiredSeconds
     * @return
     */
    boolean hmset(String key , Map<String,String> hashMap , int expiredSeconds);


    /**
     * Hash读操作
     *
     * @param key
     * @return
     */
    Map<String , String> hgetAll(String key);


    /**
     * Hash写操作
     *
     * @param key
     * @param hashKey
     * @param hashValue
     * @return f the field already exists, and the HSET just produced an update of the value, 0 is
     *         returned, otherwise if a new field is created 1 is returned.
     */
    long hset(String key , String hashKey , String hashValue);


    /**
     * Hash 读操作
     *
     * If key holds a hash, retrieve the value associated to the specified field.
     *
     * If the field is not found or the key does not exist, a special 'nil' value is returned. <br/>
     *
     * @param key
     * @param hashKey
     * @return
     */
    String hget(String key , String hashKey);



    /**
     *
     * <b>List写操作</b> <br/>
     * <br/>
     *
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param values
     * @return boolean
     *
     */
    boolean lpush(String key, String... values);

    /**
     *
     * <b>List写操作（先进先出，可用于队列）</b> <br/>
     * <br/>
     *
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param expiredSeconds
     * @param values
     * @return boolean
     *
     */
    boolean lpush(String key, int expiredSeconds, String... values);

    /**
     *
     * <b>List写操作</b> <br/>
     * <br/>
     *
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param values
     * @return boolean
     *
     */
    boolean rpush(String key, String... values);

    /**
     *
     * <b>List写操作</b> <br/>
     * <br/>
     *
     * Add the string value to the head (LPUSH) or tail (RPUSH) of the list stored at key. If the
     * key does not exist an empty list is created just before the append operation. If the key
     * exists but is not a List an error is returned. <br/>
     *
     * @param key
     * @param expiredSeconds
     * @param values
     * @return boolean
     *
     */
    boolean rpush(String key, int expiredSeconds, String... values);

    /**
     *
     * <b>List读操作（先进后出读取-类似栈）</b> <br/>
     * <br/>
     *
     * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
     * if the list contains the elements "a","b","c" LPOP will return "a" and the list will become
     * "b","c".<br/>
     * <br/>
     *
     * If the key does not exist or the list is already empty the special value 'nil' is returned.
     *
     * @param key
     * @return String
     *
     */
    String lpop(String key);

    /**
     *
     * <b>List读操作（先进先出读取-类似队列）</b> <br/>
     * <br/>
     *
     * Atomically return and remove the first (LPOP) or last (RPOP) element of the list. For example
     * if the list contains the elements "a","b","c" RPOP will return "c" and the list will become
     * "a","b". <br/>
     * <br/>
     *
     * If the key does not exist or the list is already empty the special value 'nil' is returned.
     * <br/>
     *
     * @param key
     * @return String
     *
     */
    String rpop(String key);


    /**
     * List 操作 保证绝对的事务性
     *
     *  先进先出 OR 先进后出 取决于数据打入时采用的是lpush OR rpush <br/>
     *
     * @param key
     * @param atom
     * @return string
     */
    String popQueue(String key, IAtom atom);

    /**
     * List读操作
     *
     * Return the specified elements of the list stored at the specified key. Start and end are
     * zero-based indexes. 0 is the first element of the list (the list head), 1 the next element
     * and so on.
     *
     * For example LRANGE foobar 0 2 will return the first three elements of the list.
     *
     * start and end can also be negative numbers indicating offsets from the end of the list. For
     * example -1 is the last element of the list, -2 the penultimate element and so on. <br/>
     *
     *
     * @param key
     * @param start
     * @param end
     * @return List<String>
     */
    List<String> lrange(String key , long start , long end);

    /**
     * setnx
     *
     * SETNX works exactly like SET with the only difference that if the key already exists no
     * operation is performed. SETNX actually means "SET if Not eXists". <br/>
     *
     * @param key
     * @param value
     * @param expiredSeconds
     * @return
     */
    long setnx(String key , String value , int expiredSeconds);


    /**
     * incr
     *
     * Increment the number stored at key by one. If the key does not exist or contains a value of a
     * wrong type, set the key to the value of "0" before to perform the increment operation.
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key after the
     *         increment.
     */
    long incr(String key);

    /**
     * decr
     *
     * Decrement the number stored at key by one. If the key does not exist or contains a value of a
     * wrong type, set the key to the value of "0" before to perform the decrement operation.
     *
     * @param key
     * @return Integer reply, this commands will reply with the new value of key after the
     *         increment.
     */
    long decr(String key);



}
