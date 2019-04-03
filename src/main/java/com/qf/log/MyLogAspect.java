package com.qf.log;

import com.alibaba.fastjson.JSON;
import com.qf.entity.SysLog;
import com.qf.service.SysLogService;
import com.qf.utils.HttpContextUtils;
import com.qf.utils.IPUtils;
import com.qf.utils.ShiroUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/29  14:34
 */

@Aspect
@Component
public class MyLogAspect {
    //注入service
    @Resource
    private SysLogService sysLogService;
    @Pointcut(value = "@annotation(com.qf.log.Mylog)")

    public void  myPointcut(){ }

    @AfterReturning(pointcut = "myPointcut()")
    public void  after(JoinPoint joinPoint){

        //System.out.println("后置增强！"+joinPoint.getTarget()+joinPoint.getSignature());
        //private String username;//操作人
        //System.out.println("操作人："+ShiroUtils.getCurrentUser().getUsername());
        //private String operation;//操作
        MethodSignature signature =(MethodSignature) joinPoint.getSignature();
        Method method =  signature.getMethod();
        //
        Mylog mylog =  method.getAnnotation(Mylog.class);
        //System.out.println(mylog.description());

        //private String method;//调用的方法
        //System.out.println("method:"+method.getName());
        //       private String params;//参数
        //System.out.println("args :"+joinPoint.getArgs());
        //System.out.println("argsJSON :"+JSON.toJSONString(joinPoint.getArgs()));
        //得到客户端ip
        // private String ip;//调用者的ip
        //System.out.println(IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
        //private Date createDate; //调用时间

        String uname = ShiroUtils.getCurrentUser().getUsername();
        String opration = mylog.value();
        String methodName = joinPoint.getTarget().getClass()+"."+joinPoint.getSignature().getName();
        String params = JSON.toJSONString(joinPoint.getArgs());
        String ip = IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest());

        SysLog sysLog  = new SysLog();
        sysLog.setCreateDate(new Date());
        sysLog.setIp(ip);
        sysLog.setMethod(methodName);
        sysLog.setParams(params);
        sysLog.setUsername(uname);
        sysLog.setOperation(opration);

        int i =  sysLogService.saveLog(sysLog);

        System.out.println(i>0?"保存日志成功":"失败");

    }

}
