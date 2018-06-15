package com.github.bakerzhu.commons.exception;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: zhubo
 * @description: 异常类信息
 * @time: 2018年06月09日
 * @modifytime:
 */
public class ApplicationException extends RuntimeException {


    private static final long serialVersionUID = -7600542797208975262L;

    protected String code ;

    private String message;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
        this.message = message;
    }

    public ApplicationException(String message, String code) {
        super(code + " : " + message);
        this.code = code;
        this.message = message;
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }


    @Override
    public synchronized Throwable fillInStackTrace() {
        return null;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }


}
