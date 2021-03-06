package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.BaseTest;
import com.model.Admin;
import com.model.Logger;
import com.service.AdminService;
import com.util.HttpUtil;

public class testController extends BaseTest{
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired AdminService adminService;
	
	/**
	 * 根级管理员登录测试
	 */
	@Test
	public void genjiadminLogin(){
		Admin admin=new Admin();
		admin.setUsername("genji");
		admin.setPassword("123");
		admin = adminService.adminLogin(admin);
		
		if(admin!=null){
			logger.info(admin.getUsername()+"登录系统");
		}
	}
	/**
	 * 一级管理员登录测试
	 */
	@Test
	public void yijiadminLogin(){
		Admin admin=new Admin();
		admin.setUsername("yiji");
		admin.setPassword("123");
		admin = adminService.adminLogin(admin);
		
		if(admin!=null){
			logger.info(admin.getUsername()+"登录系统");
		}
	}
	
	/**
	 * 错误登录测试
	 */
	@Test
	public void adminLogin(){
		Admin admin=new Admin();
		admin.setUsername("123");
		admin.setPassword("123");
		admin = adminService.adminLogin(admin);
		
		if(admin!=null){
			logger.info(admin.getUsername()+"登录系统");
		}else {
			logger.info("用户不存在!");
		}
	}
	
	@Test
	public void frontLogin(){
		/*Teacher teacher=new Teacher();
		teacher.setUsername("2");
		teacher.setPassword("2");*/
		//String sendGET = HttpUtil.sendGET("http://www.51asj.com:9093/aihudong-duoping-web/front/userLogin", "username=2&password=2");
		
		Map<String,String> map=new HashMap<>();
		map.put("username", "2");
		map.put("password", "2");
		String sendPost = HttpUtil.sendPost("http://www.51asj.com:9093/aihudong-duoping-web/front/userLogin", map);
		System.out.println("sendPost="+sendPost);
	}
	
	
	
	
}
