package com.qf.controller;

import com.google.code.kaptcha.Producer;
import com.qf.dto.SysUserDTO;
import com.qf.entity.SysUser;
import com.qf.log.Mylog;
import com.qf.service.SysUserService;
import com.qf.utils.*;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @auther ZhaoXingLei
 * @date 2019/03/25  17:34
 */
@RestController
public class SysUserController {
    @Resource
    private SysUserService sysUserService;
    @Resource
    private Producer producer;

    @RequestMapping("/findAll")
    public List<SysUser> findAll() {
        return sysUserService.findAll();
    }

    @RequestMapping("/sys/login")
    public R login(@RequestBody SysUserDTO sysUser) {
        String code = ShiroUtils.getCaptcha();
        String c = sysUser.getCaptcha();
        if (code != null && !code.equalsIgnoreCase(c)) {
            return R.error("验证码错误");

        }


        //传统登录
        //return sysUserService.login(sysUser);

        //1 得到subject
        String s =null;
        try {
            Subject subject = SecurityUtils.getSubject();

            String pwd  =sysUser.getPassword();
            Md5Hash md5Hash = new Md5Hash(pwd,sysUser.getUsername(),1024);

            pwd= md5Hash.toString();
            System.out.println(pwd);
            //2把用户户名密码封装成USernamePassWordToken
            UsernamePasswordToken token = new UsernamePasswordToken(sysUser.getUsername(),pwd);
            if(sysUser.isRememberMe()){
                token.setRememberMe(true);
            }
            subject.login(token);
            System.out.println(subject.hasRole("管理员"));
            System.out.println(subject.isPermitted("sys:menu:list"));
            return R.ok();
        } catch (Exception e) {
            s=e.getMessage();
        }

        return R.error(s);
    }

    @Mylog(value = "用户列表",description = "分页获取用户列表")
    @RequestMapping("/sys/user/list")

    public ResultData findBypage(Pager pager, String search, Sorter sorter) {

        return  sysUserService.findByPage(pager,search,sorter);

    }

    @RequestMapping("/captcha.jpg")
    public void captcha(HttpServletResponse response) {
        try {
            String text = producer.createText();
            ShiroUtils.setAttribute("code", text);
            BufferedImage bufferedImage = producer.createImage(text);
            OutputStream os = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpg", os);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @RequestMapping("/email")
    public R  email(String to){
        String text = producer.createText();
        ShiroUtils.setAttribute("emile", text);
        EmailUtils emailUtils = new EmailUtils();

        try {
            emailUtils.QQEmail(to,text);
        } catch (Exception e) {
           return R.error("发送失败");
        }
        return R.ok("发送成功");
    }

    @RequestMapping("/yanzheng")
    public R  yanzheng(String contont){
        String code = (String)ShiroUtils.getAttribute("emile");
        if (code != null && !code.equalsIgnoreCase(contont)) {
            return R.error("验证码错误");

        }
        return R.ok("验证码正确");
    }

    @RequestMapping("/sys/user/info")
    public  R userInfo(){

        // System.out.println("--->shiro中取出用户信息："+SecurityUtils.getSubject().getPrincipal());

        SysUser user = ShiroUtils.getCurrentUser();
        return  R.ok().put("user",user);

    }

    @RequestMapping("/sys/echarts/pie")
    public  R pie(){

        return sysUserService.findPieData();
    }

    @RequestMapping("/sys/echarts/bar")
    public  R bar(){

        return sysUserService.findBarData();
    }

}
