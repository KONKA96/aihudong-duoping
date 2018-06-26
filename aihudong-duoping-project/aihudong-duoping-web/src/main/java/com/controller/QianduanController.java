package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Err;
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

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
	
	Logger logger  = Logger.getLogger(this.getClass());
	
	private String BigBlueButtonURL = httpUrl+"/bigbluebutton/";
	/**
	 * 用户登录
	 * @param string 传过来的所有信息
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping(value="/userLogin")
	public String UserLogin(@RequestParam String username,@RequestParam String password,
			HttpServletResponse response,HttpServletRequest request,ModelMap modelMap) throws IOException{
		response.setCharacterEncoding("utf-8");
		ServletContext servletContext = request.getServletContext();
//		modelMap.put("action", "create");
		modelMap.put("username", username);
		// 
/*//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
//		base64解码
		BASE64Decoder decoder = new BASE64Decoder();*/
		/*string=new String(decoder.decodeBuffer(string), "UTF-8");
		JSONObject fromObject = JSONObject.fromObject(string);*/
		
		/*String username =fromObject.getString("username");
		String password=fromObject.getString("password");
		String device=fromObject.getString("device");*/
//		Set<Entry<String, String>> entrySet = map.entrySet();
		/*Set<Entry<String, String>> entrySet = new HashSet<>();
		for (Entry<String, String> entry : entrySet) {
			if(entry.getKey().equals("username")){
				username=entry.getValue();
			}
			if(entry.getKey().equals("password")){
				password=entry.getValue();
			}
		}*/
		HttpSession session = request.getSession();
//		通过用户名密码在数据库中查，看该用户是教师、屏幕、学生这三者的其中一种
		Teacher teacher=new Teacher();
		teacher.setUsername(username);
		teacher.setPassword(password);
		teacher = teacherService.teacherLogin(teacher);
		
		Screen screen=new Screen();
		screen.setUsername(username);
		screen.setPassword(password);
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);
		
		Student student=new Student();
		student.setUsername(username);
		student.setPassword(password);
		student=studentService.studentLogin(student);
//		Subject sub=null;
//		保存的记录对象
		Record record=new Record();
		
