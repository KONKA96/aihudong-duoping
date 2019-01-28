package com.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.model.Logger;
import com.model.Record;
import com.model.Room;
import com.model.Screen;
import com.model.Student;
import com.model.Teacher;
import com.service.AdminService;
import com.service.RecordService;
import com.service.RoomService;
import com.service.ScreenService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.ADUserUtils;
import com.util.JsonUtils;
import com.util.OSUtils;
import com.util.ProduceId;
import com.util.ProduceUsername4;
import com.util.TestSha1;

import net.sf.json.JSONObject;
import sun.misc.BASE64Encoder;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/login")
public class test {
	protected Logger logger = Logger.getLogger(this.getClass());

	@Value("${defaultPwd}")
	private String defaultPwd;
	@Value("${virtualRoomSwitch}")
	private boolean virtualRoomSwitch;
	@Value("${checkTokenUrl}")
	private String checkTokenUrl;

	@Autowired
	private ScreenService screenService;
	@Autowired
	private RoomService roomService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private RecordService recordService;

	@Autowired
	AdminService adminService;

	@ResponseBody
	@RequestMapping("/testjsp")
	public String testjsp() {
		
		ADUserUtils ad = new ADUserUtils();
		
		return ad.testConnect();
	}

	/**
	 * 跳转到登录界面
	 * 
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(required = false) String token, @RequestParam(required = false) String tenant,
			@RequestParam(required = false) String user, @RequestParam(required = false) String sign)
			throws IOException, NoSuchAlgorithmException {

		// 化大成教对接单点
		/*if (token != null && tenant != null && user != null) {
			String testSha1 = TestSha1.testSha1(token, tenant, user);
			if (sign != null && sign.equals(testSha1)) {
				String url = "http://celjwmanager.buct.edu.cn/openapi/ssoservice";
				Map<String, Object> params = new HashMap<>();
				params.put("tenant", tenant);
				params.put("method", "checkToken");
				HashMap<String, String> data = new HashMap<>();
				data.put("token", token);
				data.put("tenant", tenant);
				data.put("sign", sign);
				data.put("appId", "hgdx90001");

				params.put("data", data);
				//String sendPost = sendPost(url, params);
				//System.out.println(sendPost);
			}
		}*/

