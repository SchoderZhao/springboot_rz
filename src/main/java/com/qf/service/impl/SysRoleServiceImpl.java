package com.qf.service.impl;

import com.qf.mapper.SysRoleMapper;
import com.qf.service.SysRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  20:34
 */
@Service("sysRoleServiceImpl")
public class SysRoleServiceImpl implements SysRoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Override
    public List<String> findRolesByUserId(long userId) {

        return sysRoleMapper.findRolesByUserId(userId);

    }
}
