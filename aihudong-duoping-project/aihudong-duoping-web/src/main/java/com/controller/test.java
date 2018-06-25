package com.controller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.service.AdminService;
import com.util.HttpUtil;
import com.util.XMLUtil;
import com.util.bbbApi;

@Controller
@RequestMapping("/login")
public class test {
	private static Logger log=LoggerFactory.getLogger(test.class);
	
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
		admin = adminService.adminLogin(admin);
		
		if(admin!=null){
			session.setAttribute("admin", admin);
			return "success";
		}
		return "";
	}

	
	@RequestMapping("/test")
	public String test() throws Exception {
		String meetingInfo = bbbApi.joinMeeting("784","2", salt);
		String sendGET = HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/join",meetingInfo);
		//Map doXMLParse = XMLUtil.doXMLParse(sendGET);
		
		return "redirect:"+httpUrl+"/bigbluebutton/api/join?"+meetingInfo;
	}
}
