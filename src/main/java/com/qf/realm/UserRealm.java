package com.qf.realm;

import com.qf.entity.SysUser;
import com.qf.service.SysMenuService;
import com.qf.service.SysRoleService;
import com.qf.service.SysUserService;
import com.qf.utils.ShiroUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/27  10:02
 */
@Component(value = "userRealm")
public class UserRealm extends AuthorizingRealm {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysMenuService sysMenuService;
    @Resource
    private SysRoleService sysRoleService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("-------授权");

        SysUser user = (SysUser) principals.getPrimaryPrincipal();
        //根据当前用户id查询角色名
        List<String> roles = sysRoleService.findRolesByUserId(user.getUserId());
        //再查询权限
        List<String> perms = sysMenuService.findPermsByUserId(user.getUserId());

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        info.addRoles(roles);
        info.addStringPermissions(perms);
        System.out.println("----->授权over!");
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("------认证");

        UsernamePasswordToken u = (UsernamePasswordToken) token;
        String uname =u.getUsername();
        String password =new String( u.getPassword());

        //调用service层方法
        SysUser sysUser = sysUserService.findbyName(uname);
        if(sysUser==null){
            throw new UnknownAccountException("账号未知");
        }
        if(!sysUser.getPassword().equals(password)){
            throw  new IncorrectCredentialsException("密码错误");
        }
        if(sysUser.getStatus()==0){
            throw  new LockedAccountException("账号冻结");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(sysUser,password,this.getName());
        return info;
    }
}
