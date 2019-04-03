package com.qf.service.impl;

import com.qf.entity.ScheduleJob;
import com.qf.entity.ScheduleJobLog;
import com.qf.mapper.ScheduleJobLogMapper;
import com.qf.mapper.ScheduleJobMapper;
import com.qf.service.ScheduleJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @auther ZhaoXingLei
 * @date 2019/04/01  21:10
 */
@Service("scheduleJobLogServiceImpl")
public class ScheduleJobLogServiceImpl implements ScheduleJobLogService {
    @Resource
    ScheduleJobLogMapper scheduleJobLogMapper;


    @Override
    public void insterJobLog(ScheduleJobLog scheduleJobLog) {
        scheduleJobLogMapper.insert(scheduleJobLog);
    }
}
