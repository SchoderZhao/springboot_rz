package com.qf.service;

import com.qf.utils.Pager;
import com.qf.utils.ResultData;
import com.qf.utils.Sorter;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  20:27
 */
public interface SysRoleService {
    List<String> findRolesByUserId(long userId);

    ResultData findByPage(Pager pager, String search, Sorter sorter);


}
