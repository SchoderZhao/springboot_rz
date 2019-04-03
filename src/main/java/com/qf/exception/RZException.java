package com.qf.exception;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/29  9:43
 */
public class RZException extends  RuntimeException {

    public RZException(){ }

    public RZException(String msg){
        super(msg);
    }

    public RZException(String msg,Throwable throwable){
        super(msg,throwable);
    }
}
