package com.controller;

import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.web.bind.annotation.RequestParam;

import com.BaseTest;
import com.util.HttpUtil;
import com.util.JsonUtils;
import com.util.XMLUtil;

import net.sf.json.JSONObject;

public class testWeiChat extends BaseTest{

	@Test
	public void sendGet(){
		String url="https://open.weixin.qq.com/connect/qrconnect";
		String param="appid=wx9261a9ef39996ff4&redirect_uri=http://www.51asj.com&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
		String sendGET = HttpUtil.sendGET(url, param);
		System.out.println(sendGET);
	}
	
	/**
	 * 通过code获取accesstoken
	 */
	@Test
	public void getAccessToken() {
		String url="https://api.weixin.qq.com/sns/oauth2/access_token";
		String code="";
		String param="appid=wx9261a9ef39996ff4&secret=5a3dbb3502243e7e53f7a3d72dbe128a&code="+code+"&grant_type=authorization_code";
		String sendGET = HttpUtil.sendGET(url, param);
		JSONObject jsonObject = JSONObject.fromObject(sendGET);
		
		System.out.println(sendGET);
	}
	
	/**
	 * 刷新accesstoken
	 */
	@Test
	public void reflushAccessToken() {
		String url="https://api.weixin.qq.com/sns/oauth2/refresh_token";
		String refresh_token="";
		String param="appid=wx9261a9ef39996ff4&grant_type=refresh_token&refresh_token="+refresh_token;
		String sendGET = HttpUtil.sendGET(url, param);
		System.out.println(sendGET);
	}
	
	/**
	 * 获取用户信息
	 */
	@Test
	public void getUserInfo() {
		String url="https://api.weixin.qq.com/sns/userinfo";
		String access_token="";
		String openId="";
		String param="access_token="+access_token+"&openid="+openId;
		String sendGET = HttpUtil.sendGET(url, param);
		System.out.println(sendGET);
	}
}
