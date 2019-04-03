package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.entity.SysMenu;
import com.qf.entity.SysMenuExample;
import com.qf.mapper.SysMenuMapper;
import com.qf.service.SysMenuService;
import com.qf.utils.R;
import com.qf.utils.ResultData;
import com.qf.utils.ShiroUtils;
import com.qf.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/26  11:44
 */
@Service(value = "sysMenuServiceImpl")
public class SysMenuServiceImpl implements SysMenuService {
    @Resource
    SysMenuMapper sysMenuMapper;

    @Override
    public ResultData findByPage(int limit, int offset) {
        PageHelper.offsetPage(offset,limit);
        List<SysMenu> sysMenus = sysMenuMapper.selectByExample(null);
        PageInfo info = new PageInfo(sysMenus);

        long total = info.getTotal();
        List<SysMenu> list = info.getList();

        ResultData resultData = new ResultData(total,list);
        return resultData;
    }

    @Override
    public ResultData findByPage(int limit, int offset, String search) {
        PageHelper.offsetPage(offset,limit);
        SysMenuExample example=null;
        if(search!=null && !"".equals(search)){
            example=new SysMenuExample();
            SysMenuExample.Criteria criteria = example.createCriteria();
            criteria.andNameLike("%"+search+"%");
        }
        List<SysMenu> sysMenus = sysMenuMapper.selectByExample(example);
        PageInfo info = new PageInfo(sysMenus);
        long total = info.getTotal();
        List<SysMenu> list = info.getList();

        ResultData resultData = new ResultData(total,list);
        return resultData;
    }

    @Override
    public ResultData findByPage(int limit, int offset, String search, String sort, String order) {
        PageHelper.offsetPage(offset,limit);

        SysMenuExample example =  example= new SysMenuExample();

        SysMenuExample.Criteria criteria = example.createCriteria();
        if (search!=null&&!"".equals(search)){
            criteria.andNameLike("%"+search+"%");
        }
        //  order by ${orderByClause}   :${orderByClause}
        //sys/menu/list?sort=menuId&order=desc&limit=10&offset=0
        if (sort!=null&&sort.equals("menuId")){
            sort = "menu_id";
        }
        example.setOrderByClause(sort+" "+order);

        List<SysMenu> list  =  sysMenuMapper.selectByExample(example);


        PageInfo info = new PageInfo(list);

        long total = info.getTotal();
        List<SysMenu> list1 = info.getList();

        ResultData resultData = new ResultData(total,list1);

        return resultData;
    }

    @Override
    public R del(List<Long> ids) {
        SysMenuExample example = new SysMenuExample();
        SysMenuExample.Criteria criteria = example.createCriteria();
        for (Long id : ids) {
            if(id<31){
                return R.error("系统菜单,不能删除！请核对");
            }
        }
        //where id menu_in (?,?);
        criteria.andMenuIdIn(ids);
        int i = sysMenuMapper.deleteByExample(example);
        if(i>0){
            return  R.ok();
        }
        return R.error("删除失败");
    }

    @Override
    public R selectMenu() {
        List<SysMenu> list = sysMenuMapper.findMenuNotButton();

        SysMenu sysMenu = new SysMenu();
        sysMenu.setMenuId(0L);
        sysMenu.setParentId(-1L);
        sysMenu.setName("一级目录");
        sysMenu.setOrderNum(0);
        list.add(sysMenu);
        return R.ok().put("menuList",list);
    }

    @Override
    public R save(SysMenu sysMenu) {
        int insert = sysMenuMapper.insert(sysMenu);
        if (insert>0){
            return R.ok();
        }
        return R.error("新增失败");
    }

    @Override
    public R findMenu(long menuId) {

        SysMenu sysMenu = sysMenuMapper.selectByPrimaryKey(menuId);

        return R.ok().put("menu",sysMenu);
    }

    @Override
    public R update(SysMenu sysMenu) {
        int i = sysMenuMapper.updateByPrimaryKeySelective(sysMenu);
        if (i > 0) {
            return R.ok();
        }
        return R.error("修改失败");
    }

    @Override
    public List<String> findPermsByUserId(long userId) {
        //null  或者 "sys:user:list,sys:user:info,sys:user:select"
        List<String> list = sysMenuMapper.findPermsByUserId(userId);
        Set set = new HashSet<String>();
        for (String s : list) {
            if (StringUtils.isNotEmpty(s)){
                //"sys:user:list,sys:user:info,sys:user:select"
                //"sys:user:list"
                String ss[] =  s.split(",");
                for (String s1 : ss) {
                    set.add(s1);
                }
            }
        }

        List<String> newList = new ArrayList<>();
        newList.addAll(set);

        return newList;
    }

    @Override
    public R findUserMenu() {
        long userId = ShiroUtils.getUserId();
        //目录
        List<SysMenu> dir = sysMenuMapper.findDir(userId);
        for (SysMenu sysMenu : dir) {
            //查询菜单
            List<SysMenu> menuList =   sysMenuMapper.findMenu(sysMenu.getMenuId(),userId);
            sysMenu.setList(menuList);
        }

        List<String> permissions =this. findPermsByUserId(userId);

        return R.ok().put("menuList",dir).put("permissions",permissions);

    }
}