//		如果都没有查询到，则报1001
		if(teacher==null&&selectAllScreen.size()==0&&student==null){
			/*Err err = new Err(1001,"用户名不存在或密码错误");
			String errString = JsonUtils.objectToJson(err);
			String errEncode = encoder.encode(errString.getBytes());
			writer.write(errEncode);*/
			return "redirect:"+httpUrl+"/demo/error.jsp";
		}else if(teacher!=null){
			session.setAttribute("teacher", teacher);
//			将session存放在application里面，其用户名username作为session的键
			servletContext.setAttribute(teacher.getUsername(), session);
			String sessionId = session.getId();
			teacher.setSessionId(sessionId);
			
			teacher.setRole(1);
			/*String jsonTeacher = JsonUtils.objectToJson(teacher);*/
			
//			sub=teacher.getSubject();
			
			record.setUserId(teacher.getId());
			record.setRole(1);
			
			modelMap.put("role", 1);
		}else if(selectAllScreen.size()!=0){
			//设置session时间为永久
			 
			session.setAttribute("screen", screen);
			String stringRandom=StringRandom.getStringRandom(3);
			
			servletContext.setAttribute(screen.getUsername(), stringRandom);
			servletContext.setAttribute(stringRandom, session);
			screen=selectAllScreen.get(0);
			screen.setRole(4);
			String sessionId = session.getId();
			screen.setSessionId(sessionId);
			screen.setRandomname(stringRandom);
			/*String jsonScreen = JsonUtils.objectToJson(screen);*/
			
			record.setUserId(screen.getId());
			record.setRole(4);
			
			screen = selectAllScreen.get(0);
			Room room = roomService.selectScreenByRoom(screen.getRoom());
			modelMap.put("role", 4);
			modelMap.put("meetingName", room.getNum());
		}else if(student!=null){
			session.setAttribute("student", student);
			servletContext.setAttribute(student.getUsername(), session);
			student.setRole(2);
			String sessionId = session.getId();
			student.setSessionId(sessionId);
			/*String jsonStudent = JsonUtils.objectToJson(student);*/
//			sub=student.getSubject();
			
			record.setUserId(student.getId());
			record.setRole(2);
			
			modelMap.put("role", 2);
		}
		session.setMaxInactiveInterval(-1);
		session.setAttribute("count", 0);
		// 灏嗙敤鎴蜂俊鎭紶鍒板墠绔�
		session.setAttribute("startTime", new Date());
		// 新建记录对象，新增的数据库中，还缺少登出时间和连接的屏幕id
		record.setStartTime(new Date());
		recordService.insertSelective(record);
		//将该条记录的id记录在session中，方便后面更改该条记录
		session.setAttribute("recordId", record.getId());
		session.setAttribute("startTime", record.getStartTime());
		session.setAttribute("role", record.getRole());
		session.setAttribute("userId", record.getUserId());
		/*return "success";*/
		return "redirect:"+httpUrl+"/demo/demo_join.jsp";
		/*ClientHeart client = new ClientHeart(session,username);
        client.start();*/
		/*writer.close();*/
	}
	
	/**
	 * 用户登出
	 * @param string 传回来的所有信息
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping("/userLogout")
	public void userLogout(@RequestParam String username,HttpServletResponse response,HttpServletRequest request) throws Exception{
		ServletContext servletContext = request.getServletContext();
		
//		base64解码
		BASE64Decoder decoder = new BASE64Decoder();
		
		
		/*Screen screen=new Screen();
		screen.setUsername(username);
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);*/
		HttpSession session=null;
		String randomname=null;
		session=(HttpSession) servletContext.getAttribute(username);
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
		
		
		Screen screen=new Screen();
		screen.setId(record.getScreenId());
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);
		String meetingInfo = bbbApi.isMeetingRunning(selectAllScreen.get(0).getRoom().getNum(), salt);
		String sendGET = HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/isMeetingRunning",meetingInfo);
		Map doXMLParse = XMLUtil.doXMLParse(sendGET);
		String returnCode=(String) doXMLParse.get("running");
		if("false".equals(returnCode)) {
			String endMeeting = bbbApi.endMeeting(selectAllScreen.get(0).getRoom().getNum(), salt);
			HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/end",endMeeting);
		}
		/*
		 * if(randomname!=null) { servletContext.removeAttribute(randomname); }
		 */
	}

	public void teaLogout(String userId,int hour,int minute,int sec) {
		Teacher teacher=new Teacher();
		teacher.setId(userId);
		teacher=teacherService.selectTeacherById(teacher);
//		更新教师使用时长，通过':'分割字符串，分别计算秒、分钟、小时
		String duration = teacher.getDuration();
		
		teacher.setTimes(teacher.getTimes()+1);
		teacher.setDuration(countTime(duration,hour,minute,sec));
		teacherService.updateTeacherSelected(teacher);
	}
	
	public void stuLogout(String userId,int hour,int minute,int sec) {
		Student student=new Student();
		student.setId(userId);
		student=studentService.selectByPrimaryKey(student);
		String duration = student.getDuration();
		
		student.setTime(student.getTime()+1);
		student.setDuration(countTime(duration,hour,minute,sec));
		studentService.updateByPrimaryKeySelective(student);
	}
	
	public void screenLogout(String userId,int hour,int minute,int sec) {
		Screen screen=new Screen();
		screen.setId(userId);
		screen=screenService.selectByPrimaryKey(screen);
		int number=screen.getNumber();
		String duration = screen.getDuration();
		
		screen.setNumber(number+1);
		screen.setDuration(countTime(duration,hour,minute,sec));
		screenService.updateByPrimaryKeySelective(screen);
	}
	
	/**
	 * 用户与屏幕连接
	 * @param string
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@RequestMapping("/connectToScreen")
	public String connectToScreen(@RequestParam String usernameUser,@RequestParam String usernameScreen,
			HttpServletResponse response,HttpServletRequest request,ModelMap modelMap) throws Exception{
		ServletContext servletContext = request.getServletContext();
		/*modelMap.put("action", "create");
		modelMap.put("username", usernameUser);
		modelMap.put("usernameScreen", usernameScreen);*/
	/*	BASE64Encoder encoder = new BASE64Encoder();
		BASE64Decoder decoder = new BASE64Decoder();*/
		/*string=new String(decoder.decodeBuffer(string), "UTF-8");
		JSONObject fromObject = JSONObject.fromObject(string);*/
		//String usernameUser =fromObject.getString("usernameUser");
		//String sessionId=fromObject.getString("sessionId");
		//String usernameScreen =fromObject.getString("usernameScreen");
		//String password =fromObject.getString("password");
		HttpSession session=(HttpSession) servletContext.getAttribute(usernameUser);
		String joinMeetingParameters = null;
