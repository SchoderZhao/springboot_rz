package com.qf.controller;

import com.qf.mapper.SysUserMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther ZhaoXingLei
 * @date 2019/04/02  14:23
 */
@Controller
public class ExportExcelController {
    @Resource
    private SysUserMapper sysUserMapper;
    @RequestMapping("/exportExcel")

    public void exportExcel(HttpServletResponse response){


    }
}
