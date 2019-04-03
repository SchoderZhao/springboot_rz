package com.qf.utils;

import java.util.HashMap;

public class R extends HashMap<String,Object> {

    private  int code;//0 操作成功  1 操作失败
    private String msg;
    //private Object data;

    public R(){

    }

    public R(int code){
        this.put("code",code);
    }

    public R(int code, String msg){
        super.put("code",code);
        super.put("msg",msg);
    }

    public  static R ok(){
        return new R(0);
    }

    public  static R ok(String msg){
        return new R(0,msg);
    }

    public  static R error(){
        return new R(1);
    }
    public  static R error(String msg){
        return new R(1,msg);
    }


    public R put(String key,Object o){
        super.put(key,o);//java.lang.StackOverflowError]
        return this;
    }




}
