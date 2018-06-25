package com.controller;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.model.Faculty;
import com.model.Student;
import com.model.Subject;
import com.model.Teacher;
import com.service.FacultyService;
import com.service.StudentService;
import com.service.TeacherService;
import com.util.ImportExcelUtil;
import com.util.JsonUtils;
import com.util.PageUtil;
import com.util.ProduceId;

@Controller
@RequestMapping("/teacher")
public class TeacherController {
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private FacultyService facultyService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private PageUtil pageUtil;
	
	/**
	 * 查询所有教师，可带参数，分页，模糊
	 * @param teacher
	 * @param modelMap
	 * @param index
	 * @param pageSize
	 * @param request
	 * @return
	 */
	@RequestMapping("/showAllTeacher")
	public String showAllTeacher(Teacher teacher,ModelMap modelMap,@RequestParam(required=true,defaultValue="1") Integer index,
            @RequestParam(required=false,defaultValue="15") Integer pageSize,HttpServletRequest request,@RequestParam(required=false) String facultyId){
		//查询院系、专业
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		
		PageHelper.startPage(index, pageSize);
		Page<Teacher> teacherList=null;
		if(teacher.getSubjectId()!=null){
			teacherList = (Page<Teacher>) teacherService.selectAllTeacher(teacher);

		}else{
			Map<String,Object> map=new HashMap<>();
			map.put("facultyId", facultyId);
			if(teacher.getUsername()!=null) {
				map.put("username", teacher.getUsername());
			}
			teacherList = (Page<Teacher>) teacherService.selectTeacherByFaculty(map);
		}
		if(facultyId!=null && facultyId!=""){
//			将系、专业对象设置进教师对象内，使查询条件回显
			Faculty faculty=new Faculty();
			faculty.setId(Integer.parseInt(facultyId));
			Subject subject=new Subject();
			subject.setId(teacher.getSubjectId());
			subject.setFaculty(faculty);
			teacher.setSubject(subject);
		}
		pageUtil.setPageInfo(teacherList, index, pageSize,request);
		modelMap.put("teacherList", teacherList);
		modelMap.put("teacher", teacher);
		return "/adminuser/list-user";
	}
	/**
	 * 跳转到更新页面，需要院系、专业传入
	 * @param teacher
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/toUpdate")
	public String toUpdate(Teacher teacher,ModelMap modelMap){
		if(teacher.getId()!=null){
			teacher = teacherService.selectTeacherById(teacher);
			modelMap.put("teacher", teacher);
		}
		List<Faculty> facultyList = facultyService.selectAllFaculty(null);
		modelMap.put("facultyList", facultyList);
		return "/adminuser/edit-user";
	}
	
	/**
	 * 根据有无Id判断进行更新或者新增操作
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/updateInfo")
	public String updateInfo(Teacher teacher){
		List<Teacher> teacherList = teacherService.selectAllTeacher(null);
		for (Teacher tea : teacherList) {
			if(tea.getUsername().equals(teacher.getUsername())&& !tea.getId().equals(teacher.getId())){
				return "same";
			}
		}
		List<Student> studentList = studentService.selectAllStudent(null);
		for (Student student : studentList) {
			if(teacher.getUsername().equals(student.getUsername())) {
				return "same";
			}
		}
		if(teacher.getId()!=null && teacher.getId()!=""){
			
			if(teacherService.updateTeacherSelected(teacher)>0){
				return "success";
			}
		}else{
			List<String> idList = teacherService.selectAllId();
			String newId=null;
			if(idList.size()==0){
//				如果表内没有数据，手动生成id
				newId="te1";
			}else{
				newId=ProduceId.produceUserId(idList);
			}
			if(newId!=null){
				teacher.setId(newId);
			}
			if(teacherService.insertTeacherSelected(teacher)>0){
				return "success";
			}
		}
		return "error";
	}
	
	/**
	 * 删除教师数据
	 * @param teacher
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/deleteTeacher")
	public String deleteTeacher(Teacher teacher){
		if(teacherService.deleteTeacherById(teacher)>0){
			return "success";
		}
		return "error";
	}
	
	/**  
     * 描述：通过传统方式form表单提交方式导入excel文件  
     * @param request  
     * @throws Exception  
     */  
    @RequestMapping(value="uploadExcel",method={RequestMethod.GET,RequestMethod.POST})  
    public  String  uploadExcel(HttpServletRequest request) throws Exception {  
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;    
        System.out.println("通过传统方式form表单提交方式导入excel文件！");  
          
        InputStream in =null;  
        List<List<Object>> listob = null;  
        MultipartFile file = multipartRequest.getFile("upfile");  
        if(file.isEmpty()){  
            throw new Exception("文件不存在！");  
        }  
        in = file.getInputStream();  
        listob = new ImportExcelUtil().getBankListByExcel(in,file.getOriginalFilename());  
        in.close();  
          
        //该处可调用service相应方法进行数据保存到数据库中，现只对数据输出  
        List<Integer> resultList=new ArrayList<>(listob.size());
        try{
        	for (int i = 0; i < listob.size(); i++) {  
                List<Object> lo = listob.get(i); 
                Teacher teacher=new Teacher();
                
                String id=ProduceId.produceUserId(teacherService.selectAllId());
                teacher.setId(id);
                teacher.setUsername(String.valueOf(lo.get(0)));
                teacher.setPassword(String.valueOf(lo.get(1)));
                teacher.setTruename(String.valueOf(lo.get(2)));
                teacher.setSex(Integer.parseInt(String.valueOf(lo.get(3))));
                teacher.setTelephone(String.valueOf(lo.get(4)));
                teacher.setEmail(String.valueOf(lo.get(5)));
                teacher.setJob(String.valueOf(lo.get(6)));
                teacher.setSubjectId(Integer.parseInt(String.valueOf(lo.get(7))));
                teacher.setScreenNumSametime(Integer.parseInt(String.valueOf(lo.get(8))));
                teacher.setRemake(String.valueOf(lo.get(9)));
                resultList.add(teacherService.insertTeacherSelected(teacher));
                System.out.println(teacher);
            } 
        }catch (Exception e) {
			// TODO: handle exception
        	e.printStackTrace();
		}
         
        String result="";
        int i=0;
        for (Integer integer : resultList) {
			if(integer==1){
				i++;
			}
		}
        if(i==0){
        	result+="导入失败！";
        }else{
        	result+="共"+resultList.size()+"条信息，成功导入"+i+"条信息，导入失败"+(resultList.size()-i)+"条信息";
        }
        System.out.println(result);
        return "redirect:showAllTeacher";  
    }
    
    /**
     * 获取教师个人信息
     * @param teacher
     * @return
     */
    @ResponseBody
    @RequestMapping(value="/getTeacherInfo",produces = "text/json;charset=UTF-8")
    public String getTeacherInfo(Teacher teacher){
    	List<Teacher> teacherList = teacherService.selectAllTeacher(teacher);
    	teacher=teacherList.get(0);
    	return JsonUtils.objectToJson(teacher);
    }
}
