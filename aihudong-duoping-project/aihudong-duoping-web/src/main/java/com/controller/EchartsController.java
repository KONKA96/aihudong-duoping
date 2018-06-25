package com.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.model.Admin;
import com.model.Faculty;
import com.model.Screen;
import com.model.Student;
import com.model.Subject;
import com.model.Teacher;
import com.service.AdminService;
import com.service.FacultyService;
import com.service.RecordService;
import com.service.ScreenService;
import com.service.TeacherService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/echarts")
public class EchartsController {
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private ScreenService screenService;
	@Autowired
	private FacultyService facultyService;
	@Autowired
	private AdminService adminService;
	
	/**
	 * 传递给登录后首页的折线图数据
	 * @param category 教师、屏幕、学生
	 * @param subject 专业
	 * @param interval 间隔时长
	 * @param time 日期
	 * @param bingfa 并发数分类和时长分类
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/getEchartsData",produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String getEchartsData(@RequestParam(required=true,defaultValue="1") Integer category,@RequestParam(required=false)String subjectId,
			@RequestParam(required=true,defaultValue="7") String interval,String time,String bingfa,
			@RequestParam(required=false,defaultValue="1")String facultyId,HttpSession session){
//		不知道为啥，传过来的参数有的时候居然会带逗号，不信就把下面的if去掉
		if(interval.contains(",")){
			interval = interval.substring(0,interval.length() - 1);
		}
//		按系查询
		Faculty facultyOld=null;
		if(subjectId==""&&facultyId!=null){
			Faculty faculty=new Faculty();
			faculty.setId(Integer.parseInt(facultyId));
			List<Faculty> facultyList = facultyService.selectAllFaculty(faculty);
			facultyOld=facultyList.get(0);
		}
//		存放符合条件对象的id集合
		List<String> idList=new ArrayList<>();
//		区分不同的角色
//		1 ---》教师
//		4 ---》屏幕
//		2 ---》学生
//		获取使用时长的集合
		Map<String,Object> map=new HashMap<>();
		Admin admin=(Admin) session.getAttribute("admin");
		List<Teacher> teacherList=new ArrayList<>();
		if(1==category){
			if(facultyOld!=null){
				for (Subject sub : facultyOld.getSubjectList()) {
					Teacher teacher=new Teacher();
					teacher.setSubjectId(sub.getId());
					List<Teacher> selectAllTeacher=null;
//					不同级别的管理员查询不同范围的对象
					if(admin.getPower()==0){
						selectAllTeacher = teacherService.selectAllTeacher(teacher);
					}else if(admin.getPower()==1){
						Map<String,Integer> adminMap=new HashMap<>();
						adminMap.put("adminId1", admin.getId());
						selectAllTeacher = teacherService.selectTeacherByAdmin(adminMap);
					}else if(admin.getPower()==2){
						Map<String,Integer> adminMap=new HashMap<>();
						adminMap.put("adminId2", admin.getId());
						selectAllTeacher = teacherService.selectTeacherByAdmin(adminMap);
					}
					teacherList.addAll(selectAllTeacher);
				}
			}else{
				Teacher teacher=new Teacher();
				teacher.setSubjectId(Integer.parseInt(subjectId));
				
//				不同级别的管理员查询不同范围的对象
				if(admin.getPower()==0){
					teacherList = teacherService.selectAllTeacher(teacher);
				}else if(admin.getPower()==1){
					Map<String,Integer> adminMap=new HashMap<>();
					adminMap.put("adminId1", admin.getId());
					teacherList = teacherService.selectTeacherByAdmin(adminMap);
				}else if(admin.getPower()==2){
					Map<String,Integer> adminMap=new HashMap<>();
					adminMap.put("adminId2", admin.getId());
					teacherList = teacherService.selectTeacherByAdmin(adminMap);
				}
			}
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
			if(facultyOld!=null){
				adminMap.put("facultyId", facultyOld.getId());
			}else{
				adminMap.put("subjectId", Integer.parseInt(subjectId));
				if(admin.getPower()==0){
				}else if(admin.getPower()==1){
					adminMap.put("adminId",admin.getId());
				}
				
			}
			admin = adminService.selectStudentAdmin(adminMap);
			List<Faculty> facultyList = admin.getFacultyList();
			for (Faculty faculty : facultyList) {
				List<Subject> subjectList = faculty.getSubjectList();
				for (Subject subject : subjectList) {
					List<Student> studentList = subject.getStudentList();
					for (Student student : studentList) {
						idList.add(student.getId());
					}
				}
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
}
