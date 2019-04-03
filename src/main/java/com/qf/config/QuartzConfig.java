package com.qf.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/30  11:06
 */
@Configuration
public class QuartzConfig {
    @Bean
    public SchedulerFactoryBean scheduler(@Qualifier("druidDataSource") DataSource dataSource) {

        //quartz参数
        Properties prop = new Properties();
        //配置实例
        //prop.put("org.quartz.scheduler.instanceName", "MyScheduler");//实例名称
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.threadCount", "5");
        //JobStore配置
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setQuartzProperties(prop);
        factory.setSchedulerName("MyScheduler");//数据库中存储的名字
        //QuartzScheduler 延时启动，应用启动5秒后 QuartzScheduler 再启动
        factory.setStartupDelay(5);

        //factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory;
    }

}
