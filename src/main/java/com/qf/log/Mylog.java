package com.qf.log;

import java.lang.annotation.*;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/29  14:23
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Mylog {

    String value();//记录方法的功能

    String description();//详细描述方法

}
