package com.qf.config;

import com.qf.realm.UserRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  9:45
 */
//等价于xml配置文件

@Configuration
public class ShiroConfig {
    @Bean(value ="sessionManager" )
    public SessionManager sessionManager(){

        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();

        //禁止URL上拼接session
        sessionManager.setSessionIdUrlRewritingEnabled(false);

        //session默认时间半小时
        sessionManager.setGlobalSessionTimeout(1000*60*60);
        //定时清理去过期回话
        sessionManager.setSessionValidationSchedulerEnabled(true);

        return sessionManager;
    }
    @Bean(value ="securityManager" )

    //方法的参数相当于传传入spring容器中创建的对象
    public SecurityManager securityManager(UserRealm userRealm, SessionManager sessionManager){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setRealm(userRealm);

        //缓存
        EhCacheManager ehCacheManager = new EhCacheManager();
        ehCacheManager.setCacheManagerConfigFile("classpath:ehcache.xml");

        securityManager.setCacheManager(ehCacheManager);

        return securityManager;
    }

    //shiro注解在spring容器中生效
    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return  advisorAutoProxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();

        advisor.setSecurityManager(securityManager);

        return advisor;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        //设置登录页面
        shiroFilterFactoryBean.setLoginUrl("/login.html");
        //设置成功页面
        shiroFilterFactoryBean.setSuccessUrl("/index.html");
        //没有权限页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unauthorized.html");
        //什么 Map能保证存取顺序?
        //LinkedHashMap
        LinkedHashMap<String,String> map = new LinkedHashMap<>();

        map.put("/json/**","anon");
        map.put("/public/**","anon");
        //map.put("/email/**","anon");
        //map.put("/yanzheng/**","anon");
        map.put("/sys/login","anon");
        map.put("/captcha.jpg","anon");//验证码
        //map.put("/sys/menu/*","perms[\"sys:menu\"]");
        //map.put("/**","user");//选中记住我能访问的资源
        map.put("/logout","logout");
        map.put("/**","authc");//登录后才能访问



        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);


        return shiroFilterFactoryBean;
    }

}