//		如果session中没有用户，则session失效，报1002
		if(session==null){
			/*Err err = new Err(1002,"浼氳瘽杩囨湡");
			String errString = JsonUtils.objectToJson(err);
			encode = encoder.encode(errString.getBytes());
			writer.write(encode);*/
			//return "redirect:"+httpUrl+"/demo/error.jsp";
		} else {

			// 通过用户名密码查询屏幕
			Screen screen = new Screen();
			screen.setUsername(usernameScreen);
			List<Screen> selectAllScreen = screenService.selectAllScreen(screen);
			// 如果没有查到，报1001
			if (selectAllScreen.size() <= 0) {
				/*Err err = new Err(1003, "屏幕不存在!");
				String errString = JsonUtils.objectToJson(err);
				encode = encoder.encode(errString.getBytes());
				writer.write(encode);*/
				return "redirect:"+httpUrl+"/demo/error.jsp";
			} else {
				screen = selectAllScreen.get(0);
				Room room = roomService.selectScreenByRoom(screen.getRoom());
				List<Screen> screenList = room.getScreenList();
				for (Screen scr : screenList) {
					if (scr.getUsername().equals(usernameScreen)) {
						continue;
					}
					scr.setRandomname(StringRandom.getStringRandom(3));
				}
				if (room != null) {
					/*
					 * String jsonRoom = JsonUtils.objectToJson(room); encode =
					 * encoder.encode(jsonRoom.getBytes());
					 */
					String meetingRunningParameters = bbbApi.isMeetingRunning(room.getNum(),salt);
					String sendRunning = HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/isMeetingRunning", meetingRunningParameters);
					logger.info(httpUrl + "/bigbluebutton/api/isMeetingRunning"+meetingRunningParameters);
					Map doXMLParse = XMLUtil.doXMLParse(sendRunning);
					String string = (String) doXMLParse.get("running");
					if("false".equals(string)) {
						// 新创建的房间
						String createMeetingParameters = bbbApi.createMeeting(room.getNum(),salt);
						String sendGET = HttpUtil.sendGET(httpUrl + "/bigbluebutton/api/create", createMeetingParameters);
						logger.info(httpUrl + "/bigbluebutton/api/create"+createMeetingParameters);
					}
				}

				// 更新记录中连接的屏幕ID
				int id = (int) session.getAttribute("recordId");
				Record record = new Record();
				record.setId(id);
				record.setScreenId(screen.getId());
				recordService.updateByPrimaryKeySelective(record);
				
				joinMeetingParameters=bbbApi.joinMeeting(room.getNum(),usernameUser,salt);
				logger.info(httpUrl+"/bigbluebutton/api/join?"+joinMeetingParameters);
				//HttpUtil.sendGET(BigBlueButtonURL+"api/join",joinMeetingParameters);
			}
		}
		
		return "redirect:"+httpUrl+"/bigbluebutton/api/join?"+joinMeetingParameters;
	}
	
	@RequestMapping("/uploadFileRecordUser")
	public void uploadFileRecordUser(@RequestParam(required=false) String fileName,
			@RequestParam(required=false) String username,
			@RequestParam(required=false) String absolutePath,HttpServletRequest request){
		ServletContext servletContext = request.getServletContext();
		HttpSession session=(HttpSession) servletContext.getAttribute(username);
		
		int id = (int) session.getAttribute("recordId");
		Record record = new Record();
		record.setId(id);
		record=recordService.selectByPrimaryKey(record);
		Screen screen=new Screen();
		screen.setId(record.getScreenId());
		List<Screen> selectAllScreen = screenService.selectAllScreen(screen);
		
		FileRecord file=new FileRecord();
		
		file.setRoomId(selectAllScreen.get(0).getRoom().getId());
		file.setFilename(fileName);
		file.setAbsolutePath(absolutePath);
		file.setUploadTime(new Date());
		Teacher teacher=(Teacher) session.getAttribute("teacher");
		if(teacher==null) {
			Student student = (Student) session.getAttribute("student");
			file.setUserId(student.getId());
		}else {
			file.setUserId(teacher.getId());
		}
		fileRecordService.insertSelective(file);
		session.setAttribute("fileRecord", file);
		
	}
	
	
	
	/**
	 * 断开连接
	 * @param usernameUser
	 * @param sessionId
	 * @param usernameScreen
	 * @param password
	 * @param response
	 * @param request
	 */
	@RequestMapping("/breakConnect")
	public void breakConnect(String usernameUser,String sessionId,String usernameScreen,
			String password,HttpServletResponse response,HttpServletRequest request){
		ServletContext servletContext = request.getServletContext();
		HttpSession session=(HttpSession) servletContext.getAttribute(usernameUser);
	}
	
	/**
	 * 增加用户的使用时长
	 * @param duration 原有时长
	 * @param hour 小时
	 * @param minute 分钟
	 * @param sec 秒
	 * @return
	 */
	public String countTime(String duration,int hour,int minute,int sec){
		
		String[] split = duration.split(":");
		sec=Integer.parseInt(split[2])+sec;
		if(sec>=60){
			minute+=(sec/60);
			sec%=60;
		}
		minute+=Integer.parseInt(split[1]);
		if(minute>=60){
			hour+=(minute/60);
			minute%=60;
		}
		hour+=Integer.parseInt(split[0]);
		return hour+":"+minute+":"+sec;
	}
}
