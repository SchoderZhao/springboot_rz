package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.entity.ScheduleJob;
import com.qf.entity.ScheduleJobExample;
import com.qf.mapper.ScheduleJobMapper;
import com.qf.service.ScheduleService;
import com.qf.utils.*;
import org.quartz.Scheduler;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


@Service
public class ScheduleServiceImpl  implements ScheduleService {

    @Resource
    private ScheduleJobMapper scheduleJobMapper;

    //依赖Scheduler    在QuartzConfig定义
    @Resource
    private Scheduler scheduler;


    @Override
    public ResultData scheduleList(Pager pager, String search) {

        PageHelper.offsetPage(pager.getOffset(),pager.getLimit());

        ScheduleJobExample example = new ScheduleJobExample();
        if (StringUtils.isNotEmpty(search)){
            ScheduleJobExample.Criteria criteria = example.createCriteria();
            criteria.andBeanNameLike("%"+search+"%");
        }
        List<ScheduleJob> list = scheduleJobMapper.selectByExample(example);

        PageInfo pageInfo = new PageInfo(list);

        ResultData resultData = new ResultData(pageInfo.getTotal(),pageInfo.getList());

        return resultData;
    }

    @Override
    public R save(ScheduleJob scheduleJob) {
        //1,保存Schedule_job表
        scheduleJob.setStatus(SysConstant.ScheduleStatus.NOMAL.getValue());
        scheduleJob.setCreateTime(new Date());
        int i = scheduleJobMapper.insert(scheduleJob);

        //2,真正定时任务的创建
        ScheduleUtils.createScheduleTask(scheduler,scheduleJob);

        return i>0?R.ok():R.error();
    }



    @Override
    public R delete(List<Long> jobIds) {
        //1,删除Schedule_job表的记录
        ScheduleJobExample example = new ScheduleJobExample();
        ScheduleJobExample.Criteria criteria =  example.createCriteria();
        criteria.andJobIdIn(jobIds);
        int i = scheduleJobMapper.deleteByExample(example);

        //2,删除真正的定时任务
        for (Long jobId : jobIds) {
            ScheduleUtils.deleteScheduleTask(scheduler,jobId);
        }

        return i>0?R.ok():R.error();
    }

    @Override
    public R update(ScheduleJob scheduleJob) {
        //1,修改数据库Schedule_job表
        int i = scheduleJobMapper.updateByPrimaryKeySelective(scheduleJob);
        //2,修改真正的定时任务
        ScheduleUtils.updateScheduleTask(scheduler,scheduleJob);
        return i>0?R.ok():R.error();
    }

    @Override
    public R pause(List<Long> jobIds) {
        //1，修改数据库Schedule_job表的状态 status
        ScheduleJobExample example = new ScheduleJobExample();
        ScheduleJobExample.Criteria criteria =  example.createCriteria();
        criteria.andJobIdIn(jobIds);
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setStatus(SysConstant.ScheduleStatus.PAUSE.getValue());
        int i = scheduleJobMapper.updateByExampleSelective(scheduleJob,example);

        //2,真正暂停任务
        for (Long jobId : jobIds) {
            ScheduleUtils.pause(scheduler,jobId);
        }

        return i>0?R.ok():R.error();

    }

    @Override
    public R resume(List<Long> jobIds) {
        //1,修改表的状态
        ScheduleJobExample example = new ScheduleJobExample();
        ScheduleJobExample.Criteria criteria =  example.createCriteria();
        criteria.andJobIdIn(jobIds);
        ScheduleJob scheduleJob = new ScheduleJob();
        scheduleJob.setStatus(SysConstant.ScheduleStatus.NOMAL.getValue());
        int i = scheduleJobMapper.updateByExampleSelective(scheduleJob,example);

        //2,真正恢复任务
        for (Long jobId : jobIds) {
            ScheduleUtils.resume(scheduler,jobId);
        }
        return i>0?R.ok():R.error();
    }

    @Override
    public R run(List<Long> jobIds) {
//        ScheduleJobExample example = new ScheduleJobExample();
//        ScheduleJobExample.Criteria criteria =  example.createCriteria();
//        criteria.andJobIdIn(jobIds);
//        ScheduleJob scheduleJob = new ScheduleJob();
//        scheduleJob.setStatus(SysConstant.ScheduleStatus.PAUSE.getValue());
//        int i = scheduleJobMapper.updateByExampleSelective(scheduleJob,example);

        for (Long jobId : jobIds) {
            ScheduleUtils.runOnce(scheduler,jobId);
        }
        return R.ok();
    }

    @Override
    public R scheduleInfo(long id) {
        ScheduleJob scheduleJob = scheduleJobMapper.selectByPrimaryKey(id);
        return R.ok().put("scheduleJob",scheduleJob);
    }
}
