package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.FileRecord;
import com.model.Record;
import com.model.Room;
import com.model.Screen;
import com.model.Student;
import com.model.Teacher;
import com.service.FileRecordService;
import com.service.RecordService;
import com.service.RoomService;
import com.service.ScreenService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.HttpUtil;
import com.util.JsonUtils;
import com.util.StringRandom;
import com.util.XMLUtil;
import com.util.bbbApi;

@Controller
@RequestMapping("/front")
public class QianduanController {
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private FileRecordService fileRecordService;

	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;

	Logger logger = Logger.getLogger(this.getClass());

	// private String BigBlueButtonURL = httpUrl + "/bigbluebutton/";

	/**
	 * 用户登录
	 * 
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param response
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = { "/userLogin" }, produces = { "text/json;charset=UTF-8" })
	public String UserLogin(@RequestParam(required = false) String username,
			@RequestParam(required = false) String password, @RequestParam(required = false) String sid,
			@RequestParam(required = false) String serverhost, @RequestParam(required = false) String openid,
			HttpServletResponse response, HttpServletRequest request, ModelMap modelMap) throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		ServletContext servletContext = request.getServletContext();
		HttpSession session = request.getSession();

		Map<String, Object> argMap = new HashMap<>();

		Teacher teacher = new Teacher();
		Screen screen = new Screen();
		Student student = new Student();
		List<Screen> selectAllScreen = new ArrayList<>();
		if (username != null) {
			modelMap.put("username", username);

			teacher.setUsername(username);

			teacher = this.teacherService.teacherLogin(teacher);

			student.setUsername(username);

			student = this.studentService.studentLogin(student);

			screen.setUsername(username);

			selectAllScreen = this.screenService.selectAllScreen(screen);
			if ((selectAllScreen.size() != 0) && (((Screen) selectAllScreen.get(0)).getSid() == null)) {
				screen = (Screen) selectAllScreen.get(0);
				screen.setSid(sid);
				this.screenService.updateByPrimaryKeySelective(screen);
			}
		} else {
			argMap.put("code", Integer.valueOf(1002));
			argMap.put("message", "用户名为空");
			return JsonUtils.objectToJson(argMap);
		}
		Record record = new Record();
		if ((teacher == null) && (selectAllScreen.size() == 0) && (student == null)) {
			argMap.put("code", Integer.valueOf(1001));
			argMap.put("message", "用户名不存在");
			return JsonUtils.objectToJson(argMap);
		}
		if ((teacher != null) && (teacher.getUsername() != null)) {
			if (!teacher.getPassword().equals(new Md5Hash(password, username, 2).toString())) {
				argMap.put("code", Integer.valueOf(1002));
				argMap.put("message", "密码错误");
				return JsonUtils.objectToJson(argMap);
			}
			session.setAttribute("teacher", teacher);

			servletContext.setAttribute(teacher.getUsername(), session);
			String sessionId = session.getId();
			teacher.setSessionId(sessionId);

			teacher.setRole(1);

			record.setUserId(teacher.getId());
			record.setRole(Integer.valueOf(1));
			argMap.put("role", Integer.valueOf(1));
			if (openid != null) {
				teacher.setOpenId(openid);
				this.teacherService.updateTeacherSelected(teacher);
			}
		} else if (selectAllScreen.size() != 0) {
			if (!((Screen) selectAllScreen.get(0)).getPassword()
					.equals(new Md5Hash(password, username, 2).toString())) {
				argMap.put("code", Integer.valueOf(1002));
				argMap.put("message", "密码错误");
				return JsonUtils.objectToJson(argMap);
			}
			screen = (Screen) selectAllScreen.get(0);
			session.setAttribute("screen", screen);
			String stringRandom = StringRandom.getStringRandom(3);

			servletContext.setAttribute(screen.getUsername(), stringRandom);
			servletContext.setAttribute(stringRandom, session);

			screen.setRole(4);
			String sessionId = session.getId();
			screen.setSessionId(sessionId);
			screen.setRandomname(stringRandom);

			record.setUserId(screen.getId());
			record.setRole(Integer.valueOf(4));
			screen = (Screen) selectAllScreen.get(0);
			Room room = this.roomService.selectScreenByRoom(screen.getRoom());
			argMap.put("role", Integer.valueOf(4));
			argMap.put("meetingName", room.getNum());
		} else if ((student != null) && (student.getUsername() != null)) {
			if (!student.getPassword().equals(new Md5Hash(password, username, 2).toString())) {
				argMap.put("code", Integer.valueOf(1002));
				argMap.put("message", "密码错误");
				return JsonUtils.objectToJson(argMap);
			}
			session.setAttribute("student", student);
			servletContext.setAttribute(student.getUsername(), session);
			student.setRole(2);
			String sessionId = session.getId();
			student.setSessionId(sessionId);

			record.setUserId(student.getId());
			record.setRole(Integer.valueOf(2));
			if (openid != null) {
				student.setOpenId(openid);
				this.studentService.updateByPrimaryKeySelective(student);
			}
		}
		session.setMaxInactiveInterval(-1);
		session.setAttribute("count", Integer.valueOf(0));

		session.setAttribute("startTime", new Date());

		record.setStartTime(new Date());
		this.recordService.insertSelective(record);

		session.setAttribute("recordId", record.getId());
		session.setAttribute("startTime", record.getStartTime());
		session.setAttribute("role", record.getRole());
		session.setAttribute("userId", record.getUserId());
		argMap.put("code", Integer.valueOf(200));
		argMap.put("serverhost", serverhost);

		return JsonUtils.objectToJson(argMap);
	}

	/**
	 * 用户登出
	 * 
	 * @param string 传回来的所有信息
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/userLogout")
	public void userLogout(@RequestParam String username, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		ServletContext servletContext = request.getServletContext();

		/*
		 * Screen screen=new Screen(); screen.setUsername(username); List<Screen>
		 * selectAllScreen = screenService.selectAllScreen(screen);
		 */
		HttpSession session = null;
		String randomname = null;
		session = (HttpSession) servletContext.getAttribute(username);
		/*
		 * if(selectAllScreen.size()==0) {
		 * 
		 * }else { randomname = (String) servletContext.getAttribute(username); session
		 * = (HttpSession) servletContext.getAttribute(randomname); }
		 */

