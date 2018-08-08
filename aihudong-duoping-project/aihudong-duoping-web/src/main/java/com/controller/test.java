package com.controller;


import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.model.Logger;
import com.service.AdminService;
import com.util.HttpUtil;

@Controller
@RequestMapping("/login")
public class test {
	protected Logger logger = Logger.getLogger(this.getClass()); 
	
	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;
	
	@Autowired AdminService adminService;
	
	@RequestMapping("/testaaa")
	public String testaaa(@RequestParam String username,ModelMap modelMap) {
		System.out.println(username);
		modelMap.put("username", username);
		modelMap.put("action", "create");
		String url=httpUrl+"/demo/demo1.jsp";
		return "redirect:"+url; 
	}
	
	@RequestMapping("/testjsp")
	public String testjsp(){
		return "/test";
	}
	
	
	/**
	 * 跳转到登录界面
	 * @return
	 */
	@RequestMapping("/toLogin")
	public String toLogin(){
		return "login";
	}
	/**
	 * 管理员登录
	 * @param admin
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/adminLogin")
	public String adminLogin(Admin admin,HttpSession session){
		UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(),
				new Md5Hash(admin.getPassword(), admin.getUsername() ,2).toString());
		Subject subject = SecurityUtils.getSubject();
		try {
			subject.login(token);
			admin = adminService.adminLogin(admin);
			
			logger.info(admin.getUsername()+"登录系统");
			session.setAttribute("admin", admin);
			return "success";
		} catch (UnknownAccountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "none";
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			return "error";
		}
	}
	
}
