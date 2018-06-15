package com.github.bakerzhu.commons.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description:
 * @time: 2018年06月09日
 * @modifytime:
 */
public class RedisClientException extends ApplicationException {


    private static final long serialVersionUID = 4059472340946891662L;

    public RedisClientException(Throwable t) {
        super(t);
    }

    public RedisClientException(String message) {
        super(message);
    }

}
