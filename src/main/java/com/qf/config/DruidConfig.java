package com.qf.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Configuration
public class DruidConfig {


    @Bean("druidDataSource")
    //加载配置文件中以spring.datasource开头的配置
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        System.out.println("-------->init  dataSource！");
        DruidDataSource druidDataSource = new DruidDataSource();

        return druidDataSource;
    }
    @Bean
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();

        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);

        return wallFilter;
    }

}
