package com.qf.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  10:57
 */
//验证码
@Configuration
public class KaptchaConfig {

    @Bean
    public Producer producer(){
        DefaultKaptcha producer = new DefaultKaptcha();
        Properties properties = new Properties();
        properties.setProperty(Constants.KAPTCHA_SESSION_KEY,"black");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH,"4");
        properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR,"white");
        Config config =new Config(properties);
        producer.setConfig(config);
        return  producer;
    }
}
