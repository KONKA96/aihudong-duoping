package com.controller;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.model.Room;
import com.model.Screen;
import com.service.AdminService;
import com.service.RoomService;
import com.service.ScreenService;
import com.util.ProduceId;
import com.util.ProduceUsername4;

import net.sf.json.JSONObject;
import sun.misc.BASE64Decoder;

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
	 */
	@RequestMapping("/toLogin")
	public String toLogin(HttpServletRequest request, HttpServletResponse response,HttpSession session) throws IOException {
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
//		base64解码
		BASE64Decoder decoder = new BASE64Decoder();
		String pwd=new String(decoder.decodeBuffer(admin.getPassword()), "UTF-8");
		pwd=new String(decoder.decodeBuffer(pwd), "UTF-8");
		try {
			admin.setPassword(pwd);
			admin = adminService.adminLogin(admin);
			
			logger.info(admin.getUsername()+"登录系统");
			session.setAttribute("admin", admin);
			return "success";
		} catch (UnknownAccountException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "none";
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			return "error";
		}
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
}
