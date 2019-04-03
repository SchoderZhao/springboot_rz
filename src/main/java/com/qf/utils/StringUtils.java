package com.qf.utils;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  9:19
 */
public class StringUtils {

    public static  boolean isEmpty(String s){
        if(s==null){
            return true;
        }
        if(s.trim().length()==0){
            return true;
        }
        return false;
    }
    public  static  boolean isNotEmpty(String s){

        return !isEmpty(s);
    }

}
