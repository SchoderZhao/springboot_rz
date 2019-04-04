package com.qf.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qf.entity.SysUser;
import com.qf.entity.SysUserExample;
import com.qf.mapper.SysUserMapper;
import com.qf.service.SysUserService;
import com.qf.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/25  17:26
 */
@Service("sysUserServiceImpl")
public class SysUserServiceImpl implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> findAll() {

        return sysUserMapper.selectByExample(null);


    }

    @Override
    public R login(SysUser sysUser) {


        //方法一：select * from sys_User where username=#{username}

        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria =  example.createCriteria();
        //方法二 使用example
        criteria.andUsernameEqualTo(sysUser.getUsername());


        List<SysUser> list = sysUserMapper.selectByExample(example);
        if(list==null||list.size()==0){
            return R.error("账号不存在");
        }
        SysUser u = list.get(0);
        if (!u.getPassword().equals(sysUser.getPassword())){
            return R.error("密码错误");
        }
        if (u.getStatus()==0){
            return R.error("账号被冻结");
        }


        return R.ok().put("u",u);
    }

    @Override
    public ResultData findByPage(Pager pager, String search, Sorter sorter) {

        PageHelper.offsetPage(pager.getOffset(),pager.getLimit());
        SysUserExample example = new SysUserExample();

        if(sorter!=null&&StringUtils.isNotEmpty(sorter.getSort())){
            example.setOrderByClause("user_id "+sorter.getOrder());
        }
        SysUserExample.Criteria criteria =example.createCriteria();
        if (search!=null&&!"".equals(search)){
            criteria.andUsernameLike("%"+search+"%");
        }

        List<SysUser> sysUsers = sysUserMapper.selectByExample(example);
        PageInfo info = new PageInfo(sysUsers);

        ResultData data = new ResultData(info.getTotal(),info.getList());

        return data;
    }

    @Override
    public R findPieData() {


        List<Map<String, Object>> list = sysUserMapper.findPieData();
        List list1 = new ArrayList();
        List list2 = new ArrayList();
        for (Map<String, Object> map : list) {
            String name = map.get("name")+"";
            String value = map.get("value")+"";
            list1.add(name);
            list2.add(value);
        }
        return R.ok().put("name",list1).put("value",list2);
    }

    @Override
    public R findBarData() {
        List<Map<String, Object>> list = sysUserMapper.findBarData();

        List xAxisData = new ArrayList();//x坐标
        List series0Data = new ArrayList();//男
        List series1Data = new ArrayList();//女

        for (Map<String, Object> map : list) {
            String deptName = map.get("name")+"";
            Object boy = map.get("boy");
            Object girl = map.get("girl");

            xAxisData.add(deptName);
            series0Data.add(boy);
            series1Data.add(girl);
        }



        return R.ok().put("xAxisData",xAxisData).
                put("series0Data",series0Data).put("series1Data",series1Data);
    }

    @Override
    public SysUser findbyName(String username) {
        SysUserExample example = new SysUserExample();
        SysUserExample.Criteria criteria =example.createCriteria();

        criteria.andUsernameEqualTo(username);

        List<SysUser> list = sysUserMapper.selectByExample(example);

        if(list!=null&&list.size()>0){
            System.out.println(list.get(0));
            return list.get(0);
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> exportExcel() {

        return sysUserMapper.findUserForExport();
    }
}
