package com.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Faculty;
import com.model.Logger;
import com.model.Record;
import com.model.Screen;
import com.model.Student;
import com.model.Subject;
import com.model.Teacher;
import com.service.AdminService;
import com.service.RecordService;
import com.service.ScreenService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.JsonDateValueProcessorUtil;
import com.util.JsonUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/**
 * 
 * @author KONKA
 *
 */
@Controller
@RequestMapping("/aiviews")
public class AiviewsController {
	
	protected Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private TeacherService teacherService;
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private ScreenService screenService;
	
	/**
	 * 远程接口管理员登录
	 * @author KONKA
	 * @param username
	 * @param session
	 */
	@ResponseBody
	@RequestMapping(value="/adminLogin",produces="text/json;charset=UTF-8")
	public void adminLogin(@RequestParam String username,HttpSession session) {
		Admin admin = new Admin();
		admin.setUsername(username);
		session.setAttribute("admin", admin);
		logger.info("爱视界管理员"+admin.getUsername()+"远程登录系统");
	}
	
	/**
	 * 远程接口查询图表数据
	 * @author KONKA
	 * @param category
	 * @param subjectId
	 * @param interval
	 * @param time
	 * @param bingfa
	 * @param facultyId
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getEcharts")
	public String getEcharts(@RequestParam(required=false,defaultValue="1") Integer category,
			@RequestParam(required=false)String subjectId,
			@RequestParam(required=false,defaultValue="7") String interval,String time,String bingfa,
			@RequestParam(required=false,defaultValue="1")String facultyId,HttpSession session,
			HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
//		不知道为啥，传过来的参数有的时候居然会带逗号，不信就把下面的if去掉
		if(interval.contains(",")){
			interval = interval.substring(0,interval.length() - 1);
		}
//		存放符合条件对象的id集合
		List<String> idList=new ArrayList<>();
//		区分不同的角色
//		1 ---》教师
//		4 ---》屏幕
//		2 ---》学生
//		获取使用时长的集合
		Map<String,Object> map=new HashMap<>();
		Admin admin = new Admin();
		admin.setPower(0);
		List<Teacher> teacherList=new ArrayList<>();
		if(1==category){
			Teacher teacher=new Teacher();
			
			teacherList = teacherService.selectAllTeacher(teacher);
			for (Teacher t : teacherList) {
				idList.add(t.getId());
			}
			map.put("interval", Integer.parseInt(interval));
		}else if(4==category){
			List<Screen> screenList = screenService.selectAllScreen(null);
			for (Screen screen : screenList) {
				idList.add(screen.getId());
			}
			map.put("interval", 1);
		}else if(2==category){
			map.put("interval", Integer.parseInt(interval));
			Map<String,Object> adminMap=new HashMap<>();
			
			List<Student> studentList = studentService.selectAllStudent(null);
				
			for (Student student : studentList) {
				idList.add(student.getId());
			}
		}
		

		map.put("role", category);
		map.put("userId", idList);
		
		if(time!=null&&time!=""){
			map.put("time", time);
		}
		Map<String, Integer> recordMap=new LinkedHashMap<>();
		
		
//		向图表中设置字段和值的集合
		List<String> xAxisData = new ArrayList<String>();  
        List< JSONObject> seriesList = new ArrayList< JSONObject>(); 
        
//      表格中字段的单位
        String type="";
		if("2".equals(bingfa)){
			map.put("bingfa", bingfa);
			recordMap=recordService.selectBingfa(map);
			Set<Entry<String, Integer>> set = recordMap.entrySet();
			for (Entry<String, Integer> entry : set) {
				xAxisData.add(entry.getKey());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("hour", entry.getValue());
				seriesList.add(jsonObject);
			}
			type="次";
		}else{
			recordMap = recordService.selectRecord(map);
			Set<Entry<String, Integer>> set = recordMap.entrySet();
			for (Entry<String, Integer> entry : set) {
				Integer hour = entry.getValue();
				if(hour!=null){
					String hourString = hour.toString();
					if(hourString.length()>4){
						String substring = hourString.substring(0, hourString.length()-4);
						hour=Integer.parseInt(substring);
					}else{
						hour=1;
					}
				}else{
					hour=0;
				}
				entry.setValue(hour);
				xAxisData.add(entry.getKey());
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("hour", entry.getValue());
				seriesList.add(jsonObject);
			}
			type="h";
		}
		Collections.reverse(seriesList);
		Collections.reverse(xAxisData);
		//xAxisData和seriesList转为json 
		JSONObject jsonObject1 = new JSONObject();  
		   
		jsonObject1.put("xAxisData", xAxisData);  
		   
		jsonObject1.put("seriesList", seriesList);
		jsonObject1.put("type", type);
		return jsonObject1.toString();
	}
	
	/**
	 * 查询用产品时长最多的前几位教师
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getTeacherOrderByTime",produces = "text/json;charset=UTF-8")
	public String getTeacherOrderByTime(HttpSession session,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Record record=new Record();
		record.setRole(1);
		Map<String,Object> adminMap=new HashMap<>();
		adminMap.put("role", record.getRole());
		List<Map<String,Object>> teacherRecordList = recordService.selectTeacherOrderByTime(adminMap);
		for (int i = 0; i < teacherRecordList.size(); i++) {
			Map<String, Object> map = teacherRecordList.get(0);
			teacherRecordList.remove(0);
			Object object = map.get("time");
			String time = "";
			if(object==null) {
				time="1";
			}else {
				time=object.toString(); 
//				截字符串 00:00:00 只获取小时，不足一小时的按一小时算，没有用过的记0
				if(Integer.parseInt(time)>Integer.valueOf(3600)){
					Integer hourTime = Integer.parseInt(time)/3600;
					time=hourTime.toString();
				}else if(time.length()>0){
					time="1";
				}else{
					time="0";
				}
			}
			map.put("time", time);
			teacherRecordList.add(map);
		}
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("teacherRecordList", teacherRecordList);
		return jsonObject.toString();
	}
	
	/**
	 * 查询用产品时长最多的前几名学生
	 * @param session
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getStudentOrderByTime",produces = "text/json;charset=UTF-8")
	public String getStudentOrderByTime(HttpSession session,HttpServletResponse response){
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Record record=new Record();
		record.setRole(3);
		Map<String,Object> adminMap=new HashMap<>();
		adminMap.put("role", record.getRole());
		List<Map<String,Object>> studentRecordList = recordService.selectStudentOrderByTime(adminMap);
		for (int i = 0; i < studentRecordList.size(); i++) {
			Map<String, Object> map = studentRecordList.get(0);
			studentRecordList.remove(0);
			Object object = map.get("time");
			String time=object.toString(); 
			if(time.length()>=4){
				time=time.substring(0, time.length()-4);
			}else if(time.length()>0){
				time="1";
			}else{
				time="0";
			}
			map.put("time", time);
			studentRecordList.add(map);
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("studentRecordList", studentRecordList);
		return jsonObject.toString();
	}
	
	/**
	 * 获取学生、教师、屏幕数量
	 * @author KONKA
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserNum",produces="text/json;charset=UTF-8")
	public String getUserNum(HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		List<String> teaIdList = teacherService.selectAllId();
		List<String> stuIdList = studentService.selectAllId();
		List<String> scrIdList = screenService.selectAllId();
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("teacherNum", teaIdList.size());
		jsonObject.put("studentNum", stuIdList.size());
		jsonObject.put("screenNum", scrIdList.size());
		return jsonObject.toString();
	}
	
	/**
	 * 远程接口查询教师信息
	 * @param teacher
	 * @param index
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectAllTeacher",produces="text/json;charset=UTF-8")
	public String selectAllTeacher(Teacher teacher,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize) {
		PageHelper.startPage(index, pageSize);
		Page<Teacher> teacherList = (Page<Teacher>) teacherService.selectAllTeacher(teacher);
		Integer end;
		Integer total;
		Integer totalCount;
		Integer start;
		
		if(index<=0){
			index=1;
		}
		//获取末页数
		end=(int) teacherList.getPages();
		//如果要跳转的页数大于末页，则跳转末页
		if(index>=end){
			index=end;
			total=(int) teacherList.getTotal();
		}else{
			total=index*pageSize;
		}
		//该页起始项的索引
		start=(index-1)*pageSize+1;
		//获取总的信息数目
		totalCount=(int) teacherList.getTotal();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("start", start);
		jsonObject.put("total", total);
		jsonObject.put("totalCount", totalCount);
		jsonObject.put("end", end);
		jsonObject.put("teacherList", teacherList);
		
		return jsonObject.toString();
	}
	
	/**
	 * 远程接口查询学生信息
	 * @param student
	 * @param index
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectAllStudent",produces="text/json;charset=UTF-8")
	public String selectAllStudent(Student student,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize) {
		PageHelper.startPage(index, pageSize);
		Page<Student> studentList =(Page<Student>) studentService.selectAllStudent(student);
		
		Integer end;
		Integer total;
		Integer totalCount;
		Integer start;
		
		if(index<=0){
			index=1;
		}
		//获取末页数
		end=(int) studentList.getPages();
		//如果要跳转的页数大于末页，则跳转末页
		if(index>=end){
			index=end;
			total=(int) studentList.getTotal();
		}else{
			total=index*pageSize;
		}
		//该页起始项的索引
		start=(index-1)*pageSize+1;
		//获取总的信息数目
		totalCount=(int) studentList.getTotal();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("start", start);
		jsonObject.put("total", total);
		jsonObject.put("totalCount", totalCount);
		jsonObject.put("end", end);
		jsonObject.put("studentList", studentList);
			
		return jsonObject.toString();
	}
	/**
	 * 远程接口查询记录信息
	 * @param record
	 * @param index
	 * @param pageSize
	 * @param role
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="/selectAllRecord",produces="text/json;charset=UTF-8")
	public String selectAllRecord(Record record,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,
            @RequestParam(required=false,defaultValue="1") Integer role) throws ParseException {
		PageHelper.startPage(index, pageSize);
		Page<Record> recordList = null;
		
		if(1==role) {
			recordList = (Page<Record>) recordService.selectTeacherRecord(record);
		}else if(2==role) {
			recordList = (Page<Record>) recordService.selectStudentRecord(record);
		}else if(4==role) {
			recordList = (Page<Record>) recordService.selectScreenRecord(record);
		}
		
		//SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Integer end;
		Integer total;
		Integer totalCount;
		Integer start;
		
		if(index<=0){
			index=1;
		}
		//获取末页数
		end=(int) recordList.getPages();
		//如果要跳转的页数大于末页，则跳转末页
		if(index>=end){
			index=end;
			total=(int) recordList.getTotal();
		}else{
			total=index*pageSize;
		}
		//该页起始项的索引
		start=(index-1)*pageSize+1;
		//获取总的信息数目
		totalCount=(int) recordList.getTotal();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("start", start);
		jsonObject.put("total", total);
		jsonObject.put("totalCount", totalCount);
		jsonObject.put("end", end);
		
		
		JsonConfig jsonConfig = new JsonConfig();                                                          
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonDateValueProcessorUtil());               
		JSONArray array = JSONArray.fromObject(recordList,jsonConfig);
		jsonObject.put("recordList", array);
		return jsonObject.toString();
	}
	
	/**
	 * 远程接口查询管理员信息
	 * @param admin
	 * @param index
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectAllAdmin",produces="text/json;charset=UTF-8")
	public String selectAllAdmin(Admin admin,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize) {
		PageHelper.startPage(index, pageSize);
		Page<Admin> adminList = (Page<Admin>) adminService.selectAllAdmin(admin);
		
		Integer end;
		Integer total;
		Integer totalCount;
		Integer start;
		
		if(index<=0){
			index=1;
		}
		//获取末页数
		end=(int) adminList.getPages();
		//如果要跳转的页数大于末页，则跳转末页
		if(index>=end){
			index=end;
			total=(int) adminList.getTotal();
		}else{
			total=index*pageSize;
		}
		//该页起始项的索引
		start=(index-1)*pageSize+1;
		//获取总的信息数目
		totalCount=(int) adminList.getTotal();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("start", start);
		jsonObject.put("total", total);
		jsonObject.put("totalCount", totalCount);
		jsonObject.put("end", end);
		jsonObject.put("adminList", adminList);
		return jsonObject.toString();
	}
	
	/**
	 * 远程接口查询屏幕信息
	 * @param screen
	 * @param index
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/selectAllScreen",produces="text/json;charset=UTF-8")
	public String selectAllScreen(Screen screen,@RequestParam(required=false,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize) {
		
		PageHelper.startPage(index, pageSize);
		Page<Screen> screenList = (Page<Screen>) screenService.selectAllScreen(screen);
		Integer end;
		Integer total;
		Integer totalCount;
		Integer start;
		
		if(index<=0){
			index=1;
		}
		//获取末页数
		end=(int) screenList.getPages();
		//如果要跳转的页数大于末页，则跳转末页
		if(index>=end){
			index=end;
			total=(int) screenList.getTotal();
		}else{
			total=index*pageSize;
		}
		//该页起始项的索引
		start=(index-1)*pageSize+1;
		//获取总的信息数目
		totalCount=(int) screenList.getTotal();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("start", start);
		jsonObject.put("total", total);
		jsonObject.put("totalCount", totalCount);
		jsonObject.put("end", end);
		jsonObject.put("screenList", screenList);
		return jsonObject.toString();
	}
	
	/**
	 * 获取在线人数
	 * @param role
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getOnlineNum",produces="text/json;charset=UTF-8")
	public String getOnlineNum(String role,HttpServletRequest request,HttpServletResponse response) {
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		ServletContext servletContext = request.getServletContext();
		Object count=null;
		if("1".equals(role)) {
			count=servletContext.getAttribute("tcount");
		}else if("2".equals(role)) {
			count=servletContext.getAttribute("scount");
		}
		return JsonUtils.objectToJson(count);
	}
	
	/**
	 * 获取文件列表
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getFileList",produces="text/json;charset=UTF-8")
	public String getFileList(HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/");
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		
		File file = new File(realPath + "/" + "logs");
		File[] listFiles = file.listFiles();
		List<String> list = new ArrayList<>();
		for (File f : listFiles) {
			list.add(f.getName());
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("fileList", list);
		return jsonObject.toString();
	}
	
	/**
	 * 远程接口传输文件
	 * @param request
	 * @param fileName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/testFile",produces="text/json;charset=UTF-8")
	public String testFile(HttpServletRequest request,String fileName) {
		ServletContext servletContext = request.getServletContext();
		String realPath = servletContext.getRealPath("/");
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		realPath = realPath.substring(0, realPath.lastIndexOf("/"));
		
		JSONObject jsonObject = new JSONObject();
		try {
			File file = new File(realPath + "/" + "logs" + "/" + fileName);

			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			List<String> list = new ArrayList<String>();
			
			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				if (str.trim().length() > 2) {
					list.add(str);
				}
			}
			
			jsonObject.put("title", fileName);
			jsonObject.put("content", list);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
