package com.qf.quartz.task;

import org.springframework.stereotype.Component;

/**
 * @auther ZhaoXingLei
 * @date 2019/04/01  9:11
 */
@Component("unLockAccount")
public class UnLockAccount {

    public void jie(String number){
        System.out.println("解封账号"+number);
    }
}
