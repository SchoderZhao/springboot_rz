package com.qf.service;

import com.qf.entity.SysUser;
import com.qf.utils.Pager;
import com.qf.utils.R;
import com.qf.utils.ResultData;
import com.qf.utils.Sorter;

import java.util.List;
import java.util.Map;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/25  17:23
 */
public interface SysUserService {
    List<SysUser> findAll();

    R login (SysUser sysUser);

    SysUser findbyName (String username);
    ResultData findByPage(Pager pager, String search, Sorter sorter);

    R findPieData ();
    R findBarData ();

    List<Map<String,Object>> exportExcel();
}
