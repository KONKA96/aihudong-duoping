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
import com.model.VirtualRoomRecord;
import com.service.FileRecordService;
import com.service.RecordService;
import com.service.RoomService;
import com.service.ScreenService;
import com.service.StudentService;
import com.service.TeacherService;
import com.service.VirtualRoomRecordService;
import com.util.HttpUtil;
import com.util.JsonUtils;
import com.util.StringRandom;
import com.util.XMLUtil;
import com.util.bbbApi;

/**
 * 
 * @author KONKA
 *
 */
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
	@Autowired
	private VirtualRoomRecordService virtualRoomRecordService;

	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;

	Logger logger = Logger.getLogger(this.getClass());

	// private String BigBlueButtonURL = httpUrl + "/bigbluebutton/";

	/**
	 * 用户登录
	 * @author KONKA
	 * @param username 用户名
	 * @param password 密码
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

		argMap.put("username", username);
		
		Teacher teacher = null;
		Screen screen = new Screen();
		Student student = null;
		List<Screen> selectAllScreen = new ArrayList<>();
		if (username != null) {
			modelMap.put("username", username);
			teacher = new Teacher();
			teacher.setUsername(username);

			teacher = teacherService.teacherLogin(teacher);

			student = new Student();
			student.setUsername(username);

			student = studentService.studentLogin(student);

			screen.setUsername(username);

			selectAllScreen = screenService.selectAllScreen(screen);
			
		}else if (sid != null) {
			screen.setSid(sid);
			selectAllScreen = screenService.selectAllScreen(screen);
			if(selectAllScreen.size()>1) {
				argMap.put("code", Integer.valueOf(1004));
				argMap.put("message", "该机器绑定多个屏幕，请联系管理员!");
				return JsonUtils.objectToJson(argMap);
			}
		}
		
		else {
			argMap.put("code", Integer.valueOf(1002));
			argMap.put("message", "用户名为空");
			return JsonUtils.objectToJson(argMap);
		}
		Record record = new Record();
		if ((teacher == null) && (selectAllScreen.size() == 0) && (student == null)) {
			argMap.put("code", Integer.valueOf(1001));
			argMap.put("message", "用户不存在");
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
			if (openid != null && openid != "") {
				teacher.setOpenId(openid);
				teacher.setPassword(null);
				this.teacherService.updateTeacherSelected(teacher);
			}
			
			//统计在线人数
			Object tcount = servletContext.getAttribute("tcount");
			if(tcount==null) {
				servletContext.setAttribute("tcount", 1);
			}else {
				servletContext.setAttribute("tcount", Integer.parseInt(tcount.toString())+1);
			}
		} else if (selectAllScreen.size() != 0) {
			if (password != null && !((Screen) selectAllScreen.get(0)).getPassword()
					.equals(new Md5Hash(password, username, 2).toString())) {
				argMap.put("code", Integer.valueOf(1002));
				argMap.put("message", "密码错误");
				return JsonUtils.objectToJson(argMap);
			}
			

			argMap.put("username", selectAllScreen.get(0).getUsername());
			
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
			Room room = roomService.selectScreenByRoom(screen.getRoom());
			argMap.put("role", Integer.valueOf(4));
			argMap.put("meetingName", room.getNum());
			argMap.put("meetingId", room.getId());
			
			if ((selectAllScreen.size() != 0) && (((Screen) selectAllScreen.get(0)).getSid() == null) && (sid != null)) {
				selectAllScreen.get(0).setSid(sid);
				selectAllScreen.get(0).setPassword(null);
				screenService.updateByPrimaryKeySelective(selectAllScreen.get(0));
			}
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

			argMap.put("role", Integer.valueOf(2));
			record.setUserId(student.getId());
			record.setRole(Integer.valueOf(2));
			if (openid != null && openid != "") {
				student.setOpenId(openid);
				student.setPassword(null);
				this.studentService.updateByPrimaryKeySelective(student);
			}
			//统计在线人数
			Object scount = servletContext.getAttribute("scount");
			if(scount==null) {
				servletContext.setAttribute("scount", 1);
			}else {
				servletContext.setAttribute("scount", Integer.parseInt(scount.toString())+1);
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
	 * @author KONKA
	 * @param string 传回来的所有信息
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/userLogout")
	public String userLogout(@RequestParam String username, HttpServletResponse response, HttpServletRequest request)
			throws Exception {
		ServletContext servletContext = request.getServletContext();
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> argMap = new HashMap<>();
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
			teaLogout(userId, hour, minute, sec, servletContext);
		} else if (role == 2) {
			stuLogout(userId, hour, minute, sec, servletContext);

		} else if (role == 4) {
			screenLogout(userId, hour, minute, sec);
		}
		recordService.updateByPrimaryKeySelective(record);
		
		servletContext.removeAttribute(username);
		// 清除session
		argMap.put("code", Integer.valueOf(200));
		
		return JsonUtils.objectToJson(argMap);
	}

	public void teaLogout(String userId, int hour, int minute, int sec, ServletContext servletContext) {
		Teacher teacher = new Teacher();
		teacher.setId(userId);
		teacher = teacherService.selectTeacherById(teacher);
		// 更新教师使用时长，通过':'分割字符串，分别计算秒、分钟、小时
		String duration = teacher.getDuration();

		teacher.setTimes(teacher.getTimes() + 1);
		teacher.setDuration(countTime(duration, hour, minute, sec));
		//统计在线人数
		Integer tcount = (Integer) servletContext.getAttribute("tcount");
		servletContext.setAttribute("tcount", tcount-1);
		teacher.setPassword(null);
		teacherService.updateTeacherSelected(teacher);
	}

	public void stuLogout(String userId, int hour, int minute, int sec, ServletContext servletContext) {
		Student student = new Student();
		student.setId(userId);
		student = studentService.selectByPrimaryKey(student);
		String duration = student.getDuration();

		student.setTime(student.getTime() + 1);
		student.setDuration(countTime(duration, hour, minute, sec));
		//统计在线人数
		Integer scount = (Integer) servletContext.getAttribute("scount");
		servletContext.setAttribute("scount",scount-1);
		student.setPassword(null);
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
	 * @author KONKA
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
		argMap.put("meetingId", room.getId());
		argMap.put("meetingName", room.getNum());

		argMap.put("role", role);
		argMap.put("serverhost", serverhost);
		argMap.put("code", Integer.valueOf(200));
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 查询用户历史虚拟教室记录接口
	 * @author KONKA
	 * @param username
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectHistoryRoomRecord", produces = { "text/json;charset=UTF-8" })
	public String selectHistoryRoomRecord(@RequestParam(required = false) String username) {
		Map<String, Object> argMap = new HashMap<>();
		if(username==null) {
			argMap.put("code", "1001");
			argMap.put("message", "用户名为空");
			return JsonUtils.objectToJson(argMap);
		}
		
		
		Map<String,Object> map = new HashMap<>();
		
		Teacher teacher = new Teacher();
		teacher.setUsername(username);
		
		Student student = new Student();
		student.setUsername(username);
		
		teacher = teacherService.teacherLogin(teacher);
		student = studentService.studentLogin(student);
		
		
		List<Room> roomList = new ArrayList<>();
		if(teacher==null && student==null) {
			argMap.put("code", "1002");
			argMap.put("message", "用户不存在！");
			return JsonUtils.objectToJson(argMap);
		}else if(teacher!=null) {
			map.put("userId", teacher.getId());
			List<VirtualRoomRecord> vrrList = virtualRoomRecordService.selectVRR(map);
			for (VirtualRoomRecord virtualRoomRecord : vrrList) {
				roomList.add(virtualRoomRecord.getRoom());
			}
			
			argMap.put("code", "200");
			argMap.put("roomList", roomList);
		}else if(student!=null) {
			map.put("userId", student.getId());
			List<VirtualRoomRecord> vrrList = virtualRoomRecordService.selectVRR(map);
			
			for (VirtualRoomRecord virtualRoomRecord : vrrList) {
				roomList.add(virtualRoomRecord.getRoom());
			}
			
			argMap.put("code", "200");
			argMap.put("roomList", roomList);
		}
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 用户加入虚拟教室接口
	 * @author KONKA
	 * @param username
	 * @param roomId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/joinVirtualRoom", produces = { "text/json;charset=UTF-8" })
	public String joinVirtualRoom(@RequestParam(required = false) String username,
			@RequestParam(required = false) String roomId,HttpSession session) {
		Map<String, Object> argMap = new HashMap<>();
		if(username==null) {
			argMap.put("code", "1001");
			argMap.put("message", "用户名为空");
			return JsonUtils.objectToJson(argMap);
		}
		if(roomId==null) {
			argMap.put("code", "1001");
			argMap.put("message", "房间id为空");
			return JsonUtils.objectToJson(argMap);
		}
		
		Map<String,Object> map = new HashMap<>();
		
		Teacher teacher = new Teacher();
		teacher.setUsername(username);
		
		Student student = new Student();
		student.setUsername(username);
		
		teacher = teacherService.teacherLogin(teacher);
		student = studentService.studentLogin(student);
		
		Room room = new Room();
		room.setId(roomId);
		room = roomService.selectByPrimaryKey(room);
		VirtualRoomRecord virtualRoomRecord = new VirtualRoomRecord();
		virtualRoomRecord.setRoomId(roomId);
		virtualRoomRecord.setStartTime(new Date());
		if(teacher==null && student==null) {
			argMap.put("code", "1002");
			argMap.put("message", "用户不存在！");
			return JsonUtils.objectToJson(argMap);
		}else if(room==null) {
			argMap.put("code", "1002");
			argMap.put("message", "房间不存在！");
			return JsonUtils.objectToJson(argMap);
		}else if(teacher!=null) {
			virtualRoomRecord.setUserId(teacher.getId());
			virtualRoomRecord.setRole(1);
		}else if(student!=null) {
			virtualRoomRecord.setUserId(student.getId());
			virtualRoomRecord.setRole(2);
		}
		session.setMaxInactiveInterval(0);
		virtualRoomRecordService.insertSelective(virtualRoomRecord);
		ServletContext servletContext = session.getServletContext();
		session.setAttribute("virtualRoomRecord", virtualRoomRecord.getId());
		servletContext.setAttribute(username, session);
		argMap.put("code", "200");
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 查询房间接口
	 * @author KONKA
	 * @param roomNum
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectVirtualRoom", produces = { "text/json;charset=UTF-8" })
	public String selectVirtualRoom(@RequestParam(required = false) String roomNum) {
		Map<String, Object> argMap = new HashMap<>();
		if(roomNum==null) {
			argMap.put("code", "1001");
			argMap.put("message", "房间号为空！");
			return JsonUtils.objectToJson(argMap);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("roomNum", roomNum);
		List<Room> roomList = roomService.selectVirtualRoom(map);
		if(roomList.size()==0) {
			argMap.put("code", "1002");
			argMap.put("message", "房间不存在！");
			return JsonUtils.objectToJson(argMap);
		}
		argMap.put("code", "200");
		argMap.put("roomList", roomList);
		
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 编辑虚拟教室接口
	 * @author KONKA
	 * @param roomId
	 * @param roomNum
	 * @param userId
	 * @param desc
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/updateVirtualRoom", produces = { "text/json;charset=UTF-8" })
	public String updateVirtualRoom(@RequestParam(required = false) String roomId,
			@RequestParam(required = false) String roomNum,@RequestParam(required = false) String userId,
			@RequestParam(required = false) String desc) {
		Map<String, Object> argMap = new HashMap<>();
		if(roomId==null) {
			argMap.put("code", "1001");
			argMap.put("message", "房间为空！");
			return JsonUtils.objectToJson(argMap);
		}
		Room room = new Room();
		room.setId(roomId);
		if(roomNum!=null) {
			room.setNum(roomNum);
		}
		if(userId!=null) {
			room.setUserId(userId);
		}
		if(desc!=null) {
			room.setDesc(desc);
		}
		roomService.updateByPrimaryKeySelective(room);
		argMap.put("code", "200");
		return JsonUtils.objectToJson(argMap);
	}
	
	/**
	 * 退出虚拟教室接口
	 * @author KONKA
	 * @param username
	 * @param roomId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/userLogoutVirtualRoom", produces = { "text/json;charset=UTF-8" })
	public String userLogoutVirtualRoom(@RequestParam(required = false) String username,
			@RequestParam(required = false) String roomId,HttpSession session) {
		Map<String, Object> argMap = new HashMap<>();
		if(username==null) {
			argMap.put("code", "1001");
			argMap.put("message", "用户名为空！");
			return JsonUtils.objectToJson(argMap);
		}
		if(roomId==null) {
			argMap.put("code", "1001");
			argMap.put("message", "房间为空！");
			return JsonUtils.objectToJson(argMap);
		}
		
		ServletContext servletContext = session.getServletContext();
		session = (HttpSession) servletContext.getAttribute(username);
		int vrrId = (int) session.getAttribute("virtualRoomRecord");
		VirtualRoomRecord virtualRoomRecord = new VirtualRoomRecord();
		virtualRoomRecord.setId(vrrId);
		virtualRoomRecord = virtualRoomRecordService.selectByPrimaryKey(virtualRoomRecord);
		virtualRoomRecord.setEndTime(new Date());
		virtualRoomRecordService.updateByPrimaryKeySelective(virtualRoomRecord);
		argMap.put("code", "200");
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
