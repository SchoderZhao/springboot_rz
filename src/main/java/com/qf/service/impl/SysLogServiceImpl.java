package com.qf.service.impl;

import com.qf.entity.SysLog;
import com.qf.mapper.SysLogMapper;
import com.qf.service.SysLogService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/29  20:54
 */
@Service
public class SysLogServiceImpl implements SysLogService {
   @Resource
    private SysLogMapper sysLogMapper;
    @Override
    public int saveLog(SysLog sysLog) {

        return sysLogMapper.insert(sysLog);
    }
}
