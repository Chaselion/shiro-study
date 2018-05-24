package com.imooc.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.vo.User;

@Controller
public class UserController {
	@RequestMapping(value = "/subLogin" , method = RequestMethod.POST,
			produces = "application/json;charset=utf-8"
			)
	@ResponseBody
	public String subLogin(User user ){
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		try {
			token.setRememberMe(user.isRememberMe());
			subject.login(token);
		} catch (AuthenticationException e) {
			return e.getMessage();
		}
		if(subject.hasRole("admin")) {
			return "有权限！";
		}
		return "登陆成功";
	}
	
	@RequiresRoles("admin")
	@RequestMapping(value = "/testRole" ,method = RequestMethod.GET)
	@ResponseBody
	public String testRole() {
		return "testRole success";
	}
	@RequestMapping(value = "/testRole1" ,method = RequestMethod.GET)
	@ResponseBody
	public String testRole1() {
		return "testRole1 success";
	}
	@RequestMapping(value = "/testPerms" ,method = RequestMethod.GET)
	@ResponseBody
	public String testPerms() {
		return "testPerms success";
	}
	@RequestMapping(value = "/testPerms1" ,method = RequestMethod.GET)
	@ResponseBody
	public String testPerms1() {
		return "testPerms1 success";
	}
	
	//xxx代表当前主题具备括号中相应权限的时候才可以执行下面相应的方法
	/*@RequiresPermissions("xxx")
	@RequestMapping(value = "/testRole1" ,method = RequestMethod.GET)
	@ResponseBody
	public String testRole1() {
		return "testRole1 success";
	}*/
}
