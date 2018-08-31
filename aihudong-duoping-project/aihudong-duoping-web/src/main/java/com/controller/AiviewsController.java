package com.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Admin;
import com.model.Record;
import com.model.Screen;
import com.model.Student;
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

@Controller
@RequestMapping("/aiviews")
public class AiviewsController {

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
		
		if(role==1) {
			recordList = (Page<Record>) recordService.selectTeacherRecord(record);
		}else if(role==2) {
			recordList = (Page<Record>) recordService.selectStudentRecord(record);
		}else if(role==4) {
			recordList = (Page<Record>) recordService.selectScreenRecord(record);
		}
		
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
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
	public String getOnlineNum(String role,HttpServletRequest request) {
		ServletContext servletContext = request.getServletContext();
		Object count=null;
		if(role.equals("1")) {
			count=servletContext.getAttribute("tcount");
		}else if(role.equals("2")) {
			count=servletContext.getAttribute("scount");
		}
		return JsonUtils.objectToJson(count);
	}
	
	@RequestMapping("/testFile")
	public String testFile() {
		return "";
	}
}
