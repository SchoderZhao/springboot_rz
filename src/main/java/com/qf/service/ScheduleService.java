package com.qf.service;

import com.qf.entity.ScheduleJob;
import com.qf.utils.Pager;
import com.qf.utils.R;
import com.qf.utils.ResultData;

import java.util.List;


public interface ScheduleService {

    public ResultData scheduleList(Pager pager, String search);

    public R save(ScheduleJob scheduleJob);
    public R update(ScheduleJob scheduleJob);
    public R delete(List<Long> jobIds);

    //暂停
    public R pause(List<Long> jobIds);
    //恢复
    public R resume(List<Long> jobIds);
    //立即执行
    public R run(List<Long> jobIds);

    //立即执行
    public R scheduleInfo(long id);


}
