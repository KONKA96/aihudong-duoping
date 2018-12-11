package com.controller;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
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
import com.util.JsonUtils;
import com.util.OSUtils;
import com.util.ProduceId;
import com.util.ProduceUsername4;
import com.util.TestSha1;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;
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
	
	@Value("${httpUrl}")
	private String httpUrl;
	@Value("${salt}")
	private String salt;
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
	 * @throws IOException 
	 * @throws NoSuchAlgorithmException 
	 */
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session,
			@RequestParam(required = false) String token,@RequestParam(required = false) String tenant,
			@RequestParam(required = false) String user,@RequestParam(required = false) String sign) throws IOException, NoSuchAlgorithmException {
		
		//化大成教对接单点
		if(token!=null && tenant!=null && user!=null) {
			String testSha1 = TestSha1.testSha1(token,tenant,user);
			if(sign!=null && sign.equals(testSha1)) {
				String url = "http://celjwmanager.buct.edu.cn/openapi/ssoservice";
				Map<String,Object> params = new HashMap<>();
				params.put("tenant", tenant);
				params.put("method", "checkToken");
				HashMap<String, String> data = new HashMap<>();
				data.put("token", token);
				data.put("tenant", tenant);
				data.put("sign", sign);
				data.put("appId", "hgdx90001");
				
				params.put("data", data);
				String sendPost = sendPost(url, params);
				System.out.println(sendPost);
			}
		}
		
		/*Admin admin = new Admin();
		
		String uid = request.getRemoteUser();// 获取登录用户id
		admin.setUsername(uid);
		
		admin = adminService.adminLogin(admin);
		if(admin!=null) {
			session.setAttribute("admin", admin);
			return "redirect:/admin/test";
		}*/
		return "login";
		//return "redirect:/admin/test";
	}
	
	/**
	 * 对接金智单点登录
	 * @author KONKA
	 * @param request
	 * @return
	 */
	@RequestMapping("/jinzhiLogin")
	public String jinzhiLogin(HttpServletRequest request) {
		HttpSession session = request.getSession();
		ServletContext servletContext = request.getServletContext();
		String username = request.getRemoteUser();// 获取登录用户id
		
		if(username==null) {
			//用户名为空
		}
		
		Teacher teacher = new Teacher();
		teacher.setUsername(username);

		teacher = teacherService.teacherLogin(teacher);

		Student student = new Student();
		student.setUsername(username);

		student = studentService.studentLogin(student);
		
		//记录对象
		Record record = new Record();
		//返回参数
		Map<String, Object> argMap = new HashMap<>();
		//登录者为教师对象
		if(teacher!=null && teacher.getId()!=null) {
			session.setAttribute("teacher", teacher);

			servletContext.setAttribute(teacher.getUsername(), session);
			String sessionId = session.getId();
			teacher.setSessionId(sessionId);

			teacher.setRole(1);

			record.setUserId(teacher.getId());
			record.setRole(Integer.valueOf(1));
			argMap.put("role", Integer.valueOf(1));
			argMap.put("truename", teacher.getTruename());
			
			//统计在线人数
			Object tcount = servletContext.getAttribute("tcount");
			if(tcount==null) {
				servletContext.setAttribute("tcount", 1);
			}else {
				servletContext.setAttribute("tcount", Integer.parseInt(tcount.toString())+1);
			}
		}
		//登陆者为学生对象
		if(student!=null && student.getId()!=null) {
			session.setAttribute("student", student);
			servletContext.setAttribute(student.getUsername(), session);
			student.setRole(2);
			String sessionId = session.getId();
			student.setSessionId(sessionId);

			argMap.put("role", Integer.valueOf(2));
			argMap.put("truename", student.getTruename());
			record.setUserId(student.getId());
			record.setRole(Integer.valueOf(2));
			//统计在线人数
			Object scount = servletContext.getAttribute("scount");
			if(scount==null) {
				servletContext.setAttribute("scount", 1);
			}else {
				servletContext.setAttribute("scount", Integer.parseInt(scount.toString())+1);
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
		//argMap.put("serverhost", serverhost);
		
		return "";
	}
	
	/**
	 * 管理员登录
	 * @param admin
	 * @param session
	 * @return
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping("/adminLogin")
	public String adminLogin(Admin admin,HttpSession session) throws UnsupportedEncodingException, IOException{
		/*Map<String,Object> map = new HashMap<>();
		UsernamePasswordToken token = new UsernamePasswordToken(admin.getUsername(),
				new Md5Hash(admin.getPassword(), admin.getUsername() ,2).toString());
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		*/
//		base64转码
		BASE64Encoder encoder = new BASE64Encoder();
		String password = new String(encoder.encode(admin.getPassword().getBytes()));
		password = new String(encoder.encode(password.getBytes()));
		admin = adminService.adminLogin(admin);
		if(admin==null || admin.getId()==null) {
			logger.info("登录失败！");
			return "none";
		}
		if(!admin.getPassword().equals(password)) {
			logger.info("登录失败！");
			return "error";
		}
		logger.info(admin.getUsername()+"登录系统");
		session.setAttribute("admin", admin);
		return "success";
	}
	
	@RequestMapping("/testLunbo")
	public String testLunbo() {
		return "/lunbomessage";
	}
	
	/*@RequestMapping("/insertScreen")
	public String insertScreen() {
		for (int i = 30; i < 100; i++) {
			Screen screen =new Screen();
			screen.setId("sc"+i);
			screen.setUsername("00"+i);
			screen.setPassword("123");
			screen.setAdminId(1);
			screen.setType("1");
			screen.setRoomId("e4f76ac3-59eb-4437-a557-2c2f62d99714");
			screenService.insertSelective(screen);
		}
		return "";
	}
	
	@RequestMapping("/deleteScreen")
	public String deleteScreen() {
		for (int i = 30; i < 100; i++) {
			Screen screen =new Screen();
			screen.setId("sc"+i);
			screenService.deleteByPrimaryKey(screen);
		}
		return "";
	}*/
	
	/**
	 * 新增房间、屏幕（接口）
	 * @param roomNum 需要生成的房间数量
	 * @param screenNum 需要生成的屏幕数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/addScreenSelected")
	public String addScreenSelected(@RequestParam String roomNum,@RequestParam String screenNum) {
		List<Room> roomList=new ArrayList<>();
		
		List<String> usernameList = screenService.selectAllUsername();
		for(int i=0;i<Integer.parseInt(roomNum);i++) {
			List<String> selectAllId = roomService.selectAllId();
			
			Room room=new Room();
			while(true) {
				String roomName = getRandom(1000,9999);
				if(!selectAllId.contains(roomName)) {
					room.setId(roomName);
					room.setNum(roomName);
					room.setBuildingId(2);
					
					roomService.insertSelective(room);
					
					List<Screen> screenList=new ArrayList<>();
					
					
			    	
			    	List<String> usernameNewList=ProduceUsername4.produceScreenUsername(usernameList, Integer.parseInt(screenNum));
			    	List<String> screenIdList = screenService.selectAllId();
					for (String usernameScreen : usernameNewList) {
						Screen screen=new Screen();
						String newId=ProduceId.produceUserId(screenIdList);
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
		
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("roomList", roomList);
		
		return jsonObject.toString();
	}
	
	/**
	 * 获取一定范围内的随机数
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static String getRandom(int min, int max){
	    Random random = new Random();
	    int s = random.nextInt(max) % (max - min + 1) + min;
	    return String.valueOf(s);

	}
	
	public String sendPost(String url, Map<String, Object> params) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn =(HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // POST方法
            conn.setRequestMethod("POST");
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            if (params != null) {
                StringBuilder param = new StringBuilder();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if(param.length()>0){
                        param.append("&");
                    }
                    param.append(entry.getKey());
                    param.append("=");
                    param.append(entry.getValue());
                    //System.out.println(entry.getKey()+":"+entry.getValue());
                }
                //System.out.println("param:"+param.toString());
                out.write(param.toString());
            }
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result.toString();
    }
	
	@ResponseBody
	@RequestMapping(value="/testCpu",produces = { "text/json;charset=UTF-8" })
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
