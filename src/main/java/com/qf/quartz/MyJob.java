package com.qf.quartz;

import com.alibaba.fastjson.JSON;
import com.qf.entity.ScheduleJob;
import com.qf.entity.ScheduleJobLog;
import com.qf.service.ScheduleJobLogService;
import com.qf.service.ScheduleService;
import com.qf.utils.SpringContextUtils;
import com.qf.utils.StringUtils;
import com.qf.utils.SysConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.lang.reflect.Method;
import java.util.Date;


public class MyJob implements Job {

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("helloworld!!!!");
        ScheduleJobLog log =new ScheduleJobLog();
        long start = System.currentTimeMillis();
        try {


            String json = (String)context.getJobDetail().getJobDataMap()
                    .get(SysConstant.SCHEDULE_DATA_KEY);
            ScheduleJob scheduleJob = JSON.parseObject(json, ScheduleJob.class);
            String beanName = scheduleJob.getBeanName();
            String method = scheduleJob.getMethodName();
            String params = scheduleJob.getParams();

            Object object = SpringContextUtils.getBean(beanName);
            Class clazz = object.getClass();
            if (StringUtils.isEmpty(params)){
                Method method1 = clazz.getDeclaredMethod(method);
                method1.invoke(object);
            }else {
                Method method1 = clazz.getDeclaredMethod(method, String.class);
                method1.invoke(object,params);

            }
            log.setJobId(scheduleJob.getJobId());
            log.setBeanName(beanName);
            log.setMethodName(method);
            log.setParams(params);
            log.setCreateTime(new Date());
            log.setStatus(SysConstant.ScheduleStatus.NOMAL.getValue());

        } catch (Exception e) {
            e.printStackTrace();
            log.setError(e.getMessage());

        }
        ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService)SpringContextUtils.getBean("scheduleJobLogServiceImpl");
        long end = System.currentTimeMillis();
        log.setTimes(Integer.parseInt(end-start+""));
        scheduleJobLogService.insterJobLog(log);
    }
}
