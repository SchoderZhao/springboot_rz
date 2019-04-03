package com.qf.controller;

import com.qf.entity.SysMenu;
import com.qf.log.Mylog;
import com.qf.service.SysMenuService;
import com.qf.utils.R;
import com.qf.utils.ResultData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/26  11:59
 */
@RestController
public class SysMenuController {

    @Autowired
    SysMenuService sysMenuService;

    //?order=asc&limit=10&offset=0
    @Mylog(value = "菜单列表",description = "分页查询并获取菜单列表")
    @RequestMapping("/sys/menu/list")

    @RequiresPermissions("sys:menu:list")
    public ResultData menuList(int limit, int offset, String search, String sort, String order) {

        return sysMenuService.findByPage(limit, offset, search, sort, order);
    }

    @RequestMapping("/sys/menu/del")

    public R del(@RequestBody List<Long> ids) {

        return sysMenuService.del(ids);
    }

    @RequestMapping("/sys/menu/info/{menuId}")

    public R findMenu(@PathVariable long menuId) {
        return sysMenuService.findMenu(menuId);

    }

    @RequestMapping("/sys/menu/update")
    public R update(@RequestBody SysMenu sysMenu) {

        return sysMenuService.update(sysMenu);
    }

    @Mylog(value = "新增菜单,目录,按钮",description = "新增菜单,目录,按钮")
    @RequiresPermissions("sys:menu:save")
    @RequestMapping("/sys/menu/save")
    public R saveMenu(@RequestBody SysMenu sysMenu){
        return  sysMenuService.save(sysMenu);
    }

    @RequestMapping("/sys/menu/select")
    public R selectMenu() {

        return sysMenuService.selectMenu();

    }
//查询当前用户菜单
    @RequestMapping("sys/menu/user")
    public R menUser() {

        return sysMenuService.findUserMenu();
    }


}
