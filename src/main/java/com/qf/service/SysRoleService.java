package com.qf.service;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  20:27
 */
public interface SysRoleService {
    List<String> findRolesByUserId(long userId);
}