		/*
		 * Admin admin = new Admin();
		 * 
		 * String uid = request.getRemoteUser();// 获取登录用户id admin.setUsername(uid);
		 * 
		 * admin = adminService.adminLogin(admin); if(admin!=null) {
		 * session.setAttribute("admin", admin); return "redirect:/admin/test"; }
		 */
		return "login";
		// return "redirect:/admin/test";
	}

	/**
	 * 化大对接单点
	 * 
	 * @author KONKA
	 * @param token  登录标识
	 * @param tenant 用户标识
	 * @param sign   加密串
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException 
	 * @throws NamingException 
	 */
	@RequestMapping(value = "/checkToken")
	public String checkToken(@RequestParam(required = false) String token,
			@RequestParam(required = false) String tenant, @RequestParam(required = false) String sign,
			@RequestParam(required = false) String user) throws NoSuchAlgorithmException, IOException, NamingException {
		Map<String, Object> argMap = new HashMap<>();
		String param2 = "";
		if (token != null && tenant != null && user != null) {
			String testSha1 = TestSha1.testSha1( tenant, user);
			if (sign != null && sign.equals(testSha1)) {
				JSONObject params = new JSONObject();
				params.put("tenant", tenant);
				params.put("method", "checkToken");
				Map<String, String> data = new TreeMap<>();
				data.put("token", token);
				data.put("tenant", tenant);
				data.put("sign", sign);
				params.put("data", data);
				
				String param = "Jsondata=" + URLEncoder.encode(params.toString(), "UTF-8");
				String sendPost = sendPost(checkTokenUrl, param);
				System.out.println(sendPost);
				JSONObject jsonObject = JSONObject.fromObject(sendPost);
				String result = jsonObject.getString("result");
				String data1 = jsonObject.getString("data");
				
				if("1".equals(result)&&"true".equals(data1)) {
					
					Teacher teacher = new Teacher();
					teacher.setUsername(user);

					teacher = teacherService.teacherLogin(teacher);

					Student student = new Student();
					student.setUsername(user);

					student = studentService.studentLogin(student);
					
					if (teacher != null && teacher.getId() != null) {
						param2 = param2+"?username="+teacher.getUsername();
					}
					else if (student != null && student.getId() != null) {
						param2 = param2+"?username="+teacher.getUsername();
					}
					else {
						ADUserUtils utils = new ADUserUtils();
				        
				        SearchResult sr = utils.searchByUserName(utils.root, user);
						
						teacher = new Teacher();
						List<String> idList = teacherService.selectAllId();
						String newId=null;
						if(idList.size()==0){
//							如果表内没有数据，手动生成id
							newId="te1";
						}else{
							newId=ProduceId.produceUserId(idList);
						}
						if(newId!=null){
							teacher.setId(newId);
						}
						teacher.setUsername(user);
						teacher.setPassword(defaultPwd);
						teacher.setTruename(sr.getAttributes().get("givenName").get(0).toString());
						teacherService.insertTeacherSelected(teacher, virtualRoomSwitch);
						param2 = param2+"?username="+teacher.getUsername();
					}
					
					/*argMap.put("result", "1");
					argMap.put("message", "成功");
					argMap.put("data", "true");*/
				} else {
					argMap.put("result", "0");
					argMap.put("message", "token验证失败");
					argMap.put("data", "false");
				}
				
			} else {
				argMap.put("result", "0");
				argMap.put("message", "签证不匹配");
				argMap.put("data", "false");
			}
		}
		return "redirect:https://celzxkt.buct.edu.cn/html5client/login"+param2;
	}

	/**
	 * 对接金智单点登录
	 * 
	 * @author KONKA
	 * @param request
	 * @return
	 */
	@RequestMapping("/jinzhiLogin")
	public String jinzhiLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ServletContext servletContext = request.getServletContext();
		String username = request.getRemoteUser();// 获取登录用户id

		if (username == null) {
			// 用户名为空
		}

		Teacher teacher = new Teacher();
		teacher.setUsername(username);

		teacher = teacherService.teacherLogin(teacher);

		Student student = new Student();
		student.setUsername(username);

		student = studentService.studentLogin(student);

		// 记录对象
		Record record = new Record();
		// 返回参数
		Map<String, Object> argMap = new HashMap<>();
		// 登录者为教师对象
		if (teacher != null && teacher.getId() != null) {
			session.setAttribute("teacher", teacher);

			servletContext.setAttribute(teacher.getUsername(), session);
			String sessionId = session.getId();
			teacher.setSessionId(sessionId);

			teacher.setRole(1);

			record.setUserId(teacher.getId());
			record.setRole(Integer.valueOf(1));
			argMap.put("role", Integer.valueOf(1));
			argMap.put("truename", teacher.getTruename());

			// 统计在线人数
			Object tcount = servletContext.getAttribute("tcount");
			if (tcount == null) {
				servletContext.setAttribute("tcount", 1);
			} else {
				servletContext.setAttribute("tcount", Integer.parseInt(tcount.toString()) + 1);
			}
		}
		// 登陆者为学生对象
		if (student != null && student.getId() != null) {
			session.setAttribute("student", student);
			servletContext.setAttribute(student.getUsername(), session);
			student.setRole(2);
			String sessionId = session.getId();
			student.setSessionId(sessionId);

			argMap.put("role", Integer.valueOf(2));
			argMap.put("truename", student.getTruename());
			record.setUserId(student.getId());
			record.setRole(Integer.valueOf(2));
			// 统计在线人数
			Object scount = servletContext.getAttribute("scount");
			if (scount == null) {
				servletContext.setAttribute("scount", 1);
			} else {
				servletContext.setAttribute("scount", Integer.parseInt(scount.toString()) + 1);
			}
		}

		session.setAttribute("count", Integer.valueOf(0));

		session.setAttribute("startTime", new Date());

		record.setStartTime(new Date());
		this.recordService.insertSelective(record);

		session.setAttribute("recordId", record.getId());
		session.setAttribute("startTime", record.getStartTime());
		session.setAttribute("role", record.getRole());
		session.setAttribute("userId", record.getUserId());
		argMap.put("code", Integer.valueOf(200));
		// argMap.put("serverhost", serverhost);

		return "";
	}

	/**
	 * 管理员登录
	 * 
	 * @param admin
	 * @param session
	 * @return
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@ResponseBody
	@RequestMapping("/adminLogin")
	public String adminLogin(Admin admin, HttpSession session) throws UnsupportedEncodingException, IOException {
		/*
		 * Map<String,Object> map = new HashMap<>(); UsernamePasswordToken token = new
		 * UsernamePasswordToken(admin.getUsername(), new Md5Hash(admin.getPassword(),
		 * admin.getUsername() ,2).toString()); Subject subject =
		 * SecurityUtils.getSubject(); subject.login(token);
		 */
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		String password = new String(encoder.encode(admin.getPassword().getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		admin = adminService.adminLogin(admin);
		if (admin == null || admin.getId() == null) {
			logger.info("登录失败！");
			return "none";
		}
		if (!admin.getPassword().equals(password)) {
			logger.info("登录失败！");
			return "error";
		}
		logger.info(admin.getUsername() + "登录系统");
		session.setAttribute("admin", admin);
		return "success";
	}

	@RequestMapping("/testLunbo")
	public String testLunbo() {
		return "/lunbomessage";
	}

	/*
	 * @RequestMapping("/insertScreen") public String insertScreen() { for (int i =
	 * 30; i < 100; i++) { Screen screen =new Screen(); screen.setId("sc"+i);
	 * screen.setUsername("00"+i); screen.setPassword("123"); screen.setAdminId(1);
	 * screen.setType("1");
	 * screen.setRoomId("e4f76ac3-59eb-4437-a557-2c2f62d99714");
	 * screenService.insertSelective(screen); } return ""; }
	 * 
	 * @RequestMapping("/deleteScreen") public String deleteScreen() { for (int i =
	 * 30; i < 100; i++) { Screen screen =new Screen(); screen.setId("sc"+i);
	 * screenService.deleteByPrimaryKey(screen); } return ""; }
	 */

	/**
	 * 新增房间、屏幕（接口）
	 * 
	 * @param roomNum   需要生成的房间数量
	 * @param screenNum 需要生成的屏幕数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addScreenSelected")
	public String addScreenSelected(@RequestParam String roomNum, @RequestParam String screenNum) {
		List<Room> roomList = new ArrayList<>();

		List<String> usernameList = screenService.selectAllUsername();
		for (int i = 0; i < Integer.parseInt(roomNum); i++) {
			List<String> selectAllId = roomService.selectAllId();

			Room room = new Room();
			while (true) {
				String roomName = getRandom(1000, 9999);
				if (!selectAllId.contains(roomName)) {
					room.setId(roomName);
					room.setNum(roomName);
					room.setBuildingId(2);

					roomService.insertSelective(room);

					List<Screen> screenList = new ArrayList<>();

					List<String> usernameNewList = ProduceUsername4.produceScreenUsername(usernameList,
							Integer.parseInt(screenNum));
					List<String> screenIdList = screenService.selectAllId();
					for (String usernameScreen : usernameNewList) {
						Screen screen = new Screen();
						String newId = ProduceId.produceUserId(screenIdList);
						screenIdList.add(newId);
						screen.setId(newId);
						screen.setUsername(usernameScreen);
						usernameList.add(usernameScreen);
						screen.setPassword("123");

						screen.setRoomId(roomName);
						screen.setAdminId(1);
						screen.setType("1");

						screenList.add(screen);

						screenService.insertSelective(screen);
					}

					room.setScreenList(screenList);

					roomList.add(room);

					break;
				}
			}

		}

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("roomList", roomList);

		return jsonObject.toString();
	}

	/**
	 * 获取一定范围内的随机数
	 * 
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static String getRandom(int min, int max) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return String.valueOf(s);

	}

	public String sendPost(String urlPath, String param) throws IOException {
		// 建立连接
		URL url = new URL(urlPath);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();

		// 设置参数
		httpConn.setDoOutput(true); // 需要输出
		httpConn.setDoInput(true); // 需要输入
		httpConn.setUseCaches(false); // 不允许缓存
		httpConn.setRequestMethod("POST"); // 设置POST方式连接

		// 设置请求属性
		httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
		httpConn.setRequestProperty("Charset", "UTF-8");

		// 连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
		httpConn.connect();

		// 建立输入流，向指向的URL传入参数
		DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
		dos.writeBytes(param);
		dos.flush();
		dos.close();

		// 获得响应状态
		int resultCode = httpConn.getResponseCode();
		StringBuffer sb = new StringBuffer();
		if (HttpURLConnection.HTTP_OK == resultCode) {
			
			String readLine = new String();

			BufferedReader responseReader = new BufferedReader(
					new InputStreamReader(httpConn.getInputStream(), "UTF-8"));
			while ((readLine = responseReader.readLine()) != null) {
				sb.append(readLine).append("\n");
			}
			responseReader.close();

		}
		return sb.toString();
	}

	@ResponseBody
	@RequestMapping(value = "/testCpu", produces = { "text/json;charset=UTF-8" })
	public String testCpu() {
		Map<String, Object> argMap = new HashMap<>();

		Map<?, ?> cpuinfo = OSUtils.cpuinfo();
		argMap.put("cpuinfo", cpuinfo);
		int cpuUsage = OSUtils.cpuUsage();
		argMap.put("cpuUsage", cpuUsage);
		int disk = OSUtils.disk();
		argMap.put("disk", disk);
		int memoryUsage = OSUtils.memoryUsage();
		argMap.put("memoryUsage", memoryUsage);
		return JsonUtils.objectToJson(argMap);
	}
}
