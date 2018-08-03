package com.controller;

import com.model.Record;
import com.model.Student;
import com.model.Teacher;
import com.service.RecordService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.HttpUtil;
import com.util.JsonUtils;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/index" })
public class WeChatLoginController {
	@Value("${appid}")
	private String appid;
	@Value("${redirect_uri}")
	private String redirect_uri;
	@Value("${appSecret}")
	private String appSecret;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RecordService recordService;

	@RequestMapping({ "/toIndex" })
	public String toIndex() {
		return "/info";
	}

	@RequestMapping({ "/weiChatLogin" })
	public String weiChatLogin() {
		return "/info2";
	}

	@ResponseBody
	@RequestMapping(value = { "/getWeiChatInfo" }, produces = { "text/json;charset=UTF-8" })
	public String getWeiChatInfo(HttpSession session, HttpServletResponse response, @RequestParam String state) {
		response.setHeader("Access-Control-Allow-Origin", "*");

		String url = "https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect";
		url = url.replace("APPID", this.appid);
		url = url.replace("REDIRECT_URI", this.redirect_uri);
		url = url.replace("STATE", state);
		return JsonUtils.objectToJson(url);
	}

	@RequestMapping({ "/getAccessToken" })
	public String getAccessToken(@RequestParam String code, @RequestParam String state, HttpSession session,
			ModelMap modelMap) {
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token";
		String param = "appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
		param = param.replace("APPID", this.appid);
		param = param.replace("SECRET", this.appSecret);
		param = param.replace("CODE", code);
		String sendGET = HttpUtil.sendGET(url, param);
		JSONObject jsonObject = JSONObject.fromObject(sendGET);
		String refresh_token = jsonObject.getString("refresh_token");
		String openid = jsonObject.getString("openid");
		String access_token = jsonObject.getString("access_token");

		String testAccessTokenAuth = testAccessTokenAuth(access_token, openid);
		System.out.println(testAccessTokenAuth);

		Teacher teacher = new Teacher();
		Student student = new Student();
		teacher.setOpenId(openid);
		student.setOpenId(openid);
		List<Teacher> teacherList = this.teacherService.selectAllTeacher(teacher);
		List<Student> studentList = this.studentService.selectAllStudent(student);
		if ((teacherList.size() == 0) && (studentList.size() == 0)) {
			modelMap.put("code", Integer.valueOf(200));
			modelMap.put("serverhost", state);
			modelMap.put("openid", openid);
			return "redirect:https://" + state + "/html5client/login";
		}
		Record record = new Record();
		if (teacherList.size() != 0) {
			teacher = (Teacher) teacherList.get(0);

			record.setUserId(teacher.getId());
			record.setRole(Integer.valueOf(1));
			modelMap.put("role", Integer.valueOf(1));
		} else if (studentList.size() != 0) {
			student = (Student) studentList.get(0);

			record.setUserId(student.getId());
			record.setRole(Integer.valueOf(2));
			modelMap.put("role", Integer.valueOf(2));
		}
		modelMap.put("code", Integer.valueOf(200));
		modelMap.put("serverhost", state);
		session.setMaxInactiveInterval(-1);
		session.setAttribute("count", Integer.valueOf(0));

		session.setAttribute("startTime", new Date());

		record.setStartTime(new Date());
		this.recordService.insertSelective(record);

		session.setAttribute("recordId", record.getId());
		session.setAttribute("startTime", record.getStartTime());
		session.setAttribute("role", record.getRole());
		session.setAttribute("userId", record.getUserId());

		return "redirect:https://" + state + "/html5client/login";
	}

	public void reflushAccessToken(String refresh_token) {
		String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
		String param = "appid=APPID&grant_type=refresh_token&refresh_token=" + refresh_token;
		param = param.replace("APPID", this.appid);
		HttpUtil.sendGET(url, param);
	}

	public String getUserInfo(String access_token, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo";
		String param = "access_token=" + access_token + "&openid=" + openId;
		return HttpUtil.sendGET(url, param);
	}

	public String testAccessTokenAuth(String access_token, String openId) {
		String url = "https://api.weixin.qq.com/sns/auth";
		String param = "access_token=" + access_token + "&openid=" + openId;
		return HttpUtil.sendGET(url, param);
	}

	public String bindUsername(String openId, ModelMap modelMap) {
		modelMap.addAttribute(openId);

		return "";
	}

	public String toConnectScreen(Teacher teacher, ModelMap modelMap) {
		return "";
	}
}
