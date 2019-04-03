package com.qf.controller;

import com.qf.utils.R;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class DelController {

    @Resource
    Scheduler scheduler;

    @RequestMapping("/del")
    public R  del(String  jobId){
        try{
            JobKey jobKey = JobKey.jobKey(jobId);
            //删除定时任务
            boolean b = scheduler.deleteJob(jobKey);

            if (b){
                return R.ok();
            }

            return R.error();
        }catch(Exception e){
            e.printStackTrace();
        }

        return R.error();
    }


}