		// 更新记录表内登出的时间
		int id = (int) session.getAttribute("recordId");
		Record record = new Record();
		record.setId(id);
		record.setEndTime(new Date());
		Date startTime = (Date) session.getAttribute("startTime");
		// 计算使用的时长
		long second = (record.getEndTime().getTime() - startTime.getTime()) / 1000;
		// 转化为小时、分钟、秒
		int hour = (int) (second / (60 * 60));
		int minute = (int) ((second % (60 * 60)) / 60);
		int sec = (int) (second % 60);

		int role = (int) session.getAttribute("role");
		String userId = (String) session.getAttribute("userId");
		if (role == 1) {
			teaLogout(userId, hour, minute, sec);
		} else if (role == 2) {
			stuLogout(userId, hour, minute, sec);

		} else if (role == 4) {
			screenLogout(userId, hour, minute, sec);
		}
		recordService.updateByPrimaryKeySelective(record);
		// 清除session
		session.invalidate();
		servletContext.removeAttribute(username);

		Screen screen = new Screen();
		screen.setId(record.getScreenId());
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);
		String meetingInfo = bbbApi.isMeetingRunning(selectAllScreen.get(0).getRoom().getNum(), salt);
		String sendGET = HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/isMeetingRunning", meetingInfo);
		Map doXMLParse = XMLUtil.doXMLParse(sendGET);
		String returnCode = (String) doXMLParse.get("running");
		if ("false".equals(returnCode)) {
			String endMeeting = bbbApi.endMeeting(selectAllScreen.get(0).getRoom().getNum(), salt);
			HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/end", endMeeting);
		}
		/*
		 * if(randomname!=null) { servletContext.removeAttribute(randomname); }
		 */
	}

	public void teaLogout(String userId, int hour, int minute, int sec) {
		Teacher teacher = new Teacher();
		teacher.setId(userId);
		teacher = teacherService.selectTeacherById(teacher);
		// 更新教师使用时长，通过':'分割字符串，分别计算秒、分钟、小时
		String duration = teacher.getDuration();

		teacher.setTimes(teacher.getTimes() + 1);
		teacher.setDuration(countTime(duration, hour, minute, sec));
		teacherService.updateTeacherSelected(teacher);
	}

	public void stuLogout(String userId, int hour, int minute, int sec) {
		Student student = new Student();
		student.setId(userId);
		student = studentService.selectByPrimaryKey(student);
		String duration = student.getDuration();

		student.setTime(student.getTime() + 1);
		student.setDuration(countTime(duration, hour, minute, sec));
		studentService.updateByPrimaryKeySelective(student);
	}

	public void screenLogout(String userId, int hour, int minute, int sec) {
		Screen screen = new Screen();
		screen.setId(userId);
		screen = screenService.selectByPrimaryKey(screen);
		int number = screen.getNumber();
		String duration = screen.getDuration();

		screen.setNumber(number + 1);
		screen.setDuration(countTime(duration, hour, minute, sec));
		screenService.updateByPrimaryKeySelective(screen);
	}

	/**
	 * 用户与屏幕连接
	 * 
	 * @param string
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = { "/connectToScreen" }, produces = { "text/json;charset=UTF-8" })
	public String connectToScreen(@RequestParam String usernameUser, @RequestParam String usernameScreen,
			@RequestParam String role, @RequestParam(required = false) String serverhost, HttpServletResponse response,
			HttpServletRequest request, ModelMap modelMap) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");

		Map<String, Object> argMap = new HashMap<>();

		ServletContext servletContext = request.getServletContext();
		HttpSession session = (HttpSession) servletContext.getAttribute(usernameUser);
		if (session == null) {
			argMap.put("code", Integer.valueOf(1002));
			argMap.put("message", "session失效");
			return JsonUtils.objectToJson(argMap);
		}
		Screen screen = new Screen();
		screen.setUsername(usernameScreen);
		List<Screen> selectAllScreen = this.screenService.selectAllScreen(screen);
		if (selectAllScreen.size() <= 0) {
			argMap.put("code", Integer.valueOf(1003));
			argMap.put("message", "屏幕不存在");
			return JsonUtils.objectToJson(argMap);
		}
		screen = (Screen) selectAllScreen.get(0);
		Room room = this.roomService.selectScreenByRoom(screen.getRoom());
		List<Screen> screenList = room.getScreenList();
		for (Screen scr : screenList) {
			if (!scr.getUsername().equals(usernameScreen)) {
				scr.setRandomname(StringRandom.getStringRandom(3));
			}
		}
		int id = ((Integer) session.getAttribute("recordId")).intValue();
		Record record = new Record();
		record.setId(Integer.valueOf(id));
		record.setScreenId(screen.getId());
		this.recordService.updateByPrimaryKeySelective(record);

		argMap.put("usernameUser", usernameUser);
		if (session.getAttribute("screen") != null) {
			argMap.put("usernameScreen", usernameScreen);
		}
		argMap.put("meetingName", room.getNum());

		argMap.put("role", role);
		argMap.put("serverhost", serverhost);
		argMap.put("code", Integer.valueOf(200));
		System.out.println(modelMap.toString());
		return JsonUtils.objectToJson(argMap);
	}

	@RequestMapping("/uploadFileRecordUser")
	public void uploadFileRecordUser(@RequestParam(required = false) String fileName,
			@RequestParam(required = false) String username, @RequestParam(required = false) String absolutePath,
			HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		HttpSession session = (HttpSession) servletContext.getAttribute(username);

		int id = (int) session.getAttribute("recordId");
		Record record = new Record();
		record.setId(id);
		record = recordService.selectByPrimaryKey(record);
		Screen screen = new Screen();
		screen.setId(record.getScreenId());
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);

		FileRecord file = new FileRecord();

		file.setRoomId(selectAllScreen.get(0).getRoom().getId());
		file.setFilename(fileName);
		file.setAbsolutePath(absolutePath);
		file.setUploadTime(new Date());
		Teacher teacher = (Teacher) session.getAttribute("teacher");
		if (teacher == null) {
			Student student = (Student) session.getAttribute("student");
			file.setUserId(student.getId());
		} else {
			file.setUserId(teacher.getId());
		}
		fileRecordService.insertSelective(file);
		session.setAttribute("fileRecord", file);

	}

	/**
	 * 断开连接
	 * 
	 * @param usernameUser
	 * @param sessionId
	 * @param usernameScreen
	 * @param password
	 * @param response
	 * @param request
	 */
	@RequestMapping("/breakConnect")
	public void breakConnect(String usernameUser, String sessionId, String usernameScreen, String password,
			HttpServletResponse response, HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		HttpSession session = (HttpSession) servletContext.getAttribute(usernameUser);
	}

	/**
	 * 增加用户的使用时长
	 * 
	 * @param duration
	 *            原有时长
	 * @param hour
	 *            小时
	 * @param minute
	 *            分钟
	 * @param sec
	 *            秒
	 * @return
	 */
	public String countTime(String duration, int hour, int minute, int sec) {

		String[] split = duration.split(":");
		sec = Integer.parseInt(split[2]) + sec;
		if (sec >= 60) {
			minute += (sec / 60);
			sec %= 60;
		}
		minute += Integer.parseInt(split[1]);
		if (minute >= 60) {
			hour += (minute / 60);
			minute %= 60;
		}
		hour += Integer.parseInt(split[0]);
		return hour + ":" + minute + ":" + sec;
	}
}
