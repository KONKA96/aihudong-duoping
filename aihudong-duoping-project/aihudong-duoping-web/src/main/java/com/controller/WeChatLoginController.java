package com.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Teacher;
import com.service.TeacherService;
import com.util.HttpUtil;
import com.util.JsonUtils;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/index")
public class WeChatLoginController {
	@Value("${appid}")
	private String appid;
	
	@Value("${redirect_uri}")
	private String redirect_uri;
	
	@Value("${appSecret}")
	private String appSecret;
	
	@RequestMapping("/toIndex")
	public String toIndex() {
		return "/info";
	}
	@RequestMapping("/weiChatLogin")
	public String weiChatLogin() {
		return "/info2";
	}
	
	@Autowired
	private TeacherService teacherService;
	
	/**
	 * 获取参数
	 * @return
	 */
	@ResponseBody()
	@RequestMapping(value="/getWeiChatInfo",produces = "text/json;charset=UTF-8")
	public String getWeiChatInfo() {
		/*Map<String,String> map=new HashMap<>();
		map.put("appid", appid);
		map.put("redirect_uri", redirect_uri);*/
		String url="https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login#wechat_redirect";
		url=url.replace("APPID", appid);
		url=url.replace("REDIRECT_URI", redirect_uri);
		return JsonUtils.objectToJson(url);
	}
	
	/**
	 * 获取AccessToken
	 * @param code
	 */
	@RequestMapping("/getAccessToken")
	public void getAccessToken(@RequestParam String code) {
		String url="https://api.weixin.qq.com/sns/oauth2/access_token";
		String param="appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		param=param.replace("APPID", appid);
		param=param.replace("SECRET", appSecret);
		param=param.replace("CODE", code);
		String sendGET = HttpUtil.sendGET(url, param);
		JSONObject jsonObject = JSONObject.fromObject(sendGET);
		String refresh_token=jsonObject.getString("refresh_token");
		String openid=jsonObject.getString("openid");
		String access_token=jsonObject.getString("access_token");
		//刷新AccessToken
		reflushAccessToken(refresh_token);
		//检测AccessToken
		String testAccessTokenAuth = testAccessTokenAuth(access_token,openid);
		System.out.println(testAccessTokenAuth);
		
		Teacher teacher=new Teacher();
		teacher.setOpenId(openid);
		List<Teacher> teacherList = teacherService.selectAllTeacher(teacher);
		if(teacherList.size()==0) {
			//该用户第一次登录系统，未绑定微信
			//获取微信用户信息
			String userInfo = getUserInfo(access_token,openid);
			JSONObject userObject = JSONObject.fromObject(userInfo);
			String nickname=userObject.getString("nickname");
			System.out.println(nickname+"登录系统");
		}else {
			System.out.println(teacherList.get(0).getUsername()+"登录系统");
		}
		
	}
	
	/**
	 * 刷新AccessToken
	 * @param refresh_token
	 */
	public void reflushAccessToken(String refresh_token) {
		String url="https://api.weixin.qq.com/sns/oauth2/refresh_token";
		String param="appid=wx9261a9ef39996ff4&grant_type=refresh_token&refresh_token="+refresh_token;
		HttpUtil.sendGET(url, param);
	}
	
	/**
	 * 获取用户信息
	 */
	public String getUserInfo(String access_token,String openId) {
		String url="https://api.weixin.qq.com/sns/userinfo";
		String param="access_token="+access_token+"&openid="+openId;
		return HttpUtil.sendGET(url, param);
	}
	
	public String testAccessTokenAuth(String access_token,String openId) {
		String url="https://api.weixin.qq.com/sns/auth";
		String param="access_token="+access_token+"&openid="+openId;
		return HttpUtil.sendGET(url, param);
	}
	
}
