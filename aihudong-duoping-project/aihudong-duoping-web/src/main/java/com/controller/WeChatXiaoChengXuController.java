package com.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Student;
import com.model.Teacher;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.AesCbcUtil;
import com.util.HttpUtil;
import com.util.JsonUtils;

import net.sf.json.JSONObject;

@RequestMapping("/xiaoChengXu")
@Controller
public class WeChatXiaoChengXuController {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
 
	@Value("${xiaochengxuAppId}")
	private String xiaochengxuAppId;
	@Value("${xiaochengxuAppSecret}")
	private String xiaochengxuAppSecret;
	
	/**
	 * 通过code访问接口获取参数
	 * @author KONKA
	 * @param js_code
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/code2accessToken", produces = { "text/json;charset=UTF-8" })
	public String code2accessToken(String js_code) {
		
		String url = "https://api.weixin.qq.com/sns/jscode2session";
		String param = "appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
		
		param = param.replace("APPID", xiaochengxuAppId);
		param = param.replace("SECRET", xiaochengxuAppSecret);
		param = param.replace("JSCODE", js_code);
		
		String sendGET = HttpUtil.sendGET(url, param);
		JSONObject jsonObject = JSONObject.fromObject(sendGET);
		
		return sendGET;
	}
	
	/**
	 * 获取用户信息（unionid）
	 * @author KONKA
	 * @param encryptedData
	 * @param session_key
	 * @param iv
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserInfo", produces = { "text/json;charset=UTF-8" })
	public String getUserInfo(String encryptedData,String session_key,String iv) {
		
		String decrypt = "";
		try {
			decrypt = AesCbcUtil.decrypt(encryptedData, session_key, iv, "UTF-8");
			logger.info(decrypt);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return decrypt;
	}
	
	/**
	 * 通过用户unionId查询用户信息
	 * @author KONKA
	 * @param unionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/checkUserId", produces = { "text/json;charset=UTF-8" })
	public String checkUserId(String unionId) {
		Map<String, Object> argMap = new HashMap<>();
		if(unionId==null || "".equals(unionId)) {
			argMap.put("code", "500");
			argMap.put("message", "用户id为空");
			return JsonUtils.objectToJson(argMap);
		}
		Teacher teacher = new Teacher();
		Student student = new Student();
		teacher.setUnionId(unionId);
		student.setUnionId(unionId);
		List<Teacher> teacherList = teacherService.selectAllTeacher(teacher);
		List<Student> studentList = studentService.selectAllStudent(student);
		if(teacherList.size()==0 && studentList.size()==0) {
			argMap.put("code", "404");
			argMap.put("message", "用户不存在");
			return JsonUtils.objectToJson(argMap);
		}else if(teacherList.size()!=0){
			argMap.put("user", teacherList.get(0));
			argMap.put("role", 1);
			argMap.put("code", "200");
			return JsonUtils.objectToJson(argMap);
		}else {
			argMap.put("user", studentList.get(0));
			argMap.put("role", 2);
			argMap.put("code", "200");
			return JsonUtils.objectToJson(argMap);
		}
		
	}
}
