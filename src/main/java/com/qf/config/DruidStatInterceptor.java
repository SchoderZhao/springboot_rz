package com.qf.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;


@Configuration
@ImportResource("classpath:applicationContext-monitor.xml")
//classpath:applicationContext-monitor.xml
public class DruidStatInterceptor {
}
