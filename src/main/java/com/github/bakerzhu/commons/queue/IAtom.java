package com.github.bakerzhu.commons.queue;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:  具有事务性的队列操作接口类
 * @time: 2018年06月10日
 * @modifytime:
 */
public interface IAtom {

    /**
     * 原子性队列数据获取
     * 与队列取数据时保持事务性操作的原子性方法
     *
     * @param message
     *      队列中取出的数据
     * @return boolean
     *
     */
    boolean run(String message);


}
